package de.hybris.merchandise.storefront.util;

import com.google.gson.Gson;
import de.hybris.merchandise.storefront.model.Product;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class APIUtil {

    @Autowired
    private CatalogVersionService catalogVersionService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    final private static String CATALOG_ID = "hybrisProductCatalog";
    final private static String CATALOG_VERSION = "Online";
    final private static String SITE_KEY = "2qtC5sJ5gVYcfvesite";
    final private static String AUTH_KEY = "5fU9iBPSPCoEQzqauth";
    final private static String FILE_NAME = "turnto_feed_tab.txt";
    final private static String TURNTO_SERVICE_URL = "https://www.turnto.com/feedUpload/postfile";


    public void sendPost() throws IOException {

        writeProductsToFile();

        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("siteKey", SITE_KEY)
                .addTextBody("authKey", AUTH_KEY)
                .addTextBody("feedStyle", "tab-style.1")
                .addBinaryBody("file", new File(FILE_NAME), ContentType.TEXT_PLAIN, FILE_NAME)
                .build();

        HttpPost post = new HttpPost(TURNTO_SERVICE_URL);
        post.setEntity(entity);

        HttpClientBuilder.create().build().execute(post);
    }

    private void writeProductsToFile() throws FileNotFoundException, UnsupportedEncodingException {
        Gson gson = new Gson();

        PrintWriter writer = new PrintWriter(FILE_NAME, "UTF-8");
        writer.print(gson.toJson(createProductFeed()));
        writer.close();
    }

    private List<Product> createProductFeed() {
        List<Product> products = new LinkedList<>();

        for (ProductModel productModel : getStoreProducts()) {
            Product product = new Product();
            product.setSku(productModel.getCode());
            product.setCategory(productModel.getEan());
            product.setCurrency("EUR");
            product.setImageURL(productModel.getPicture().getCode());
            product.setTitle(productModel.getName());
            product.setItemURL(getItemURL(product));
            setProductPrice(productModel, product);

            products.add(product);
        }
        return products;
    }

    private void setProductPrice(ProductModel productModel, Product product) {
        for (PriceRowModel prm : productModel.getEurope1Prices()) {
            if (isDefaultCurrency(prm))
                product.setPrice(String.valueOf(prm.getPrice()));
        }
    }

    private boolean isDefaultCurrency(PriceRowModel prm) {
        return prm.getCurrency().getName().equals("Euro");
    }

    private String getItemURL(Product product) {
        String category = "Stuff";
        String subcategory = product.getCategory();

        if (subcategory.equals(category))
            subcategory = "";
        else
            category = "Clothes/";

        return "/Hybris-Catalogue/"
                + category
                + subcategory
                + "/"
                + product.getTitle().trim().replace(' ', '-')
                + "/p/"
                + product.getSku();
    }

    private List<ProductModel> getStoreProducts() {
        List<ProductModel> productModels = new LinkedList<>();

        for (CategoryModel cm : getCategories()) {
            List<ProductModel> pm = cm.getProducts();
            if (pm.size() > 0) {
                for (ProductModel productModel : pm) {
                    productModel.setEan(cm.getName());
                    productModels.add(productModel);
                }
            }
        }
        return productModels;
    }

    private List<CategoryModel> getCategories() {
        catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION);

        final FlexibleSearchQuery flexiSearchQuery = new FlexibleSearchQuery("SELECT {cat."
                + CategoryModel.PK
                + "} "
                + "FROM {"
                + CategoryModel._TYPECODE
                + " AS cat} ");

        flexiSearchQuery.setCatalogVersions(catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION));

        final SearchResult<CategoryModel> searchResult = flexibleSearchService.search(flexiSearchQuery);

        return searchResult.getResult();
    }
}
