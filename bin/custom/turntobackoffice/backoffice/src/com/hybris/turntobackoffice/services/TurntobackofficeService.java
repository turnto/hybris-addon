package com.hybris.turntobackoffice.services;


import com.hybris.turntobackoffice.enums.SetupType;
import com.hybris.turntobackoffice.jalo.StateTurnFlag;
import com.hybris.turntobackoffice.model.FeedProduct;
import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TurntobackofficeService {

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String CATALOG_VERSION = "Online";
    private static final String FILE_NAME = "turnto_products_feed";
    private static final String FILE_TYPE = ".tsv";

    private CatalogVersionService catalogVersionService;
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;

    public String createMessage(Boolean flag) {
        String trigger = "off";
        if (flag) trigger = "on";
        return "Module is turned " + trigger;
    }

    public String sendCatalogFeed() throws Exception {

        List<FeedProduct> products = createProductFeed();
        File productsFile = writeProductsToFile(products);
        return executeRequest(productsFile);


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

    public TurnToGeneralStoreModel loadFromTurntoToStoreByKey(String key) {

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

    private File writeProductsToFile(List<FeedProduct> products) {
        File tsv = null;
        try {
            tsv = File.createTempFile(FILE_NAME, FILE_TYPE);
            FileWriter fw = new FileWriter(tsv);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("CATEGORY\tITEMURL\tPRICE\tCURRENCY\tSKU\tIMAGEURL\tTITLE\t");
            for (FeedProduct product : products) {
                pw.print(product.getCategory() + "\t");
                pw.print(product.getItemURL() + "\t");
                pw.print(product.getPrice() + "\t");
                pw.print(product.getCurrency() + "\t");
                pw.print(product.getSku() + "\t");
                pw.print(product.getImageURL() + "\t");
                pw.print(product.getTitle() + "\t");
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
                    product.setEan(category.getName());
                    productModels.add(product);
                }
            }
        }
        return productModels;
    }

    private List<CategoryModel> getCategories() {
        catalogVersionService.setSessionCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION);
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery("SELECT {cat."
                + CategoryModel.PK
                + "} "
                + "FROM {"
                + CategoryModel._TYPECODE
                + " AS cat} ");

        flexibleSearchQuery.setCatalogVersions(catalogVersionService.getCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION));
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
        String subcategory = model.getEan();

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
}
