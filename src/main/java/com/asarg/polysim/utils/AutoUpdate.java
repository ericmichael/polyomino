package com.asarg.polysim.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by ericmartinez on 2/3/15.
 */
public class AutoUpdate {
    class Assets{
        String url;
        String id;
        int size;
        String browser_download_url;
    }
    class Release{
        String url;
        String assets_url;
        String upload_url;
        String html_url;
        int id;
        String tag_name;
        String created_at;
        String published_at;
        List<Assets> assets;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    public static String checkForUpdates(){
        try{
            Gson gson = new Gson();
            String json = readUrl("https://api.github.com/repos/ericmichael/polyomino/releases");
            Release[] releases = gson.fromJson(json, Release[].class);
            if(releases.length>0) {
                System.out.println(releases[0].tag_name);
            }
        }catch(Exception e){

        }
        return null;
    }
}
