package ru.geekbrains.android.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtil {
    private String request = "";



    public String getHttpsRequest (String url) {

        HttpsURLConnection urlConnection = null;
        try {
            final URL uri = new URL(url);
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток

            request = getLines(in);


        } catch (Exception e) {
            Log.i("myLogs", "request",e);
            e.printStackTrace();
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return request;
    }



    private String getLines(BufferedReader in) throws IOException {

        StringBuilder buf=new StringBuilder();
        String line=null;
        while ((line=in.readLine()) != null) {
            buf.append(line + "\n");
        }
        return(buf.toString());
    }

}
