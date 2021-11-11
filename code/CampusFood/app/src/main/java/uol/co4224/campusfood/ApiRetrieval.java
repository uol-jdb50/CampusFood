package uol.co4224.campusfood;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ApiRetrieval extends AsyncTask<String, Void, String> implements Serializable {
    public static final String CAMPUSES = "https://prod-08.uksouth.logic.azure.com:443/workflows/ce84ac284a984faf9ae4a3c938f391c1/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=OeNQ5g6WbJncUc8MZek7fvjsyjam0zGAtcK5a2b52j8";//GetAllCampuses
    public static final String LOCATIONS = "https://prod-29.uksouth.logic.azure.com:443/workflows/dacdf9e8b18c427884504ae3054289fa/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=_wnoSQFZgjAJ95fNVM2-8Us9YWjnd7zJt18tDLdZs1A";//GetLocations
    public static final String ALLERGENS = "https://prod-24.uksouth.logic.azure.com:443/workflows/7f35c4652bd944a6a7cc4bfe7d7ca263/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=t12JiqcDnRLk6eRpSiZxUq2CALy7TaCRtqcmxKY4P6g";//GetAllAllergens
    public static final String MENU = "https://prod-31.uksouth.logic.azure.com:443/workflows/9c9f6e89f7a641df90ab5efa738fb61e/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=R1LLp-TR2cqNQ6lrav_d_VfI6HTgSMTKV6nnjGIbEsc";//Unused
    public static final String DEALS = "https://prod-02.uksouth.logic.azure.com:443/workflows/1fd3c8f2afeb44ba8653230c1ef33b21/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=Axegiqsk-nuiBER1CUTgtZt-y5EbABl_V33LiBmujNA";//GetDealsAtLocation
    public static final String OFFERS = "https://prod-31.uksouth.logic.azure.com:443/workflows/7c3723468f7d46458d282643e19a5b4f/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=2D7iCgQjehLPJRolfww_a8MXH2_jTo2ahYaSxDwDJHY";//GetOffers
    public static final String ADVERTS = "";//Unused
    public static final String OPENING_SPEC = "https://prod-24.uksouth.logic.azure.com:443/workflows/1050ac127ae846e8847962fa5f280f92/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=jFHwWYwz3V0MxOjqp7IQJ2Swzy8_AaRRRIAib3-1XzU";//GetSpecificOpening
    public static final String OPENING_GEN = "https://prod-28.uksouth.logic.azure.com:443/workflows/8631e5c1a11546c4844bb5b633dd541e/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=UUHfJYZzRLjpxw4UPNkZZqchVN-5ZVPHjiwFu9_oOBo";//GetGeneralOpening
    public static final String ITEMALLERGENS = "https://prod-18.uksouth.logic.azure.com:443/workflows/b3065359afaf43c0a400226359aeaa49/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=g4KIzE1milDvrepQxXLcBhVRsOMnBAoocv26bZNwBFg";
    public static final String ITEM = "https://prod-18.uksouth.logic.azure.com:443/workflows/06b570ecb0d744909ad3e68516c193b2/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=uUGOsrc7_0KH7sSaxNYC2nzBXllmyZw4niNaD47FLvQ";//GetMenuCategoryItems
    public static final String CATEGORY = "https://prod-31.uksouth.logic.azure.com:443/workflows/3179e68f808342babf6a716e6b2eb1f4/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=lIFgk9qduQlCEOIGvjEV0VfU8gPzuAOFb1CrMe_AML0";//GetCategories
    public static final String SETORDER = "https://prod-31.uksouth.logic.azure.com:443/workflows/44cce86546e543b4b884537b345fec95/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=wMFKGpD_PQZcGF1SrbSMRSE7jEIbHK8daVOa1kuxXvI";//SetOrder
    public static final String GETORDERS = "https://prod-07.uksouth.logic.azure.com:443/workflows/1ae049e46af94e4eb925e9056f041841/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=01p5ne2g3PpXICobEBj4aXmy7RaRQFbcfK4Lq9gZMz0";//GetOrdersForUser
    public static final String CANCEL_ORDER = "https://prod-28.uksouth.logic.azure.com:443/workflows/a6eae657e74047ef8e08994ff3978a40/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=rRjdCOeDXIcdeioTjYUrhj8MNZm2FDKImCs_DBNI7dY";//CancelOrder
    public static final String CHECK_IN = "https://prod-19.uksouth.logic.azure.com:443/workflows/5165d8c0c1644997bddd4398e6e98a48/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=MwxwJQaCVwsDAzQB_wbnARPPFp4-4QIwEXf3RqqUU2I";//OrderCheckIn
    public static final String MANYITEMS = "https://prod-16.uksouth.logic.azure.com:443/workflows/b2e211d5b8c04f9d9c1564b08bb3b59c/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=61-lir2WDW-iTqKSeNVlNqQDznmab0pYPSIY0KXnRbc";//GetManyItems

    public ApiResponse delegate = null;

    @Override
    protected String doInBackground(String... urls) {
        String request = "";
        String output = "";
        URL url;
        try {

            switch (urls[0]) {
                case "campuses":
                    url = new URL(CAMPUSES);
                    break;
                case "locations":
                    url = new URL(LOCATIONS);
                    request += "{\"name\":\"" + urls[1] + "\"}";
                    break;
                case "allergens":
                    url = new URL(ALLERGENS);
                    break;
                case "menu":
                    url = new URL(MENU);
                    request += "{\"date\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\"}";
                    break;
                case "deals":
                    url = new URL(DEALS);
                    request += "{\"date\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\"}";
                    break;
                case "offers":
                    url = new URL(OFFERS);
                    request += "{\"date\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\"}";
                    break;
                case "adverts": //Unimplemented
                    url = new URL(ADVERTS);
                    break;
                case "opening_spec":
                    url = new URL(OPENING_SPEC);
                    request += "{\"date\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\"}";
                    break;
                case "opening_gen":
                    url = new URL(OPENING_GEN);
                    request += "{\"day\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\"}";
                    break;
                case "itemallergens":
                    url = new URL(ITEMALLERGENS);
                    request += "{\"item\":\"" + urls[1] + "\"}";
                    break;
                case "item":
                    url = new URL(ITEM);
                    request += "{\"category\":\"" + urls[1] + "\",\"date\":\"" + urls[2] + "\",\"location\":\"" + urls[3] + "\"}";
                    break;
                case "category":
                    url = new URL(CATEGORY);
                    request += "{\"location\":\"" + urls[1] + "\"}";
                    break;
                case "setorder":
                    url = new URL(SETORDER);
                    request += urls[1];
                    break;
                case "getorders":
                    url = new URL(GETORDERS);
                    request += "{\"user\":\"" + urls[1] + "\",\"date\":\"" + urls[2] + "\"}";
                    break;
                case "cancel":
                    url = new URL(CANCEL_ORDER);
                    request += "{\"order\":\"" + urls[1] + "\"}";
                    break;
                case "checkin":
                    url = new URL(CHECK_IN);
                    request += "{\"order\":\"" + urls[1] + "\",\"location\":\"" + urls[2] + "\",\"campus\":\"" + urls[3] + "\",\"verify\":\"" + urls[4] + "\"}";
                    break;
                case "manyitems":
                    url = new URL(MANYITEMS);
                    request += "{\"items\":";
//                    for (int i = 1; i < urls.length; i++) {
//                        request += "{\"id\":\"" + urls[i] + "\"}";
//                        if (i < urls.length - 1) request += ",";
//                    }
                    request += urls[1];
                    request += "}";
                    break;
                default:
                    return null;
            }

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            con.setRequestProperty("ACCEPT-LANGUAGE", "en-GB,en;0.5");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            con.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(con.getOutputStream());

            dStream.writeBytes(request);
            dStream.flush();
            dStream.close();

            int responseCode = con.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            output += System.getProperty("line.separator") + responseOutput.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    protected void onPostExecute(String result) {
        if (delegate != null) {
            delegate.processFinish(result);
        }
    }

}
