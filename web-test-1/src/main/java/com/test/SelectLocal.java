package com.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectLocal {

    /**
     * 116.387778,39.7825
     * @param args
     */
    public static void main(String[] args) {
        getAdd("-34.6033","-58.3817");
    }

    private static String getAdd(String lat,String log) {
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
        String res = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();

        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        System.out.println(res);
        return res;
    }

}