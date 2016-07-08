package com.hybris.turntobackoffice.builder;


import com.hybris.turntobackoffice.model.CategoryPath;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CategoryPathBuilder {

    private UrlResolver<ProductModel> productModelUrlResolver;
    private UrlResolver<CategoryModel> categoryModelUrlResolver;

    public List<CategoryPath> getCategoryPaths(final ProductModel productModel) throws IllegalArgumentException {
        final List<CategoryPath> categoryPathList = new ArrayList<>();

        final Collection<CategoryModel> categoryModels = new ArrayList<>();

        categoryPathList.add(getProductPath(productModel));

        final ProductModel baseProductModel = getBaseProduct(productModel);

        if (!baseProductModel.getCode().equals(productModel.getCode())) {
            categoryPathList.add(getProductPath(baseProductModel));
        }

        categoryModels.addAll(baseProductModel.getSupercategories());

        while (!categoryModels.isEmpty()) {
            CategoryModel toDisplay = null;
            for (final CategoryModel categoryModel : categoryModels) {
                if (!(categoryModel instanceof ClassificationClassModel)) {
                    if (toDisplay == null) {
                        toDisplay = categoryModel;
                    }
                }
            }
            categoryModels.clear();
            if (toDisplay != null) {
                categoryPathList.add(getCategoryPath(toDisplay));
                categoryModels.addAll(toDisplay.getSupercategories());
            }
        }
        Collections.reverse(categoryPathList);
        return categoryPathList;
    }

    protected ProductModel getBaseProduct(final ProductModel product) {
        if (product instanceof VariantProductModel) {
            return getBaseProduct(((VariantProductModel) product).getBaseProduct());
        }
        return product;
    }

    protected CategoryPath getProductPath(final ProductModel product) {
        final String productUrl = getBasePath() + getProductModelUrlResolver().resolve(product);
        return new CategoryPath(product.getCode(), product.getName(), productUrl);
    }

    protected CategoryPath getCategoryPath(final CategoryModel category) {
        final String categoryUrl = getBasePath() + getCategoryModelUrlResolver().resolve(category);
        return new CategoryPath(category.getCode(), category.getName(), categoryUrl);
    }

    private String getBasePath() {
        return Config.getParameter("hybris.main.path") + Config.getParameter("hybris.store.path");
    }

    protected UrlResolver<ProductModel> getProductModelUrlResolver() {
        return productModelUrlResolver;
    }

    @Required
    public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver) {
        this.productModelUrlResolver = productModelUrlResolver;
    }

    protected UrlResolver<CategoryModel> getCategoryModelUrlResolver() {
        return categoryModelUrlResolver;
    }

    @Required
    public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver) {
        this.categoryModelUrlResolver = categoryModelUrlResolver;
    }
}
