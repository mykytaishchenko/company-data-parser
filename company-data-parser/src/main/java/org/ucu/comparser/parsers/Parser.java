package org.ucu.comparser.parsers;

import com.google.maps.errors.ApiException;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

public interface Parser {
    public HashMap<String, String> get(String domain) throws IOException, JSONException, InterruptedException, ApiException;
}
