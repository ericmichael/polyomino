package com.asarg.polysim.utils;

import com.asarg.polysim.Main;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * Created by ericmartinez on 2/3/15.
 */
public class Version implements Comparable<Version> {

    private String version;

    public final String get() {
        return this.version;
    }

    public Version(String version) {
        if(version == null)
            throw new IllegalArgumentException("Version can not be null");
        if(!version.matches("[0-9]+(\\.[0-9]+)*"))
            throw new IllegalArgumentException("Invalid version format");
        this.version = version;
    }

    @Override public int compareTo(Version that) {
        if(that == null)
            return 1;
        String[] thisParts = this.get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for(int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if(thisPart < thatPart)
                return -1;
            if(thisPart > thatPart)
                return 1;
        }
        return 0;
    }

    @Override public boolean equals(Object that) {
        if(this == that)
            return true;
        if(that == null)
            return false;
        if(this.getClass() != that.getClass())
            return false;
        return this.compareTo((Version) that) == 0;
    }

    @Override
    public String toString() {
        return version;
    }

    public static Version getCurrentVersion() {
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

    public static Version getLatestInstalledVersion() {
        File versionsDir= getAppVersionsDir("VersaTile", true);
        String[] directories = versionsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        Version latest = null;
        for(String folder : directories){
            try {
                Version v = new Version(folder);
                if(v.compareTo(latest)>0){
                    latest = v;
                }
            }catch(IllegalArgumentException ignored){

            }
        }
        return latest;
    }

    public static File getLatestInstalled() {
        File versionsDir = getAppVersionsDir("VersaTile", true);
        String[] directories = versionsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        Version latest = null;
        for (String folder : directories) {
            try {
                Version v = new Version(folder);
                if (v.compareTo(latest) > 0) {
                    latest = v;
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
        if (latest != null) {
            File jar = new File(versionsDir + "/" + latest.toString() + "/versatile.jar");
            return jar;
        } else return null;
    }

    private static File getAppDataDir(String aName, boolean doCreate)
    {
        // Get user home + AppDataDir (platform specific) + name (if provided)
        String dir = System.getProperty("user.home");
        if(isWindows) dir += File.separator + "AppData" + File.separator + "Local";
        else if(isMac) dir += File.separator + "Library" + File.separator + "Application Support";
        if(aName!=null) dir += File.separator + aName;

        // Create file, actual directory (if requested) and return
        File dfile = new File(dir);
        if(doCreate && aName!=null) dfile.mkdirs();
        return dfile;
    }

    private static File getAppVersionsDir(String aName, boolean doCreate){
        File dataDir = getAppDataDir(aName, doCreate);
        String dir = dataDir.getAbsolutePath()+ "/versions";
        // Create file, actual directory (if requested) and return
        File dfile = new File(dir);
        if(doCreate && aName!=null) dfile.mkdirs();
        return dfile;
    }

    public static void startLatest(String[] args) throws Exception{
        //Create URLClassLoader for main jar file, get App class and invoke main
        File jar = getLatestInstalled();
        if(jar!=null) {
            final URLClassLoader child = new URLClassLoader(new URL[]{jar.toURI().toURL()});
            final Class classToLoad = Class.forName ("com.asarg.polysim.SimulationApplication", true, child);
            final Method method = classToLoad.getMethod("start", Stage.class);
            final Object instance = classToLoad.newInstance ();


            if (classToLoad == Object.class) child.close(); // Getting rid of warning message for ucl
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().setContextClassLoader(child);
                        method.invoke(instance, new Stage());
                    } catch (Throwable th) {
                        System.out.println(th.getMessage());
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    // Whether Windows/Mac
    static final boolean isWindows = (System.getProperty("os.name").contains("Windows"));
    static final boolean isMac = (System.getProperty("os.name").contains("Mac OS X"));
}