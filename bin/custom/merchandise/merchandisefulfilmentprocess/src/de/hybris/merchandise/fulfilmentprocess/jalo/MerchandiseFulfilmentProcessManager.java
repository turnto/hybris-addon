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
package de.hybris.merchandise.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.merchandise.fulfilmentprocess.constants.MerchandiseFulfilmentProcessConstants;

@SuppressWarnings("PMD")
public class MerchandiseFulfilmentProcessManager extends GeneratedMerchandiseFulfilmentProcessManager
{
	public static final MerchandiseFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MerchandiseFulfilmentProcessManager) em.getExtension(MerchandiseFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
