package org.ucu.comparser.parsers;

import org.ucu.comparser.parsers.brandfetch.*;
import org.ucu.comparser.parsers.google.*;
import org.ucu.comparser.parsers.pdl.*;

import java.util.*;

public class CompanyDataParser extends CombinedParser {
    public CompanyDataParser() {
        this.setParsers(Arrays.asList(
                new BrandfetchCompanyNameParser(),
                new PdlCompanyNameParser(),

                new BrandfetchFacebookParser(),
                new PdlFacebookParser(),

                new BrandfetchTwitterParser(),
                new PdlTwitterParser(),

                new BrandfetchIconParser(),

                new BrandfetchLogoParser(),

                new PdlEmployeesNumberParser(),

                new GoogleAddressParser()
        ));
    }
}
