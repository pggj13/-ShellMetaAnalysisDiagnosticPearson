package net.unesc.roma.ClienteWS;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.unesc.roma.ClienteWS.Dados.dataMetaD3;

public class ClienteWS {

    public static String toMetaD3(dataMetaD3 tData) {

        try {
            
            URL url = new URL("http://localhost:9000/api/draw_forest_plot/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            Gson lSerealizer = new Gson();
            String input = lSerealizer.toJson(tData);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String totOutPut = "";
            
            while ((output = br.readLine()) != null) {
                
                totOutPut = totOutPut + output;                
            }

            conn.disconnect();            
            return totOutPut;

        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return "";
    }
    
    public static String toMetaRoc(dataMetaD3 tData) {

        try {
            
            URL url = new URL("http://localhost:9000/api/draw_roc_plot/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            Gson lSerealizer = new Gson();
            String input = lSerealizer.toJson(tData);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String totOutPut = "";
            
            while ((output = br.readLine()) != null) {
                
                totOutPut = totOutPut + output;                
            }

            conn.disconnect();            
            return totOutPut;

        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return "";
    }
}
