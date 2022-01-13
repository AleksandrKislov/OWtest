package com.alexandr.owtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Handler {

    public String httpServiceCall(String requestUrl) {
        String result = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null)
                connection.disconnect();
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

