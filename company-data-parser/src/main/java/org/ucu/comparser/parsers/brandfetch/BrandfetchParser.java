package org.ucu.comparser.parsers.brandfetch;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import org.ucu.comparser.config.Config;
import org.ucu.comparser.parsers.Parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;


public class BrandfetchParser implements Parser {
    @Setter
    private String API_KEY = new Config().BRANDFATCH_API_KEY;
    @Setter @Getter
    private JSONObject jsonObject = new JSONObject();


    public JSONObject getJsonByUrl(String request) throws JSONException {
        String text = "{}";

        try {
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", this.API_KEY);
            connection.connect();
            text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
        } catch (Exception ignored) {}
        return new JSONObject(text);
    }

    public void setJsonByDomain(String domain) throws JSONException, IOException {
        String API_URl = "https://api.brandfetch.io/v2/brands/";
        this.setJsonObject(getJsonByUrl(API_URl + domain));
    }

    public HashMap<String, String> getCompanyName() {
        HashMap<String, String> result = new HashMap<>();
        try { result.put("companyName", jsonObject.getString("name")); } catch (JSONException ignored) {}
        return result;
    }

    public HashMap<String, String> getCompanyLink(String linkName) {
        HashMap<String, String> result = new HashMap<>();
        try {
            for (int i = 0; i < jsonObject.getJSONArray("links").length(); i++) {
                String link = jsonObject.getJSONArray("links").getJSONObject(i).getString("name");
                if (Objects.equals(link, linkName)) {
                    result.put(linkName, jsonObject.getJSONArray("links").getJSONObject(i).getString("url"));
                    return result;
                }
            }
        } catch (JSONException ignored) {}
        return result;
    }

    public HashMap<String, String> getCompanyImage(String imageType) {
        HashMap<String, String> result = new HashMap<>();
        try {
            for (int i = 0; i < jsonObject.getJSONArray("logos").length(); i++) {
                String type = jsonObject.getJSONArray("logos").getJSONObject(i).getString("type");
                if (Objects.equals(type, imageType)) {
                    result.put(imageType, jsonObject.getJSONArray("logos").getJSONObject(i).getJSONArray("formats").getJSONObject(0).getString("src"));
                    return result;
                }
            }
        } catch (JSONException ignored) {}

        return result;
    }


    @Override
    public HashMap<String, String> get(String domain) throws IOException, JSONException {
        this.setJsonByDomain(domain);
        return new HashMap<>() {{
            putAll(getCompanyName());
            putAll(getCompanyImage("logo"));
            putAll(getCompanyImage("icon"));
            putAll(getCompanyLink("twitter"));
            putAll(getCompanyLink("facebook"));
        }};
    }
}
