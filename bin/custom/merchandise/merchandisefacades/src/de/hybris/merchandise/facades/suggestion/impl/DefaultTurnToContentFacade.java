
package de.hybris.merchandise.facades.suggestion.impl;

import com.hybris.turntobackoffice.model.StateTurnFlagModel;
import com.hybris.turntobackoffice.model.TurnToGeneralStoreModel;
import de.hybris.merchandise.core.model.TurnToStaticContentsModel;
import de.hybris.merchandise.core.suggestion.TurnToContentService;
import de.hybris.merchandise.facades.suggestion.TurnToContentFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Required;
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
public class DefaultTurnToContentFacade implements TurnToContentFacade {
    private static final Logger LOG = Logger.getLogger(DefaultTurnToContentFacade.class);


    private TurnToContentService turnToContentService;
    private ModelService modelService;
    private TurnToGeneralStoreModel invalidResponseModel;

    private static final String QA_CONTENT = "/d/catitemhtml";
    private static final String SOURCE_URL = "http://static.www.turnto.com/sitedata/";
    private static final String[] APPENDIXES = {"/d/catitemreviewshtml", "/d/catitemhtml"};
    private static final String PRODUCT_JSON_URL = "http://static.www.turnto.com/sitedata/";
    private static final String PRODUCT_JSON_URL_CHUNCK = "/d/exportjson/5fU9iBPSPCoEQzqauth";
    private static final String DEFAULT_VERSION = "4_2";

    @Override
    public void populateModelWithContent(Model model, String productId) {

        List<TurnToStaticContentsModel> search = turnToContentService.getTurnToStaticContents(productId);
        turnToCaching(productId, search);

        List<TurnToStaticContentsModel> tm = turnToContentService.getTurnToStaticContents(productId);
        setModelAttribute(model, tm.get(0).getQaContent(), tm.get(0).getReviewsContent());
    }

    @Override
    public void populateModelWithTurnToFlags(Model model) {
        Map<String, StateTurnFlagModel> flagModelMap = new HashMap<>();

        final List<StateTurnFlagModel> searchResult = turnToContentService.getStateTurnFlags();

        if (searchResult.size() > 0) {

            for (StateTurnFlagModel turnFlagModel : searchResult) {
                flagModelMap.put(turnFlagModel.getCheckboxName(), turnFlagModel);
            }

            model.addAttribute("flags", flagModelMap);
        }
    }

    @Override
    public void populateModelWithTurnToSiteKey(Model model) {
        model.addAttribute("siteKey", getSiteKey());
    }

    @Override
    public void populateModelWithSiteKeyValidationFlag(Model model) {
        model.addAttribute("isSiteKeyInvalid", isSiteKeyInvalid());
    }

    @Override
    public void populateModelWithRating(Model model, List<ProductData> productData) {
        Map<String, String> rating = new HashMap<>();
        createRatingAttribute(productData, rating);

        model.addAttribute("rating", rating);
    }

    @Override
    public void populateModelBuyerComments(Model model, List<ProductData> productData) {
        Map<String, String> comments = new HashMap<>();
        createCommentsAttribute(productData, comments);

        model.addAttribute("countBuyerComment", comments);
    }

    @Override
    public void populateModelWithTurnToVersion(Model model) {
        model.addAttribute("currentVersion", getCurrentVersion());
    }

    public String getAverageRatingForProduct(String id) {
        return getAverageRatingById(id);
    }

    public ModelService getModelService() {
        return modelService;
    }

    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public TurnToContentService getTurnToContentService() {
        return turnToContentService;
    }

    @Required
    public void setTurnToContentService(TurnToContentService turnToContentService) {
        this.turnToContentService = turnToContentService;
    }

    private void createRatingAttribute(List<ProductData> productData, Map<String, String> rating) {
        for (ProductData pd : productData) {
            String id = pd.getCode();
            String averageRating = getAverageRatingById(id);

            rating.put(id, averageRating);
        }
    }

    private void createCommentsAttribute(List<ProductData> productData, Map<String, String> comments) {
        for (ProductData pd : productData) {
            String id = pd.getCode();
            String averageRating = getCountBuyerCommentsById(id);

            comments.put(id, averageRating);
        }
    }

    private String getCountBuyerCommentsById(String id) {
        String countBuyerComments = "";
        try {
            StringBuilder response = getExportFromTurnTo(id);

            if (StringUtils.isNotBlank(response.toString())) {
                countBuyerComments = getValueFromTurnToItem(response, "comments", "commentCount", "0");
            }

        } catch (IOException e) {
            LOG.error("Error while getting count buyer comments from TurnTo service, cause: ", e);
        }
        return countBuyerComments;
    }

