package com.turntoplugin.dao;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import com.turntoplugin.model.TurnToStaticContentsModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;

public interface TurnToContentDao extends Dao {

    List<TurnToStaticContentsModel> findTurnToStaticContents(String productId);

    List<StateTurnFlagModel> findStateTurnFlags();

    List<TurnToGeneralStoreModel> findItemFromTurnToGeneralStore(String key);

}
