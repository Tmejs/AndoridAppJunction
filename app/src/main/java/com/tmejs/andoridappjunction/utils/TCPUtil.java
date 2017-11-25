/*
 * AWS PROJECT (c) 2017 by tmejs (mateusz.rzad@gmail.com)
 * --------------------------------------------------------
 * Niniejszy program chroniony jest prawem autorskim. Jego rozpowszechnianie bez wyraźnej zgody autora
 * jest zabronione. Jakakolwiek ingerencja w oprogramowanie bez upoważnienia autora,
 * w tym w szczególności jego modyfikacja lub nieuprawnione kopiowanie jest sprzeczne z prawem.
 * Wersja opracowana dla Domax Sp. z o.o. z siedzibą w Łężycach
 */
package com.tmejs.andoridappjunction.utils;

import android.util.Pair;

import com.tmejs.andoridappjunction.AppParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Tmejs
 */
public class TCPUtil {


    private static String sendPostRequest(List<Pair<String, String>> request) throws IOException {

        String url = "http://" + AppParams.INCOMING_SERVLET_PATH;

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en");

        //Generating params based on incomed list
        String params = createParamsString(request);

        //System.out.println(urlParameters); 
        // Send post request
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        return response.toString();
    }


    private static String createParamsString(List<Pair<String, String>> params) {
        String paramsString = "";
        for (Pair<String, String> pair : params) {
            if (!paramsString.isEmpty()) {
                paramsString = paramsString.concat(AppParams.HTTP_PARAM_DELIMETER);
            } else {
                paramsString = paramsString.concat(pair.first);
                paramsString = paramsString.concat("=");
                paramsString = paramsString.concat(pair.second);
            }
        }
        return paramsString;
    }

    public static String sendRequest(List<Pair<String, String>> params) throws IOException {
        return sendPostRequest(params);
    }

}