    private String getAverageRatingById(String id) {
        String averageRating = "";
        try {
            StringBuilder response = getExportFromTurnTo(id);

            if (StringUtils.isNotBlank(response.toString())){
                averageRating = getValueFromTurnToItem(response, "reviews", "averageRating", "0");
            }

        } catch (IOException e) {
            LOG.error("Error while getting average rating from TurnTo service, cause: ", e);
        }
        return averageRating;
    }

    private StringBuilder getExportFromTurnTo(String id) throws IOException {
        URL url = new URL(PRODUCT_JSON_URL + getSiteKey() + "/" + id + PRODUCT_JSON_URL_CHUNCK);
        return getResponse(url);
    }

    private String getValueFromTurnToItem(StringBuilder response, String arrayKey, String key, String defaultValue) {
        try {
            final JSONObject obj = new JSONObject(response.toString());
            final JSONObject reviews = (JSONObject) obj.getJSONArray(arrayKey).get(0);
            final JSONObject item = (JSONObject) reviews.get("item");

            return String.valueOf(item.get(key));

        } catch (JSONException e) {
            LOG.info("There are no " + arrayKey + " for the item yet");
            return defaultValue;
        }
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


    private void turnToCaching(String id, List<TurnToStaticContentsModel> searchResult) {
        if (isNeedToSave(searchResult)) {
            TurnToStaticContentsModel contentsModel = fillTurnToContentModel(id);
            modelService.save(contentsModel);
        }
    }

    private boolean isNeedToSave(List<TurnToStaticContentsModel> searchResult) {
        boolean needToSave = true;
        if (searchResult.size() > 0) {
            TurnToStaticContentsModel tm = searchResult.get(0);
            needToSave = isCachingTimeOver(tm);

            if (needToSave) modelService.remove(tm);
        }
        return needToSave;
    }

    private boolean isCachingTimeOver(TurnToStaticContentsModel tm) {
        return getTimestampWithCachingLimit(tm) < getTimestamp();
    }

    private TurnToStaticContentsModel fillTurnToContentModel(String id) {
        TurnToStaticContentsModel model = new TurnToStaticContentsModel();
        model.setProductId(id);
        model.setTimestamp(getTimestamp());

        String currentVersion = "/v" + getCurrentVersion() + "/";

        try {
            for (String appendix : APPENDIXES) {
                URL url = new URL(SOURCE_URL + getSiteKey() + currentVersion + id + appendix);
                StringBuilder response = getResponse(url);

                validateResponse(response);
                setModelContent(model, appendix, response);
            }
        } catch (IOException e) {
            LOG.error("Error while getting content from TurnTo service, cause: ", e);
        }
        return model;
    }

    private void validateResponse(StringBuilder response) {
        invalidResponseModel = getSiteKeyValidationModel();
        if (response.toString().contains("site does not exist")) {
            LOG.error(response);
            invalidResponseModel.setValue(true);
        } else {
            invalidResponseModel.setValue(false);
        }

        modelService.save(invalidResponseModel);
    }

    private TurnToGeneralStoreModel getSiteKeyValidationModel() {
        return turnToContentService.getItemFromTurnToGeneralStore("isSiteKeyInvalid").get(0);
    }

    private long getTimestampWithCachingLimit(TurnToStaticContentsModel model) {
        DateTime dt = new DateTime(model.getTimestamp());
        return dt.plusMinutes(loadCachingTime()).getMillis();
    }

    private long getTimestamp() {
        return DateTimeUtils.currentTimeMillis();
    }

    private void setModelContent(TurnToStaticContentsModel model, String appendix, StringBuilder response) {
        if (StringUtils.isNotBlank(appendix) && appendix.equals(QA_CONTENT))
            model.setQaContent(String.valueOf(response));//longtext
        else
            model.setReviewsContent(String.valueOf(response));//longtext
    }

    private void setModelAttribute(Model model, String qa, String reviews) {
        model.addAttribute("qaContent", qa);
        model.addAttribute("reviewContent", reviews);
    }

    private Integer loadCachingTime() {
        final List<TurnToGeneralStoreModel> searchResult = turnToContentService.getItemFromTurnToGeneralStore("cachingTime");

        if (searchResult.size() > 0) {
            return (Integer) searchResult.get(0).getValue();
        }

        return 1;
    }

    private String getSiteKey() {
        return (String) turnToContentService.getItemFromTurnToGeneralStore("siteKey").get(0).getValue();
    }

    private Boolean isSiteKeyInvalid() {
        return (Boolean) getSiteKeyValidationModel().getValue();
    }

    private String getCurrentVersion() {
        List<TurnToGeneralStoreModel> selectboxVersion = turnToContentService.getItemFromTurnToGeneralStore("selectboxVersion");

        if (selectboxVersion.isEmpty()) {
            return DEFAULT_VERSION;
        }

        return (String) selectboxVersion.get(0).getValue();
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

}
