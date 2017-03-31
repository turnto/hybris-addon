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
package com.turntoplugin.controllers.sso;

import com.turntoplugin.controllers.TurntopluginControllerConstants;
import com.turntoplugin.facades.TurnToContentFacade;
import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * Checkout Login Controller. Handles login and register for the checkout flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login/turn")
public class TurnLoginController extends AbstractLoginPageController {
    @Resource(name = "checkoutFlowFacade")
    private CheckoutFlowFacade checkoutFlowFacade;

    @Resource(name = "guidCookieStrategy")
    private GUIDCookieStrategy guidCookieStrategy;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource(name="turnToContentFacade")
    private TurnToContentFacade turnToContentFacade;

    @Resource(name = "userService")
    private UserService userService;
    private HttpSessionRequestCache httpSessionRequestCache;

    @Override
    protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException {
        return getContentPageForLabelOrId("login");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String doCheckoutLogin(@RequestHeader(value = "referer", required = false) final String referer,
                                  @RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
                                  @RequestParam(required = false, value = "message") final String message,
                                  final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) throws CMSItemNotFoundException {
        return getDefaultLoginPage(loginError, session, model, message);
    }

    @Override
    protected String getView() {
        return TurntopluginControllerConstants.Views.Pages.Pinboard.Login;
    }

    @Override
    protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response) {
        return "/";
    }

    protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model, String message)
            throws CMSItemNotFoundException {
        final LoginForm loginForm = new LoginForm();
        model.addAttribute(loginForm);
        final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
        if (username != null) {
            session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
        }

        loginForm.setJ_username(username);

        if (loginError) {
//            model.addAttribute("loginError", Boolean.valueOf(loginError));
//            GlobalMessages.addErrorMessage(model, message.equalsIgnoreCase("login.error.account.without.group.or.unit.title")
//                    ? message : "login.error.account.not.found.title");
            model.addAttribute("loginError", Boolean.valueOf(loginError));
            GlobalMessages.addErrorMessage(model, "login.error.account.not.found.title");
        }
//        CustomerData customer = getUser();
//        turnToContentFacade.populateModelWithUser(model,customer);
        Map<String, String> data = new HashMap<String, String>();
        UserModel user = userService.getCurrentUser();
        data.put("user_auth_token",user.getUid());
        model.addAttribute("users",data);
        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        return getView();
    }


    protected CheckoutFlowFacade getCheckoutFlowFacade() {
        return checkoutFlowFacade;
    }

    @Override
    protected GUIDCookieStrategy getGuidCookieStrategy() {
        return guidCookieStrategy;
    }

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }
}
