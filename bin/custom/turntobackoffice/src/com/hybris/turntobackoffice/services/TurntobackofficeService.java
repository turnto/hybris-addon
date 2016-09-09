package com.hybris.turntobackoffice.services;


import com.hybris.turntobackoffice.enums.SetupType;
import com.hybris.turntobackoffice.jalo.StateTurnFlag;
import com.hybris.turntobackoffice.job.HistoricalTransactionFeedJobFactory;
import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import com.turntoplugin.facades.TurnToContentFacade;
import com.turntoplugin.model.TurnToStaticContentsModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TurntobackofficeService {

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private HistoricalTransactionFeedJobFactory historicalTransactionFeedJobFactory;

    @Resource(name = "catalogFeedService")
    private CatalogFeedService catalogFeedService;

    @Resource(name = "transactionFeedService")
    private TransactionFeedService transactionFeedService;

    @Resource(name = "turnToContentFacade")
    private TurnToContentFacade turnToContentFacade;

    public String createMessage(Boolean flag) {
        String trigger = "off";
        if (flag) trigger = "on";
        return "Module is turned " + trigger;
    }

    public void turnCronJob(boolean isChecked) {
        CronJobModel cronJobModel = getCronJob();

        if (isChecked) {

            if (!cronJobModel.getActive()) {
                cronJobModel.setActive(true);
                modelService.save(cronJobModel);
            }
        } else {
            modelService.remove(cronJobModel);
        }
    }

    public String sendCatalogFeed() throws Exception {
        File feedFile = catalogFeedService.generateCatalogFeedFile();
        return executeRequest(feedFile);
    }

    public String sendTransactionsFeed(Date startDate) throws Exception {
        File historicalOrderFeedsFile = transactionFeedService.generateTransactionFeedFile(startDate);
        return executeRequest(historicalOrderFeedsFile);
    }

    public String sendDailyTransactionsFeed(Date startDate) throws Exception {
        File historicalOrderFeedsFile = transactionFeedService.generateDailyTransactionFeedFile(startDate);
        return executeRequest(historicalOrderFeedsFile);
    }


    public void saveStateTurnFlag(String checkboxName, boolean flag, String setupType) {
        StateTurnFlagModel model = new StateTurnFlagModel();
        model.setCheckboxName(checkboxName);
        model.setFlag(flag);
        model.setSetupType(SetupType.valueOf(setupType.toUpperCase()));
        getModelService().save(model);
    }

    public void saveStateTurnFlag(StateTurnFlagModel turnFlagModel) {
        getModelService().save(turnFlagModel);
    }

    public void saveToTurnToStore(TurnToGeneralStoreModel storeModel) {
        getModelService().save(storeModel);
    }

    public StateTurnFlagModel loadByCheckboxId(String checkboxId) {
        StateTurnFlagModel model = new StateTurnFlagModel();

        final String queryString = "SELECT {" + StateTurnFlag.PK + "} " +
                "FROM {" + StateTurnFlagModel._TYPECODE + "} " +
                "WHERE {" + StateTurnFlagModel.CHECKBOXNAME + "} = ?checkboxId";

        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("checkboxId", checkboxId);

        final SearchResult<StateTurnFlagModel> searchResult = flexibleSearchService.search(query);

        if (searchResult.getCount() > 0) {
            model = searchResult.getResult().iterator().next();
        } else {
            modelService.initDefaults(model);
            model.setCheckboxName(checkboxId);
        }

        return model;
    }

    public void invalidateCache() {
        final String queryInvalidateCache = "SELECT {"
                + TurnToStaticContentsModel.PK
                + "} "
                + "FROM {"
                + TurnToStaticContentsModel._TYPECODE
                + "} ";

        modelService.removeAll(getFlexibleSearchService().search(queryInvalidateCache).getResult());
    }

    public TurnToGeneralStoreModel loadFromTurnToStoreByKey(String key) {

        final String queryString = "SELECT {" + TurnToGeneralStoreModel.PK + "} " +
                "FROM {" + TurnToGeneralStoreModel._TYPECODE + "} " +
                "WHERE {" + TurnToGeneralStoreModel.KEY + "} = ?key";

        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("key", key);

        final SearchResult<TurnToGeneralStoreModel> searchResult = flexibleSearchService.search(query);

        if (searchResult.getCount() > 0) {
            return searchResult.getResult().get(0);
        }

        return null;
    }

    private CronJobModel getCronJob() {
        ServicelayerJobModel sjm = new ServicelayerJobModel();
        sjm.setSpringId("historicalTransactionFeedJob");
        ServicelayerJobModel servicelayerJobModel = flexibleSearchService.getModelByExample(sjm);
        return historicalTransactionFeedJobFactory.createCronJob(servicelayerJobModel);
    }

    private String executeRequest(File file) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("siteKey", turnToContentFacade.getSiteKey())
                .addTextBody("authKey", turnToContentFacade.getAuthKey())
                .addTextBody("feedStyle", "tab-style.1")
                .addBinaryBody("file", file)
                .build();

        HttpPost post = new HttpPost(Config.getParameter("turnto.service.url"));
        post.setEntity(entity);
        HttpResponse response = httpclient.execute(post);



       /* String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";

        URLConnection connection = new URL(Config.getParameter("turnto.service.url")).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)
        ) {
            // Send normal param.
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"siteKey\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
            writer.append(CRLF).append(turnToContentFacade.getSiteKey()).append(CRLF).flush();

            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"authKey\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
            writer.append(CRLF).append(turnToContentFacade.getAuthKey()).append(CRLF).flush();

            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"feedStyle\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
            writer.append(CRLF).append("tab-style.1").append(CRLF).flush();

            // Send text file.
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(file.toPath(), output);
            output.flush();
            writer.append(CRLF).flush();

            // End of multipart/form-data.
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        } catch (Exception e) {
            _logger.error("Error with execution , cause: " + e.getMessage(), e);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        */
        //print result
        _logger.info(response.toString());

        if (response.getStatusLine().getStatusCode() == 200) {
            return "SUCCESS";
        }

        return response.toString();
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

    public HistoricalTransactionFeedJobFactory getHistoricalTransactionFeedJobFactory() {
        return historicalTransactionFeedJobFactory;
    }

    public void setHistoricalTransactionFeedJobFactory(HistoricalTransactionFeedJobFactory historicalTransactionFeedJobFactory) {
        this.historicalTransactionFeedJobFactory = historicalTransactionFeedJobFactory;
    }

}
