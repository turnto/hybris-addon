package com.hybris.turntobackoffice.services;


import com.hybris.turntobackoffice.model.FeedProduct;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TurntobackofficeService {
    public String getHelloWorld() {
        return "Success!";
    }

    @WireVariable
    private CatalogVersionService catalogVersionService;

    @WireVariable
    private FlexibleSearchService flexibleSearchService;

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String HYBRIS_HOME_URL = "http://turnto.zaelab.com:9001";
    private static final String HYBRIS_STORE_PATH = "/store";
    private static final String CATALOG_ID = "hybrisProductCatalog";
    private static final String CATALOG_VERSION = "Online";
    private static final String SITE_KEY = "2qtC5sJ5gVYcfvesite";
    private static final String AUTH_KEY = "5fU9iBPSPCoEQzqauth";
    private static final String FILE_NAME = "turnto_products_feed";
    private static final String FILE_TYPE = ".tsv";

    private final static String TURNTO_SERVICE_URL = "https://www.turnto.com/feedUpload/postfile";

    public String sendCatalogFeed() throws Exception {

        String url = "http://turnto.zaelab.com:9001/store/sendCatalogFeed?site=hybris";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

//        HttpGet get = new HttpGet(URL);
//        try {
//            HttpClientBuilder.create().build().execute(get);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        List<FeedProduct> products = createProductFeed();
//        File file = writeProductsToFile(products);
        return response.toString();
    }

//    private HttpResponse executeRequest(File file) {
//        HttpEntity entity = MultipartEntityBuilder
//                .create()
//                .addTextBody("siteKey", SITE_KEY)
//                .addTextBody("authKey", AUTH_KEY)
//                .addTextBody("feedStyle", "tab-style.1")
//                .addBinaryBody("file", file)
//                .build();
//
//        HttpPost post = new HttpPost(TURNTO_SERVICE_URL);
//        post.setEntity(entity);
//        HttpResponse response = null;
//
//        try {
//            response = HttpClientBuilder.create().build().execute(post);
//        } catch (IOException e) {
//            _logger.error("Error with execution , cause: " + e.getMessage(), e);
//        }
//
//        return response;
//    }

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

            FeedProduct feedProduct = new FeedProduct(model, HYBRIS_HOME_URL);
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
        catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION);
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery("SELECT {cat."
                + CategoryModel.PK
                + "} "
                + "FROM {"
                + CategoryModel._TYPECODE
                + " AS cat} ");

        flexibleSearchQuery.setCatalogVersions(catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION));
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

        if (category.equals(subcategory))
            subcategory = "";
        else
            category = "Clothes/";

        return HYBRIS_HOME_URL + HYBRIS_STORE_PATH + "/Hybris-Catalogue/" + category + subcategory + "/" + model.getName().trim().replace(' ', '-') + "/p/" + model.getCode() + "?site=hybris";
    }
}
