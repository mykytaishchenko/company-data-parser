package org.ucu.comparser.parsers.pdl;

import org.json.JSONException;
import org.ucu.comparser.parsers.Parser;

import java.io.IOException;
import java.util.HashMap;

public class PdlTwitterParser implements Parser {
    @Override
    public HashMap<String, String> get(String url) throws IOException, JSONException {
        PdlParser parser = new PdlParser();
        parser.setJsonByDomain(url);
        return parser.getCompanyLink("twitter");
    }
}
