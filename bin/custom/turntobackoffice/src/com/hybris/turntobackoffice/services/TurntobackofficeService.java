package com.hybris.turntobackoffice.services;


import com.hybris.turntobackoffice.enums.SetupType;
import com.hybris.turntobackoffice.jalo.StateTurnFlag;
import com.hybris.turntobackoffice.job.HistoricalTransactionFeedJobFactory;
import com.hybris.turntobackoffice.model.FeedProduct;
import com.hybris.turntobackoffice.model.HistoricalOrderFeed;
import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.merchandise.core.model.TurnToStaticContentsModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TurntobackofficeService {

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String CATALOG_VERSION = "Online";
    private static final String FILE_NAME = "turnto_products_feed";
    private static final String ORDER_FILE_NAME = "turnto_orders_feed";
    private static final String FILE_TYPE = ".tsv";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private ProductFacade productFacade;
    private CatalogVersionService catalogVersionService;
    private FlexibleSearchService flexibleSearchService;
    private BaseStoreService baseStoreService;
    private ModelService modelService;

    private HistoricalTransactionFeedJobFactory historicalTransactionFeedJobFactory;

    private Converter<OrderModel, OrderData> orderConverter;

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

    private CronJobModel getCronJob() {
        ServicelayerJobModel sjm = new ServicelayerJobModel();
        sjm.setSpringId("historicalTransactionFeedJob");
        ServicelayerJobModel servicelayerJobModel = flexibleSearchService.getModelByExample(sjm);
        return historicalTransactionFeedJobFactory.createCronJob(servicelayerJobModel);
    }

    public String sendCatalogFeed() throws Exception {
        List<FeedProduct> products = createProductFeed();
        File productsFile = writeProductsToFile(products);
        return executeRequest(productsFile);
    }

    public String sendTransactionsFeed(Date startDate) throws Exception {
        List<HistoricalOrderFeed> historicalOrderFeeds = createHistoricalOrders(startDate);
        File historicalOrderFeedsFile = writeOrdersToFile(historicalOrderFeeds);
        return executeRequest(historicalOrderFeedsFile);
    }

    public String sendDailyTransactionsFeed(Date startDate) throws Exception {
        List<HistoricalOrderFeed> historicalOrderFeeds = createForLastDayHistoricalOrders(startDate);
        File historicalOrderFeedsFile = writeOrdersToFile(historicalOrderFeeds);
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

    private String executeRequest(File file) throws IOException {

        String charset = "UTF-8";
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
            writer.append(CRLF).append(Config.getParameter("turnto.site.key")).append(CRLF).flush();

            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"authKey\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
            writer.append(CRLF).append(Config.getParameter("turnto.auth.key")).append(CRLF).flush();

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

        //print result
        System.out.println(response.toString());

        return response.toString();
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

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
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

    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService) {
        this.baseStoreService = baseStoreService;
    }

    public Converter<OrderModel, OrderData> getOrderConverter() {
        return orderConverter;
    }

    @Required
    public void setOrderConverter(Converter<OrderModel, OrderData> orderConverter) {
        this.orderConverter = orderConverter;
    }

    public HistoricalTransactionFeedJobFactory getHistoricalTransactionFeedJobFactory() {
        return historicalTransactionFeedJobFactory;
    }

    public void setHistoricalTransactionFeedJobFactory(HistoricalTransactionFeedJobFactory historicalTransactionFeedJobFactory) {
        this.historicalTransactionFeedJobFactory = historicalTransactionFeedJobFactory;
    }

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public BaseStoreService getBaseStoreService() {
        return baseStoreService;
    }

    private File writeProductsToFile(List<FeedProduct> products) {
        File tsv = null;
        try {
            tsv = File.createTempFile(FILE_NAME, FILE_TYPE);
            FileWriter fw = new FileWriter(tsv);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("CATEGORY\tITEMURL\tPRICE\tCURRENCY\tSKU\tIMAGEURL\tTITLE\tBRAND\tMPN\tEAN\t");
            for (FeedProduct product : products) {
                pw.print(product.getCategory() + "\t");
                pw.print(product.getItemURL() + "\t");
                pw.print(product.getPrice() + "\t");
                pw.print(product.getCurrency() + "\t");
                pw.print(product.getSku() + "\t");
                pw.print(product.getImageURL() + "\t");
                pw.print(product.getTitle() + "\t");
                pw.print(product.getBrand() + "\t");
                pw.print(product.getMpn() + "\t");
                pw.print(product.getEan() + "\t");
                pw.println();
            }

            pw.close();
            fw.close();
        } catch (IOException e) {
            _logger.error("Error with writing products to file, cause " + e.getMessage(), e);
        }

        return tsv;
    }

    private List<FeedProduct> createProductFeed() {
        List<ProductModel> storeProducts = getStoreProducts();
        List<FeedProduct> feedProducts = new ArrayList<>();

        for (ProductModel model : storeProducts) {
            String itemURL = getItemURL(model);
            String price = getProductPrice(model);

            FeedProduct feedProduct = new FeedProduct(model, Config.getParameter("hybris.main.path"));
            feedProduct.setItemURL(itemURL);
            feedProduct.setPrice(price);

            feedProducts.add(feedProduct);
        }
        return feedProducts;
    }

    private List<ProductModel> getStoreProducts() {
        List<CategoryModel> categories = getCategories();
        List<ProductModel> productModels = new ArrayList<>();

        for (CategoryModel category : categories) {
            if (!category.getProducts().isEmpty()) {
                for (ProductModel product : category.getProducts()) {
                    product.setSegment(category.getName());
                    productModels.add(product);
                }
            }
        }
        return productModels;
    }

    private List<CategoryModel> getCategories() {
//        catalogVersionService.setSessionCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION);
        String query = "SELECT {cat." + CategoryModel.PK + "} " + "FROM {" + CategoryModel._TYPECODE + " AS cat} WHERE {cat."
                + CategoryModel.CATALOGVERSION + "} = ?catalogVersion";

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
        flexibleSearchQuery.addQueryParameter("catalogVersion", catalogVersionService.getCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION));
//        flexibleSearchQuery.setCatalogVersions(catalogVersionService.getCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION));
        final SearchResult<CategoryModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);
        return searchResult.getResult();
    }

    private String getProductPrice(ProductModel model) {
        String price = "";
        for (PriceRowModel prm : model.getEurope1Prices()) {
            if ("Euro".equalsIgnoreCase(prm.getCurrency().getName())) {
                price = String.valueOf(prm.getPrice());
                break;
            }
        }
        return price;
    }

    private String getItemURL(ProductModel model) {
        String category = "Stuff";
        String subcategory = model.getSegment();

        if (category.equals(subcategory)) subcategory = "";
        else category = "Clothes/";

        return Config.getParameter("hybris.main.path")
                + Config.getParameter("hybris.store.path")
                + Config.getParameter("hybris.catalog.name")
                + category + subcategory + "/"
                + (model.getName() == null ? model.getName() : model.getName().trim().replace(' ', '-'))
                + "/p/" + model.getCode()
                + "?site=hybris";
    }

    private File writeOrdersToFile(List<HistoricalOrderFeed> historicalOrderFeeds) {
        File tsv = null;
        FileWriter fw = null;
        PrintWriter pw = null;

        try {
            tsv = File.createTempFile(ORDER_FILE_NAME, FILE_TYPE);
            fw = new FileWriter(tsv);
            pw = new PrintWriter(fw);
            pw.println("ORDERID\tORDERDATE\tEMAIL\tITEMTITLE\tITEMURL\tITEMLINEID\tZIP\tFIRSTNAME\tLASTNAME\tSKU\tPRICE");
            int lineId = 0;

            for (HistoricalOrderFeed order : historicalOrderFeeds) {

                pw.print(order.getOrderId() + "\t");
                pw.print(order.getOrderDate() + "\t");
                pw.print(order.getEmail() + "\t");
                pw.print(order.getItemTitle() + "\t");
                pw.print(order.getItemURL() + "\t");
                pw.print(++lineId + "\t");
                pw.print(order.getZip() + "\t");
                pw.print(order.getFirstname() + "\t");
                pw.print(order.getLastname() + "\t");
                pw.print(order.getSku() + "\t");
                pw.print(order.getPrice() + "\t");
                pw.println();
            }

        } catch (IOException e) {
            _logger.error("Error with writing products to file, cause " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(pw);
            IOUtils.closeQuietly(fw);
        }

        return tsv;
    }

    private List<HistoricalOrderFeed> createHistoricalOrders(Date startDate) {
        DateTime start = new DateTime(startDate.getTime()).withTime(0, 0, 0, 0);
        DateTime end = start.plusDays(1);

        List<OrderData> orderDataList = getOrdersBetweenDates(start.toDate(), end.toDate());

        List<HistoricalOrderFeed> historicalOrderFeeds = getHistoricalOrderFeeds(orderDataList);

        return historicalOrderFeeds;
    }

    private List<HistoricalOrderFeed> getHistoricalOrderFeeds(List<OrderData> orderDataList) {
        List<HistoricalOrderFeed> historicalOrderFeeds = new ArrayList<>();

        for (OrderData orderData : orderDataList) {
            List<OrderEntryData> products = orderData.getEntries();

            for (OrderEntryData entryData : products) {
                HistoricalOrderFeed orderFeed = new HistoricalOrderFeed();
                populateOrderData(orderFeed, orderData);
                populateProductData(orderFeed, entryData);
                historicalOrderFeeds.add(orderFeed);
            }
        }

        return  historicalOrderFeeds;

    }

    private List<HistoricalOrderFeed> createForLastDayHistoricalOrders(Date startDate) {
        DateTime end = new DateTime(startDate.getTime());
        DateTime start = end.minusDays(1);

        List<OrderData> orderDataList = getOrdersBetweenDates(start.toDate(), end.toDate());

        List<HistoricalOrderFeed> historicalOrderFeeds = getHistoricalOrderFeeds(orderDataList);

        return historicalOrderFeeds;
    }

    private List<OrderData> getOrdersBetweenDates(Date startDate, Date endDate) {

        String query = "SELECT {pk} FROM {" + OrderModel._TYPECODE + "} " + " WHERE {versionID} IS NULL " +
                "AND {store} = ?store " +
                "AND {" + OrderModel.CREATIONTIME + "} >= ?startDate " +
                "AND {" + OrderModel.CREATIONTIME + "} < ?endDate";

        final BaseStoreModel currentBaseStore = getBaseStoreService().getBaseStoreForUid(Config.getParameter("hybris.store.uid"));

        HashMap queryParams = new HashMap();

        queryParams.put("store", currentBaseStore);
        queryParams.put("startDate", startDate);
        queryParams.put("endDate", endDate);

        SearchResult<OrderModel> result = getFlexibleSearchService().search(query, queryParams);

        return Converters.convertAll(result.getResult(), getOrderConverter());
    }

    private void populateOrderData(HistoricalOrderFeed orderFeed, OrderData orderData) {
        orderFeed.setOrderId(orderData.getCode());
        orderFeed.setOrderDate(DATE_FORMAT.format(orderData.getCreated()));
        orderFeed.setFirstname(parseFirstName(orderData.getUser().getName()));
        orderFeed.setLastname(parseLastName(orderData.getUser().getName()));
        orderFeed.setEmail(orderData.getUser().getUid());
        orderFeed.setZip(orderData.getDeliveryAddress().getPostalCode());

    }

    private void populateProductData(HistoricalOrderFeed orderFeed, OrderEntryData entryData) {
        orderFeed.setItemTitle(entryData.getProduct().getName());
        orderFeed.setSku(entryData.getProduct().getCode());
        orderFeed.setItemURL(Config.getParameter("hybris.main.path") + Config.getParameter("hybris.store.path") + entryData.getProduct().getUrl());
        orderFeed.setPrice(entryData.getBasePrice().getValue().toString());

    }

    private String parseLastName(String name) {
        String[] names = name.split(" ");
        if (names.length > 1) {
            return names[1];
        }

        return "";
    }

    private String parseFirstName(String name) {
        String[] names = name.split(" ");
        if (names.length > 0) {
            return names[0];
        }

        return "";
    }

}
