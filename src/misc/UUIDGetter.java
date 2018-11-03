package misc;

import json.JSONException;
import json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UUIDGetter {

    private static String name;

    public static final String getUUID(String player) {

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player + "?at=" + System.currentTimeMillis()/1000);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();
            System.out.println("Sending request at " + url.toString() + "; RESPONSE CODE: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer jsonResponse = new StringBuffer();
            while((inputLine = reader.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            reader.close();

            JSONObject obj = new JSONObject(jsonResponse.toString());
            String uuid = obj.getString("id");

            name = obj.getString("name");

            return uuid;
        } catch (IOException | JSONException e) {
            return  null;
        }
    }

    public static final String getNameFromUUID(String uuid) {
        return name;
    }

}
