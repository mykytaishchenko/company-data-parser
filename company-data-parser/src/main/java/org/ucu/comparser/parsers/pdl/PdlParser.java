package org.ucu.comparser.parsers.pdl;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucu.comparser.config.Config;
import org.ucu.comparser.parsers.Parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class PdlParser implements Parser {
    @Setter
    private String API_KEY = new Config().PDL_API_KEY;
    @Setter @Getter
    private JSONObject jsonObject;

    private JSONObject getJsonByUrl(String request) throws JSONException {
        String text = "{}";
        try {
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Api-Key", API_KEY);
            connection.connect();
            text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
        } catch (Exception ignored) {}
        return new JSONObject(text);
    }

    public void setJsonByDomain(String domain) throws JSONException, IOException {
        String raw_query = "SELECT NAME FROM COMPANY WHERE WEBSITE=";
        String API_URL = "https://api.peopledatalabs.com/v5/company/search?sql=";
        String query = URLEncoder.encode(raw_query + "'" + domain + "'", StandardCharsets.UTF_8);
        this.setJsonObject(getJsonByUrl(API_URL + query));
    }

    public HashMap<String, String> getCompanyName() {
        HashMap<String, String> result = new HashMap<>();
        try {
            result.put("companyName", jsonObject.getJSONArray("data").getJSONObject(0).getString("name"));
        } catch (JSONException ignored) {}
        return result;
    }

    public HashMap<String, String> getNumberOfEmployees() {
        HashMap<String, String> result = new HashMap<>();
        try {
            result.put("numberOfEmployees", Integer.toString(jsonObject.getJSONArray("data").getJSONObject(0).getInt("employee_count")));
        } catch (JSONException ignored) {}
        return result;
    }

    public HashMap<String, String> getCompanyLink(String linkName) {
        HashMap<String, String> result = new HashMap<>();
        try {
            result.put(linkName, jsonObject.getJSONArray("data").getJSONObject(0).getString(linkName + "_url"));
        } catch (JSONException ignored) {}
        return result;
    }


    @Override
    public HashMap<String, String> get(String domain) throws IOException, JSONException {
        this.setJsonByDomain(domain);
        return new HashMap<>() {{
            putAll(getCompanyName());
            putAll(getNumberOfEmployees());
            putAll(getCompanyLink("twitter"));
            putAll(getCompanyLink("facebook"));
        }};
    }
}
