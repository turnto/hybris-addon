package com.hybris.turntobackoffice.job;


import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import com.turntoplugin.facades.TurnToContentFacade;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class HistoricalTransactionFeedJobFactory implements CronJobFactory<CronJobModel, JobModel> {

    public static final String CODE_FEED_CRON_JOB = "historicalTransactionFeedCronJob";
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private TurnToContentFacade turnToContentFacade;

    @Override
    public CronJobModel createCronJob(JobModel historicalTransactionFeedJob) {
        return createCronJobModel(historicalTransactionFeedJob);
    }

    private CronJobModel createCronJobModel(JobModel historicalTransactionFeedJob) {

        CronJobModel cronJobModel = findCronJobModel();

        if (cronJobModel == null) {
            cronJobModel = modelService.create(CronJobModel.class);
            cronJobModel.setActive(Boolean.TRUE);
            cronJobModel.setJob(historicalTransactionFeedJob);
            cronJobModel.setCode(CODE_FEED_CRON_JOB);
            modelService.save(cronJobModel);

            createTrigger(cronJobModel);
        }

        return cronJobModel;
    }

    private CronJobModel findCronJobModel() {

        String sql = "SELECT {" + CronJobModel.PK + "} " + "FROM {" + CronJobModel._TYPECODE + "} WHERE {" + CronJobModel.CODE + "} = ?jobCode";

        final FlexibleSearchQuery query = new FlexibleSearchQuery(sql);
        query.addQueryParameter("jobCode", CODE_FEED_CRON_JOB);

        final SearchResult<CronJobModel> cronJobModels = flexibleSearchService.search(query);

        if (cronJobModels.getResult().isEmpty()) {
            return null;
        }

        return cronJobModels.getResult().get(0);

    }

    private void createTrigger(CronJobModel cronJobModel) {
        final TriggerModel triggerModel = modelService.create(TriggerModel.class);
        triggerModel.setActive(Boolean.TRUE);
        triggerModel.setCronExpression(createCronExpression());
        triggerModel.setCronJob(cronJobModel);

        modelService.save(triggerModel);
    }


    private long getDailyTime() {
        List<TurnToGeneralStoreModel> dailyFeedTimes = turnToContentFacade.getItemFromTurnToGeneralStore("dailyFeedTime");

        if (dailyFeedTimes.isEmpty()) {
            return new Date().getTime();
        }


        return (long) dailyFeedTimes.get(0).getValue();

    }

    private String createCronExpression() {
        long time = getDailyTime();

        DateTime dateTime = new DateTime(time);

        int hourOfDay = dateTime.getHourOfDay();
        int minutes = dateTime.getMinuteOfHour();

        return String.format("0 %d %d * * ?", minutes, hourOfDay);

    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public TurnToContentFacade getTurnToContentFacade() {
        return turnToContentFacade;
    }

    @Required
    public void setTurnToContentFacade(TurnToContentFacade turnToContentFacade) {
        this.turnToContentFacade = turnToContentFacade;
    }
}
