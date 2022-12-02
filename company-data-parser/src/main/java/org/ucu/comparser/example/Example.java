package org.ucu.comparser.example;

import com.google.maps.errors.ApiException;
import org.json.JSONException;
import org.ucu.comparser.parsers.CompanyDataParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Example {
    private static void print(String url, HashMap<String, String> data) {
        System.out.println(url);
        for (String key: data.keySet()) System.out.println("\t" + key + ": " + data.get(key));
        System.out.println();
    }

    public static void main(String[] args) throws JSONException, IOException, InterruptedException, ApiException {
        List<String> demo = Arrays.asList(
                "ucu.edu.ua",
                "google.com",
                "facebook.com"
        );

        CompanyDataParser cdp = new CompanyDataParser();
        for (String url: demo) print(url, cdp.get(url));
    }
}
