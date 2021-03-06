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
package de.hybris.merchandise.core.suggestion;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.merchandise.core.model.TurnToStaticContentsModel;

import java.util.List;

public interface TurnToContentService {

    List<TurnToStaticContentsModel> getTurnToStaticContents(String productId);

    List<StateTurnFlagModel> getStateTurnFlags();

    List<TurnToGeneralStoreModel> getItemFromTurnToGeneralStore(String key);

}
