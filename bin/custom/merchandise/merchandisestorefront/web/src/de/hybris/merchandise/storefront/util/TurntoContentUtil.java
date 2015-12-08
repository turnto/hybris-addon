package de.hybris.merchandise.storefront.util;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TurntoContentUtil {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    private static final Logger LOG = Logger.getLogger(TurntoContentUtil.class);
    private static final String SOURCE_URL = "http://static.www.turnto.com/sitedata/2qtC5sJ5gVYcfvesite/v4_2/";
    private static final String[] APPENDIXES = {"/d/catitemreviewshtml", "/d/catitemhtml"};
    private static final String PRODUCT_JSON_URL = "http://static.www.turnto.com/sitedata/2qtC5sJ5gVYcfvesite/";
    private static final String PRODUCT_JSON_URL_CHUNCK = "/d/exportjson/5fU9iBPSPCoEQzqauth";

    public void renderContent(Model model, String id) {
        try {
            for (String appendix : APPENDIXES) {
                URL url = new URL(SOURCE_URL + id + appendix);
                StringBuilder response = getResponse(url);

                setModelAttribute(model, appendix, response);
            }
        } catch (IOException e) {
            LOG.error("Error while getting content from TurnTo service, cause: ", e);
        }
    }

    public void renderReviewContent(Model model, List<ProductData> productData) {
        Map<String, String> rating = new HashMap<>();

        createRatingAttribute(productData, rating);
        model.addAttribute("rating", rating);
    }

    public String getAverageRatingForProduct(String id) {
        return getAverageRatingById(id);
    }

    public void setTurnFlags(Model model) {
        Map<String, StateTurnFlagModel> flagModelMap = new HashMap<>();

        final String queryString = "SELECT {" + StateTurnFlagModel.PK + "} " +
                "FROM {" + StateTurnFlagModel._TYPECODE + "} ";

        final SearchResult<StateTurnFlagModel> searchResult = flexibleSearchService.search(queryString);

        if (searchResult.getCount() > 0) {

            for (StateTurnFlagModel turnFlagModel : searchResult.getResult()) {
                flagModelMap.put(turnFlagModel.getCheckboxName(), turnFlagModel);
            }

            model.addAttribute("flags", flagModelMap);
        }

    }

    private StringBuilder getResponse(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();
        return response;
    }

    private void createRatingAttribute(List<ProductData> productData, Map<String, String> rating) {
        for (ProductData pd : productData) {
            String id = pd.getCode();
            String averageRating = getAverageRatingById(id);

            rating.put(id, averageRating);
        }
    }

    private String getAverageRatingById(String id) {
        String averageRating = "";
        try {
            URL url = new URL(PRODUCT_JSON_URL + id + PRODUCT_JSON_URL_CHUNCK);
            StringBuilder response = getResponse(url);

            if (StringUtils.isNotBlank(response.toString())) averageRating = parseAverageRating(response);
        } catch (IOException e) {
            LOG.error("Error while getting average rating from TurnTo service, cause: ", e);
        }
        return averageRating;
    }

    private String parseAverageRating(StringBuilder response) {
        try {
            final JSONObject obj = new JSONObject(response.toString());
            final JSONObject reviews = (JSONObject) obj.getJSONArray("reviews").get(0);
            final JSONObject item = (JSONObject) reviews.get("item");

            return String.valueOf(item.get("averageRating"));

        } catch (JSONException e) {
            LOG.info("There are no reviews for the item yet");
            return "0";
        }
    }

    private void setModelAttribute(Model model, String appendix, StringBuilder response) {
        String modelAttribute = "reviewContent";

        if (StringUtils.isNotBlank(appendix) && appendix.equals("/d/catitemhtml"))
            modelAttribute = "qaContent";

        model.addAttribute(modelAttribute, response);
    }

}
