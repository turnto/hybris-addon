package com.hybris.turntobackoffice.job;


import com.hybris.turntobackoffice.services.TurntobackofficeService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

public class HistoricalTransactionFeedJob extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = Logger.getLogger(HistoricalTransactionFeedJob.class.getName());

    private TurntobackofficeService turntobackofficeService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {

        if (clearAbortRequestedIfNeeded(cronJobModel)) {
            LOG.info("The job is aborted.");
            return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }

        try {
            Date currentDate = new Date();
            turntobackofficeService.sendDailyTransactionsFeed(currentDate);
            LOG.info(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            LOG.error("Error occur while send daily transactions Feed ");
        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    public TurntobackofficeService getTurntobackofficeService() {
        return turntobackofficeService;
    }

    @Override
    public boolean isAbortable() {
        return true;
    }

    @Required
    public void setTurntobackofficeService(TurntobackofficeService turntobackofficeService) {
        this.turntobackofficeService = turntobackofficeService;
    }
}
