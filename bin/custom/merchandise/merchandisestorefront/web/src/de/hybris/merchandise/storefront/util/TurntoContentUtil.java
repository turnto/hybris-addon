package de.hybris.merchandise.storefront.util;

import de.hybris.platform.commercefacades.product.data.ProductData;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public void renderReviewContent(Model model, List<ProductData> productData) {
        try {
            Map<String, String> rating = new HashMap<>();

            createRatingAttribute(productData, rating);
            model.addAttribute("rating", rating);
        } catch (IOException e) {
            LOG.error("Error while getting reviews from TurnTo service, cause: ", e);
        }
    }

    public String renderAverageRatingForItem(String id) throws IOException {
        return String.valueOf(getAverageRatingForItem(id));
    }

    private void createRatingAttribute(List<ProductData> productData, Map<String, String> rating) throws IOException {
        for (ProductData pd : productData) {
            String id = pd.getCode();
            int averageRating = getAverageRatingForItem(id);

            rating.put(id, String.valueOf(averageRating));
        }
    }

    private int getAverageRatingForItem(String id) throws IOException {
        int averageRating = 0;
        URL url = new URL(PRODUCT_JSON_URL + id + PRODUCT_JSON_URL_CHUNCK);
        StringBuilder response = getResponse(url);

        if (StringUtils.isNotBlank(response.toString())) averageRating = getOverallRating(response);
        return averageRating;
    }

    private int getOverallRating(StringBuilder response) {
        final JSONObject obj = new JSONObject(response.toString());
        final JSONArray reviews = obj.getJSONArray("reviews");
        int count = 0;
        int sum = 0;
        for (int i = 0; i < reviews.length(); ++i) {
            final JSONObject rating = reviews.getJSONObject(i);
            count++;
            sum += rating.getInt("rating");
        }

        if (count == 0) return 0;
        else return sum / count;
    }

    private void setModelAttribute(Model model, String appendix, StringBuilder response) {
        String modelAttribute = "reviewContent";

        if (StringUtils.isNotBlank(appendix) && appendix.equals("/d/catitemhtml"))
            modelAttribute = "qaContent";

        model.addAttribute(modelAttribute, response);
    }

}
