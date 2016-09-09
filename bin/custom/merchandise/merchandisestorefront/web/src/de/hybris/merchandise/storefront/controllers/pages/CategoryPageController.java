/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.merchandise.storefront.controllers.pages;


import com.turntoplugin.facades.TurnToContentFacade;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


/**
 * Controller for a category page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController {


    @Autowired
    private TurnToContentFacade turnToContentFacade;

    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
    public String category(@PathVariable("categoryCode") final String categoryCode, // NOSONAR
                           @RequestParam(value = "q", required = false) final String searchQuery,
                           @RequestParam(value = "page", defaultValue = "0") final int page,
                           @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                           @RequestParam(value = "sort", required = false) final String sortCode, final Model model,
                           final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException {
        turnToContentFacade.populateModelWithTurnToFlags(model);
        renderProductRating(categoryCode, searchQuery, page, showMode, sortCode, model);
        populateBuyerComments(categoryCode, searchQuery, page, showMode, sortCode, model);

        return performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode, model, request, response);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
    public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
                                                      @RequestParam(value = "q", required = false) final String searchQuery,
                                                      @RequestParam(value = "page", defaultValue = "0") final int page,
                                                      @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                      @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
    public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
                                                     @RequestParam(value = "q", required = false) final String searchQuery,
                                                     @RequestParam(value = "page", defaultValue = "0") final int page,
                                                     @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                     @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
    }

    private void renderProductRating(@PathVariable("categoryCode") String categoryCode,
                                     @RequestParam(value = "q", required = false) String searchQuery,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "show", defaultValue = "Page") ShowMode showMode,
                                     @RequestParam(value = "sort", required = false) String sortCode, Model model) {
        final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = getProductCategorySearchPageData(categoryCode, searchQuery, page, showMode, sortCode);

        turnToContentFacade.populateModelWithRating(model, searchPageData.getResults());
    }

    private void populateBuyerComments(String categoryCode, String searchQuery, int page, ShowMode showMode, String sortCode, Model model) {
        final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = getProductCategorySearchPageData(categoryCode, searchQuery, page, showMode, sortCode);

        turnToContentFacade.populateModelBuyerComments(model, searchPageData.getResults());

    }

    private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> getProductCategorySearchPageData(
            String categoryCode, String searchQuery, int page, ShowMode showMode, String sortCode) {

        final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);
        final CategoryPageModel categoryPage = getCategoryPage(category);
        final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(
                categoryCode,
                XSSFilterUtil.filter(searchQuery),
                page,
                showMode,
                sortCode,
                categoryPage);

        categorySearch.doSearch();

        return categorySearch.getSearchPageData();
    }

}