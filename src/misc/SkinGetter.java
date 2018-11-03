package misc;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import json.JSONArray;
import json.JSONException;
import json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class SkinGetter {

    public static final BufferedImage getSkinAsBufferedImage(Label insufficientName, ImageView skinImage, String uuid) {
        try {

            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();
            System.out.println("Sending request at " + url.toString() + "; RESPONSE CODE:" + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer buffer = new StringBuffer();
            while((inputLine = reader.readLine()) != null) {
                buffer.append(inputLine);
            }
            reader.close();

            JSONObject json = new JSONObject(buffer.toString());
            JSONArray props = json.getJSONArray("properties");
            JSONObject probsObj = new JSONObject(props.getJSONObject(0).toString());
            String value = probsObj.getString("value");

            byte[] valueBytes = Base64.getDecoder().decode(value);
            String valueDecoded = new String(valueBytes);

            JSONObject valueObject = new JSONObject(valueDecoded);
            JSONObject textures = new JSONObject(valueObject.getJSONObject("textures").toString());
            JSONObject skin = new JSONObject(textures.getJSONObject("SKIN").toString());
            String skinURL = skin.getString("url");

            BufferedImage image = ImageIO.read(new URL(skinURL));

            return image;
        } catch (IOException e) {
            insufficientName.setText("Wait until sending another request for that player!");
            skinImage.setImage(null);
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            insufficientName.setText("That player doesn't have a skin!");
            skinImage.setImage(null);
            e.printStackTrace();
            return null;
        }
    }

    public static final BufferedImage getSkinRender(String uuid) {
        try {
            BufferedImage skinRenderIMG = ImageIO.read(new URL("https://crafatar.com/renders/body/" + uuid));
            return skinRenderIMG;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
