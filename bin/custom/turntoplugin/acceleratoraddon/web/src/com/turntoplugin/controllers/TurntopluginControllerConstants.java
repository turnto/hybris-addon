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
package com.turntoplugin.controllers;


public interface TurntopluginControllerConstants
{

    String ADDON_PREFIX = "addon:/turntoplugin/";

    interface Views
    {
        interface Cms
        {
            String ComponentPrefix = ADDON_PREFIX + "cms/";
        }

        interface Pages
        {

            interface Pinboard
            {
                String visualContent = ADDON_PREFIX + "/pages/pinboard/vcPinboard";
                String checkoutComments = ADDON_PREFIX + "pages/pinboard/ccPinboard";
                String Login = ADDON_PREFIX + "pages/pinboard/accountReturningCustomerLogin";
            }

            String mobilePage = ADDON_PREFIX + "/pages/mobile/turntomobilepage";

        }
    }
}
