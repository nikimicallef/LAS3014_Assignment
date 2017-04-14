package com.uom.las3014.resources;

import java.util.Map;

public class Resources {
    public static String jsonMessageBuilder(final Map<String, String> jsonBody){
        final StringBuilder jsonOutputStringBuilder = new StringBuilder();
        jsonOutputStringBuilder.append("{\n");

        for(final String key : jsonBody.keySet()){
            final String keyValuePair = "\t\"" + key + "\" : \"" + jsonBody.get(key) + "\"\n";
            jsonOutputStringBuilder.append(keyValuePair);
        }

        jsonOutputStringBuilder.append("}");

        return jsonOutputStringBuilder.toString();
    }
}
