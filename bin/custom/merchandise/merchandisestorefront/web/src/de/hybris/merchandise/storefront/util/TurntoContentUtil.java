package de.hybris.merchandise.storefront.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class TurntoContentUtil {

    private static final Logger LOG = Logger.getLogger(TurntoContentUtil.class);
    private static final String SOURCE_URL = "http://static.www.turnto.com/sitedata/2qtC5sJ5gVYcfvesite/v4_2/";
    private static final String[] APPENDIXES = {"/d/catitemreviewshtml", "/d/catitemhtml"};

    public void renderContent(Model model, String id) {

        try {
            for (String appendix : APPENDIXES) {
                URL url = new URL(SOURCE_URL + id + appendix);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                in.close();

                setModelAttribute(model, appendix, response);
            }
        } catch (IOException e) {
            LOG.error("Error while getting content from TurnTo service, cause: ", e);
        }
    }

    private void setModelAttribute(Model model, String appendix, StringBuilder response) {
        String modelAttribute = "reviewContent";

        if (appendix.equals("/d/catitemhtml"))
            modelAttribute = "qaContent";

        model.addAttribute(modelAttribute, response);
    }

}
