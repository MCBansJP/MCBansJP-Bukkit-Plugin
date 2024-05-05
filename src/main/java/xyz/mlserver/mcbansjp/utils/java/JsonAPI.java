package xyz.mlserver.mcbansjp.utils.java;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonAPI {

    public static JsonNode getResult(String urlString) {
        StringBuilder result = new StringBuilder();
        JsonNode root = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.connect(); // URL接続
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String tmp = "";

            while ((tmp = in.readLine()) != null) {
                result.append(tmp);
            }

            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(result.toString());
            in.close();
            con.disconnect();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return root;
    }

}
