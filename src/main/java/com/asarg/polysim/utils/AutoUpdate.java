package com.asarg.polysim.utils;

import com.asarg.polysim.Main;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Properties;

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


    public static Version getVersion() {
        try {
            FileReader reader = new FileReader(Main.class.getResource("/project.properties").getFile());
            Properties properties = new Properties();
            properties.load(reader);
            String version = properties.getProperty("MAVEN_PROJECT_VERSION");
            Version myVersion = new Version(version);
            return myVersion;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                Version version = getVersion();
                Version remoteVersion = new Version(releases[0].tag_name);
                if(version.compareTo(remoteVersion)==-1){
                    System.out.println("Time to update");
                }else{
                    System.out.println("up-to-date");
                }
            }
        }catch(Exception e){

        }
        return null;
    }
}
