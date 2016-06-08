/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
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

import de.hybris.merchandise.facades.suggestion.TurnToContentFacade;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for home page
 */
@Controller
@Scope("tenant")
@RequestMapping("/")
public class HomePageController extends AbstractPageController {

    @Autowired
    private TurnToContentFacade turnToContentFacade;

    @RequestMapping(method = RequestMethod.GET)
    public String home(@RequestParam(value = "logout", defaultValue = "false") final boolean logout, final Model model,
                       final RedirectAttributes redirectModel) throws CMSItemNotFoundException {
        if (logout) {
            GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
            return REDIRECT_PREFIX + ROOT;
        }

        storeCmsPageInModel(model, getContentPageForLabelOrId(null));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
        updatePageTitle(model, getContentPageForLabelOrId(null));

        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        turnToContentFacade.populateModelWithTurnToVersion(model);
        turnToContentFacade.populateModelWithTurnToFlags(model);
        turnToContentFacade.populateModelWithSiteKeyValidationFlag(model);

        return getViewForPage(model);
    }

    protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage) {
        storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
    }

    @RequestMapping(value = "/rest/{id}", method = RequestMethod.GET)
    public void getReviewContent(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        response.getWriter().print(turnToContentFacade.getAverageRatingForProduct(id));
    }

}
