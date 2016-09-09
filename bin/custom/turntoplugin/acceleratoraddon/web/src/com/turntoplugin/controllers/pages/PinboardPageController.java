package com.turntoplugin.controllers.pages;

import com.turntoplugin.controllers.TurntopluginControllerConstants;
import com.turntoplugin.facades.TurnToContentFacade;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for pinboard page
 */
@Controller
@RequestMapping("/pinboard")
public class PinboardPageController extends AbstractPageController {

    protected static final Logger LOG = Logger.getLogger(PinboardPageController.class);
    private static final String VC_PINBOARD_CMS_PAGE = "vcPinboard";
    private static final String CC_PINBOARD_CMS_PAGE = "ccPinboard";

    @Resource(name = "simpleBreadcrumbBuilder")
    private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

    @Autowired
    private TurnToContentFacade turnToContentFacade;

    @RequestMapping(value = "/visual_content", method = RequestMethod.GET)
    public String visualContent(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {

        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        turnToContentFacade.populateModelWithTurnToVersion(model);
        turnToContentFacade.populateModelWithTurnToFlags(model);

        model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("pinboard.visual.content.page.breadcrumb"));
        storeCmsPageInModel(model, getContentPageForLabelOrId(VC_PINBOARD_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(VC_PINBOARD_CMS_PAGE));

        return TurntopluginControllerConstants.Views.Pages.Pinboard.visualContent;
    }

    @RequestMapping(value = "/checkout_comments", method = RequestMethod.GET)
    public String checkoutComments(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {

        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        turnToContentFacade.populateModelWithTurnToVersion(model);
        turnToContentFacade.populateModelWithTurnToFlags(model);

        model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("checkout.comments.content.page.breadcrumb"));
        storeCmsPageInModel(model, getContentPageForLabelOrId(CC_PINBOARD_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CC_PINBOARD_CMS_PAGE));

        return TurntopluginControllerConstants.Views.Pages.Pinboard.checkoutComments;
    }

}
