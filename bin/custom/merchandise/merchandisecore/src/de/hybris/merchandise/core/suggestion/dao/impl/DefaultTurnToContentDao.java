package de.hybris.merchandise.core.suggestion.dao.impl;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.merchandise.core.model.TurnToStaticContentsModel;
import de.hybris.merchandise.core.suggestion.dao.TurnToContentDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("turnToContentDao")
public class DefaultTurnToContentDao extends AbstractItemDao implements TurnToContentDao {


    final String queryTurnToStaticContents = "SELECT {" + TurnToStaticContentsModel.PK + "} "
            + "FROM {" + TurnToStaticContentsModel._TYPECODE + " } "
            + " WHERE {" + TurnToStaticContentsModel.PRODUCTID + "} = ?productId";

    final String queryStateTurnFlags = "SELECT {" + StateTurnFlagModel.PK + "} " +
            "FROM {" + StateTurnFlagModel._TYPECODE + "} ";


    final String queryItemFromTurnToGeneralStore = "SELECT {" + TurnToGeneralStoreModel.PK + "} " +
            "FROM {" + TurnToGeneralStoreModel._TYPECODE + "} " +
            "WHERE {" + TurnToGeneralStoreModel.KEY + "} = ?key";

    @Override
    public List<TurnToStaticContentsModel> findTurnToStaticContents(String productId) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryTurnToStaticContents);
        query.addQueryParameter("productId", productId);

        final SearchResult<TurnToStaticContentsModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }

    @Override
    public List<StateTurnFlagModel> findStateTurnFlags() {
        final SearchResult<StateTurnFlagModel> searchResult = getFlexibleSearchService().search(queryStateTurnFlags);
        return searchResult.getResult();

    }

    @Override
    public List<TurnToGeneralStoreModel> findItemFromTurnToGeneralStore(String key) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryItemFromTurnToGeneralStore);
        query.addQueryParameter("key", key);

        final SearchResult<TurnToGeneralStoreModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
