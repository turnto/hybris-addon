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
package de.hybris.merchandise.core.suggestion.impl;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.merchandise.core.model.TurnToStaticContentsModel;
import de.hybris.merchandise.core.suggestion.TurnToContentService;
import de.hybris.merchandise.core.suggestion.dao.TurnToContentDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultTurnToContentService implements TurnToContentService {

    private TurnToContentDao turnToContentDao;

    @Override
    public List<TurnToStaticContentsModel> getTurnToStaticContents(String productId) {
        return turnToContentDao.findTurnToStaticContents(productId);
    }

    @Override
    public List<StateTurnFlagModel> getStateTurnFlags() {
        return turnToContentDao.findStateTurnFlags();
    }

    @Override
    public List<TurnToGeneralStoreModel> getItemFromTurnToGeneralStore(String key) {
        return turnToContentDao.findItemFromTurnToGeneralStore(key);
    }

    public TurnToContentDao getTurnToContentDao() {
        return turnToContentDao;
    }

    @Required
    public void setTurnToContentDao(TurnToContentDao turnToContentDao) {
        this.turnToContentDao = turnToContentDao;
    }
}
