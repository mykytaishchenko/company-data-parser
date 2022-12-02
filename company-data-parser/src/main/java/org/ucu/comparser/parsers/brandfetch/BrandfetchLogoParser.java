package org.ucu.comparser.parsers.brandfetch;

import org.json.JSONException;
import org.ucu.comparser.parsers.Parser;

import java.io.IOException;
import java.util.*;

public class BrandfetchLogoParser implements Parser {
    @Override
    public HashMap<String, String> get(String url) throws IOException, JSONException {
        BrandfetchParser parser = new BrandfetchParser();
        parser.setJsonByDomain(url);
        return parser.getCompanyImage("logo");
    }
}
