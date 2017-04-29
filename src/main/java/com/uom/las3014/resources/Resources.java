package com.uom.las3014.resources;

import java.util.Map;

public class Resources {
    public static String jsonMessageBuilder(final Map<String, String> jsonBody){
        final StringBuilder jsonOutputStringBuilder = new StringBuilder();
        jsonOutputStringBuilder.append("{");

        for(final String key : jsonBody.keySet()){
            final String keyValuePair = "\n\t\"" + key + "\" : \"" + jsonBody.get(key) + "\",";
            jsonOutputStringBuilder.append(keyValuePair);
        }

        jsonOutputStringBuilder.deleteCharAt(jsonOutputStringBuilder.lastIndexOf(","));

        jsonOutputStringBuilder.append("\n}");

        return jsonOutputStringBuilder.toString();
    }
}
