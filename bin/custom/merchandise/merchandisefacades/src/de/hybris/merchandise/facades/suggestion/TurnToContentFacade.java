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
package de.hybris.merchandise.facades.suggestion;

import de.hybris.platform.commercefacades.product.data.ProductData;
import org.springframework.ui.Model;

import java.util.List;

public interface TurnToContentFacade {

    void populateModelWithContent(Model model, String productId);

    void populateModelWithTurnFlags(Model model);

    void populateModelWithRating(Model model, List<ProductData> productData);

    String getAverageRatingForProduct(String productId);

}
