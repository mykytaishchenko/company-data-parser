package org.ucu.comparser.parsers.google;

import com.google.maps.GeoApiContext;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResponse;
import lombok.Setter;
import org.json.JSONException;

import org.ucu.comparser.config.Config;
import org.ucu.comparser.parsers.Parser;
import org.ucu.comparser.parsers.brandfetch.BrandfetchCompanyNameParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GoogleAddressParser implements Parser {
    @Setter
    private String API_KEY = new Config().GOOGLE_MAP_API_KEY;

    @Override
    public HashMap<String, String> get(String domain) throws IOException, JSONException, InterruptedException, ApiException {
        HashMap<String, String> result = new HashMap<>();

        try {
            BrandfetchCompanyNameParser parser = new BrandfetchCompanyNameParser();
            String companyName = parser.get(domain).getOrDefault("companyName", null);
            if (Objects.nonNull(companyName)) companyName = domain;

            List<String> queries = Arrays.asList(domain, companyName + " headquarters", companyName + "company office");

            for (String query : queries) {
                GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
                PlacesSearchResponse placesRespose = new TextSearchRequest(context).query(query).await();
                if (placesRespose.results.length > 0) {
                    String address = placesRespose.results[0].formattedAddress;
                    result.put("address", address);
                    break;
                }
            }
        } catch (Exception ignored) {}
        return result;
    }
}
