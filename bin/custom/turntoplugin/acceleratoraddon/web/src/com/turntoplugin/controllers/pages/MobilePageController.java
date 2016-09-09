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
public class MobilePageController extends AbstractPageController {

    protected static final Logger LOG = Logger.getLogger(MobilePageController.class);
    private static final String turntomobilepage = "turntomobilepage";

    @Resource(name = "simpleBreadcrumbBuilder")
    private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

    @Autowired
    private TurnToContentFacade turnToContentFacade;

    @RequestMapping(value = "/turntomobilepage", method = RequestMethod.GET)
    public String visualContent(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {

        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        turnToContentFacade.populateModelWithTurnToVersion(model);

//        model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("pinboard.visual.content.page.breadcrumb"));
//        storeCmsPageInModel(model, getContentPageForLabelOrId(VC_PINBOARD_CMS_PAGE));
//        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(VC_PINBOARD_CMS_PAGE));

        return TurntopluginControllerConstants.Views.Pages.mobilePage;
    }

}
