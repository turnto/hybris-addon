package de.hybris.merchandise.storefront.util;

import de.hybris.merchandise.storefront.model.FeedProduct;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogFeedsProcessor {

    @Autowired
    private CatalogVersionService catalogVersionService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String HYBRIS_STORE_PATH = "/store";
    private static final String CATALOG_ID = "hybrisProductCatalog";
    private static final String CATALOG_VERSION = "Online";
    private static final String SITE_KEY = "2qtC5sJ5gVYcfvesite";
    private static final String AUTH_KEY = "5fU9iBPSPCoEQzqauth";
    private static final String FILE_NAME = "turnto_products_feed";
    private static final String FILE_TYPE = ".tsv";

    private final static String TURNTO_SERVICE_URL = "https://www.turnto.com/feedUpload/postfile";

    public HttpResponse sendCatalogFeed(String homeUrl) {
        List<FeedProduct> products = createProductFeed(homeUrl);
        File file = writeProductsToFile(products);
        return executeRequest(file);
    }

    private HttpResponse executeRequest(File file) {
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("siteKey", SITE_KEY)
                .addTextBody("authKey", AUTH_KEY)
                .addTextBody("feedStyle", "tab-style.1")
                .addBinaryBody("file", file)
                .build();

        HttpPost post = new HttpPost(TURNTO_SERVICE_URL);
        post.setEntity(entity);
        HttpResponse response = null;

        try {
            response = HttpClientBuilder.create().build().execute(post);
        } catch (IOException e) {
            _logger.error("Error with execution , cause: " + e.getMessage(), e);
        }

        return response;
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

    private List<FeedProduct> createProductFeed(String homeUrl) {
        List<ProductModel> storeProducts = getStoreProducts();
        List<FeedProduct> feedProducts = new ArrayList<>();

        for (ProductModel model : storeProducts) {
            String itemURL = getItemURL(model, homeUrl);
            String price = getProductPrice(model);

            FeedProduct feedProduct = new FeedProduct(model, homeUrl);
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

    private String getItemURL(ProductModel model, String homeUrl) {
        String category = "Stuff";
        String subcategory = model.getEan();

        if (category.equals(subcategory))
            subcategory = "";
        else
            category = "Clothes/";

        return homeUrl + HYBRIS_STORE_PATH + "/Hybris-Catalogue/" + category + subcategory + "/" + model.getName().trim().replace(' ', '-') + "/p/" + model.getCode() + "?site=hybris";
    }
}