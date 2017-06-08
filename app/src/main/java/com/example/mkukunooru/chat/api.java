package com.example.mkukunooru.chat;

import android.content.Context;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class api {

    public static String startService(Context ctx)
    {
        String resourcePath="https://chat-4ab21.firebaseio.com/accounts.json?orderBy=%22username%22&equalTo=%22"+app.loginemail+"%22&print=pretty";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer("");
        int responseCode=0;
System.out.println("resource---"+resourcePath);
        try {
            URL url = new URL(resourcePath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.connect();
            responseCode = connection.getResponseCode();
            System.out.println("Response code from service "+responseCode);
            InputStream stream=connection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(stream));

            String line="";
            while ((line= reader.readLine())!=null)
            {
                buffer.append(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return buffer.toString();
    }


    public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

