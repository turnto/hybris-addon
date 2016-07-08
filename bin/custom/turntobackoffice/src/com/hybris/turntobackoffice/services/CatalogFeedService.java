package com.hybris.turntobackoffice.services;


import com.google.gson.Gson;
import com.hybris.turntobackoffice.builder.CategoryPathBuilder;
import com.hybris.turntobackoffice.model.CategoryPath;
import com.hybris.turntobackoffice.model.FeedProduct;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CatalogFeedService {

    private Logger _logger = LoggerFactory.getLogger(getClass());

    private static final String CATALOG_VERSION = "Online";
    private static final String FILE_NAME = "turnto_products_feed";
    private static final String FILE_TYPE = ".tsv";

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "categoryPathBuilder")
    private CategoryPathBuilder categoryPathBuilder;

    public File generateCatalogFeedFile() {
        List<FeedProduct> products = createProductFeed();
        File productsFile = writeProductsToFile(products);
        return productsFile;
    }

    private File writeProductsToFile(List<FeedProduct> products) {
        File tsv = null;
        try {
            tsv = File.createTempFile(FILE_NAME, FILE_TYPE);
            FileWriter fw = new FileWriter(tsv);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("CATEGORYPATHJSON\tCATEGORY\tITEMURL\tPRICE\tCURRENCY\tSKU\tIMAGEURL\tTITLE\tBRAND\tMPN\tEAN\t");
            for (FeedProduct product : products) {
                pw.print(product.getCategorypathjson() + "\t");
                pw.print(product.getCategory() + "\t");
                pw.print(product.getItemURL() + "\t");
                pw.print(product.getPrice() + "\t");
                pw.print(product.getCurrency() + "\t");
                pw.print(product.getSku() + "\t");
                pw.print(product.getImageURL() + "\t");
                pw.print(product.getTitle() + "\t");
                pw.print(product.getBrand() + "\t");
                pw.print(product.getMpn() + "\t");
                pw.println(product.getEan() + "\t");
            }

            pw.close();
            fw.close();
        } catch (IOException e) {
            _logger.error("Error with writing products to file, cause " + e.getMessage(), e);
        }

        return tsv;
    }

    private List<FeedProduct> createProductFeed() {
        List<FeedProduct> feedProducts = new ArrayList<>();

        List<ProductModel> products = getProducts();
        Gson gson = new Gson();

        for (ProductModel model : products) {
            String price = getProductPrice(model);
            List<CategoryPath> categoryPathList = categoryPathBuilder.getCategoryPaths(model);
            String itemURL = getItemURL(categoryPathList);
            String categoryPathJson = gson.toJson(categoryPathList);

            FeedProduct feedProduct = new FeedProduct(model, Config.getParameter("hybris.main.path"));
            feedProduct.setPrice(price);
            feedProduct.setItemURL(itemURL);
            feedProduct.setCategorypathjson(categoryPathJson);

            feedProducts.add(feedProduct);
        }

        return feedProducts;
    }

    private String getItemURL(List<CategoryPath> categoryPathList) {
        CategoryPath categoryPath = categoryPathList.get(categoryPathList.size() - 1);
        return categoryPath.getUrl();
    }

    private List<ProductModel> getProducts() {
        String query = "SELECT {" + ProductModel.PK + "} " + "FROM {" + ProductModel._TYPECODE + "} WHERE {" + ProductModel.CATALOGVERSION + "} = ?catalogVersion AND" +
                " {" + ProductModel.VARIANTTYPE + "} IS NULL";

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
        flexibleSearchQuery.addQueryParameter("catalogVersion", catalogVersionService.getCatalogVersion(Config.getParameter("hybris.catalog.id"), CATALOG_VERSION));
        final SearchResult<ProductModel> searchResult = flexibleSearchService.search(flexibleSearchQuery);
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

}
