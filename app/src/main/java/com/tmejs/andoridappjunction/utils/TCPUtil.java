/*
 * AWS PROJECT (c) 2017 by tmejs (mateusz.rzad@gmail.com)
 * --------------------------------------------------------
 * Niniejszy program chroniony jest prawem autorskim. Jego rozpowszechnianie bez wyraźnej zgody autora
 * jest zabronione. Jakakolwiek ingerencja w oprogramowanie bez upoważnienia autora,
 * w tym w szczególności jego modyfikacja lub nieuprawnione kopiowanie jest sprzeczne z prawem.
 * Wersja opracowana dla Domax Sp. z o.o. z siedzibą w Łężycach
 */
package com.tmejs.andoridappjunction.utils;

import android.util.Log;
import android.util.Pair;

import com.tmejs.andoridappjunction.AppParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Tmejs
 */
public class TCPUtil {


    private static String sendPostRequest(String json) throws IOException {

        String url = "http://" +AppParams.WEB_SERWER_ADDRESS + AppParams.INCOMING_SERVLET_PATH;
        Log.e("dasdasdasd",url);
        URL obj = new URL(url);


        HttpURLConnection con = (HttpURLConnection) obj.openConnection();



        String line;
        StringBuffer jsonString = new StringBuffer();

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setInstanceFollowRedirects(false);
        con.connect();
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        writer.write(json);

        writer.close();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        con.disconnect();
        return jsonString.toString();

    }
//
//
//    private static String createParamsString(List<Pair<String, String>> params) {
//        String paramsString = "";
//        for (Pair<String, String> pair : params) {
//            if (!paramsString.isEmpty()) {
//                paramsString = paramsString.concat(AppParams.HTTP_PARAM_DELIMETER);
//            } else {
//                paramsString = paramsString.concat(pair.first);
//                paramsString = paramsString.concat("=");
//                paramsString = paramsString.concat(pair.second);
//            }
//        }
//        return paramsString;
//    }

    public static String sendRequest(String json) throws IOException {
        Log.e(TCPUtil.class.toString(),"sendRequest()");
        return sendPostRequest(json);
    }

}
