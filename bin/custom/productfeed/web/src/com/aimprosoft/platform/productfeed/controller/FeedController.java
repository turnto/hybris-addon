package com.aimprosoft.platform.productfeed.controller;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FeedController {

//    @Autowired
//    ProductService productService;

    //    @Autowired
//    CategoryModel categoryModel;
    @Autowired
    private ProductDao productDao;

//    @Autowired
//    private CatalogService catalogService;

    @Autowired
    private CatalogVersionService catalogVersionService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @RequestMapping(value = "/")
    public String main() {
//        final CategoryModel category = getCategoryService().getCategoryForCode(categoryCode);
//        CategoryModel category = categoryService.getCategoryForCode(version, categoryCode);
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCode("Clothes");
        catalogVersionService.setSessionCatalogVersion("Stuff", "Staged");
//        ProductManager.getInstance().getAllProducts();
//        List<MyItem> all = TypeManager.getInstance().getComposedType("MyType").getAllInstances();
//productService.getProductsForCategory(Catego)
//        TypeService.getTypeService().getComposedTypeForCode(YourModel._TYPECODE)
//defaultCategoryDao.

//        genericDao.find();

        catalogVersionService.getAllCatalogVersions();
//        catalogService.getAllCatalogs();

        productDao.findProductsByCategory(categoryModel, 1, -1).getResult();

//        List categories = FlexibleSearch.getInstance().search("SELECT {PK} FROM {Category}",Category.class).getResult();


        final StringBuilder categoryQuery = new StringBuilder("SELECT {cat." + CategoryModel.PK + "} ").append("FROM {" + CategoryModel._TYPECODE + " AS cat} ");
        final FlexibleSearchQuery flexiSearchQuery = new FlexibleSearchQuery(categoryQuery.toString());


        final SearchResult<CategoryModel> searchResult = flexibleSearchService.search(flexiSearchQuery);

        final List<CategoryModel> categoryList = searchResult.getResult();

        return "main";



    }

    @RequestMapping(value = "/api")
    public String getProducts(final Model model) {
        model.addAttribute("products", "products");
        return "product";
    }

}
