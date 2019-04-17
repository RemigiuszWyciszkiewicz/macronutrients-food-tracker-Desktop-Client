package Controller;

import com.mysql.cj.xdevapi.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class HttpRequests {

    public static void deleteRequest(String urlA) throws IOException {




        URL url = new URL(urlA);
        HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("DELETE");
        httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        httpConnection.setRequestProperty("Accept", "application/json");
        httpConnection.setRequestProperty("charset", "UTF-8");

        BufferedReader bufferedReader;
        Integer responseCode = httpConnection.getResponseCode();
        ;
/*
        if (responseCode > 199 && responseCode < 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
        }


        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }


        if(content != null) {
            JSONObject responeFromApi = null;
            try {
                responeFromApi = new JSONObject(content.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responeFromApi;
        }else return null;


*/


    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject sendJsonToApi(JSONObject data,String urlAdress) throws IOException {



        // URL and parameters for the connection, This particulary returns the information passed
        URL url = new URL(urlAdress);
        HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("Accept", "application/json");
        // Not required
        // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

        // Writes the JSON parsed as string to the connection
        DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
        wr.write(data.toString().getBytes());
        Integer responseCode = httpConnection.getResponseCode();

        BufferedReader bufferedReader;

        // Creates a reader buffer
        if (responseCode > 199 && responseCode < 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
        }

        // To receive the response
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }
        bufferedReader.close();

        if(content.length()!=0) {
            JSONObject responeFromApi = null;
            try {
                responeFromApi = new JSONObject(content.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responeFromApi;
        }else return null;
    }
}
