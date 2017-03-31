package com.turntoplugin.controllers.sso;

import com.turntoplugin.controllers.TurntopluginControllerConstants;
import com.turntoplugin.facades.TurnToContentFacade;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SsoPageController extends AbstractPageController {

    @Resource(name = "userService")
    private UserService userService;
    private HttpSessionRequestCache httpSessionRequestCache;

    @Resource(name="turnToContentFacade")
    private TurnToContentFacade turnToContentFacade;


    @Resource(name = "httpSessionRequestCache")
    public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
    {
        this.httpSessionRequestCache = accHttpSessionRequestCache;
    }


    @RequestMapping(value = "/ssoData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getSSOData() {
        CustomerData customerData = getUser();
        Map<String,String> data = turnToContentFacade.populateDataWithUser(customerData);
        return data;
    }

    @RequestMapping(value = "/sso", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getSSO()
    {
        Map<String, String> data = new HashMap<>();
        UserModel user = userService.getCurrentUser();
        if (!"anonymous".equals(user.getUid())) {
            data.put("user_auth_token", user.getUid());
        }
        else
        {
            data.put("user_auth_token", null);
        }
        return data;
    }

    @RequestMapping(value = "/turntoSuccess", method = RequestMethod.GET)
    public String getTTlogin(final HttpServletRequest request, final HttpServletResponse response)
    {
        return "forward://"+getSessionService().getAttribute("refererTurn").toString();
    }

    @RequestMapping(value = "/turntoLogin", method = RequestMethod.GET)
    public String getTTlogin(@RequestHeader(value = "referer", required = false) final String referer,
                             @RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
                             @RequestParam(required = false, value = "message") final String message,
                             final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
    {
        final LoginForm loginForm = new LoginForm();
        model.addAttribute(loginForm);
        CustomerData customer = getUser();
        turnToContentFacade.populateModelWithUser(model,customer);
        turnToContentFacade.populateModelWithTurnToSiteKey(model);
        if(referer!=null) {
            getSessionService().setAttribute("refererTurn", referer);
            httpSessionRequestCache.saveRequest(request, response);
        }
        return  TurntopluginControllerConstants.Views.Pages.Pinboard.Login;
    }

}
