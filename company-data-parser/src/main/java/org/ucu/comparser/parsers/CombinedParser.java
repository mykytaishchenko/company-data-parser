package org.ucu.comparser.parsers;

import com.google.maps.errors.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class CombinedParser implements Parser {
    @Setter
    @Getter
    private List<Parser> parsers = new ArrayList<>();

    private HashMap<String, String> mergeParsers(String url) throws JSONException, IOException, InterruptedException, ApiException {
        HashMap<String, String> result = new HashMap<>();
        for (Parser parser : parsers) {
            HashMap<String, String> response = parser.get(url);
            if (Objects.isNull(response) || response.isEmpty()) continue;
            for (String key : response.keySet()) {
                if (!result.containsKey(key) || result.get(key).equals("null")) result.put(key, response.get(key));
            }
        }
        return result;
    }

    @Override
    public HashMap<String, String> get(String url) throws IOException, JSONException, InterruptedException, ApiException {
        return mergeParsers(url);
    }
}
