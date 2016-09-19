package cn.sinobest.ypgj.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Author : lihaoquan
 * Description : 读取配置文件类
 */
public class ResourceUtil {

    /** classpath 前缀(用于url) */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /** URL prefix for loading from the file system: "file:" */
    public static final String FILE_URL_PREFIX = "file:";

    /** URL protocol for a file in the file system: "file" */
    public static final String URL_PROTOCOL_FILE = "file";

    /** URL protocol for an entry from a jar file: "jar" */
    public static final String URL_PROTOCOL_JAR = "jar";

    /** URL protocol for an entry from a zip file: "zip" */
    public static final String URL_PROTOCOL_ZIP = "zip";

    /** URL protocol for an entry from a WebSphere jar file: "wsjar" */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";

    /** URL protocol for an entry from an OC4J jar file: "code-source" */
    public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";

    /** Separator between JAR URL and file path within the JAR */
    public static final String JAR_URL_SEPARATOR = "!/";

    /**
     * 获得一个 ClassLoader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (cl == null) {
            // No thread context class loader, use class loader of this class.
            cl = ResourceUtil.class.getClassLoader();
        }
        return cl;
    }

    /**
     * 获得配置文件的 url
     *
     * @param configFileName
     * @return
     * @throws Exception
     */
    public static URL getUrl(String configFileName) throws Exception {
        String path = configFileName;
        if (configFileName.startsWith(CLASSPATH_URL_PREFIX)) {
            path = configFileName.substring(CLASSPATH_URL_PREFIX.length());
        }

        URL url = getClassLoader().getResource(path);
        if (url == null) {
            String description = "file " + path + " ";
            throw new FileNotFoundException(description + " cannot be found");
        }
        return url;
    }

    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol)
                || URL_PROTOCOL_ZIP.equals(protocol)
                || URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE
                .equals(protocol) && url.getPath().indexOf(JAR_URL_SEPARATOR) != -1));
    }

    public static URL extractJarFileURL(URL jarUrl)
            throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                // Probably no protocol in original jar URL, like
                // "jar:C:/mypath/myjar.jar".
                // This usually indicates that the jar file resides in the file
                // system.
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * 获得配置文件的 java.io.File
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static File getFile(String configFileName) throws Exception {
        URL url = getUrl(configFileName);
        // if (isJarURL(url)) {
        // url = extractJarFileURL(url);
        // }
        return new File(toURI(url).getSchemeSpecificPart());
    }

    /**
     * 获得配置文件的 java.io.InputStream
     *
     * @param configFileName
     * @return
     * @throws Exception
     */
    public static InputStream getFileAsStream(String configFileName)
            throws Exception {
        String path = configFileName;
        if (configFileName.startsWith(CLASSPATH_URL_PREFIX)) {
            path = configFileName.substring(CLASSPATH_URL_PREFIX.length());
        }
        URL url = getUrl(path);
        if (ResourceUtil.isJarURL(url)) {
            url = ResourceUtil.extractJarFileURL(url);
            JarFile currentJar = new JarFile(ResourceUtil.toURI(url)
                    .getSchemeSpecificPart());
            JarEntry dbEntry = currentJar.getJarEntry(path);
            return currentJar.getInputStream(dbEntry);
        }

        return new FileInputStream(new File(toURI(url).getSchemeSpecificPart()));
    }

    /**
     * 读取文件
     *
     * @param filename
     * @return
     * @throws java.io.IOException
     */
    public static String read(String filename) throws Exception {
        InputStreamReader read = new InputStreamReader(
                getFileAsStream(filename));
        BufferedReader reader = new BufferedReader(read);
        String line = "";
        StringBuffer readfile = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            readfile.append(line + "\r\n");
        }
        try {
            if (read != null)
                read.close();
            if (reader != null)
                reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readfile.toString().trim();
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return new URI(url.toString().replaceAll(" ", "%20"));
    }

    /**
     * 获得资源文件列表
     *
     * @param configFile
     *            - 1. 文件，支持通配符 2. 文件列表，以 "," 隔开
     * @return
     */
    public static File[] getResources(String configFiles) throws Exception {
        String[] configFileArray = configFiles.split(",");
        List<File> fileList = new ArrayList<File>(16);
        for (int i = 0; i < configFileArray.length; i++) {
            String configFile = configFileArray[i];
            if (configFile.indexOf("*") >= 0) {
                // 处理通配符
                String config = configFile;
                if (configFile.startsWith(CLASSPATH_URL_PREFIX)) {
                    config = configFile
                            .substring(CLASSPATH_URL_PREFIX.length());
                }
                int pos = config.lastIndexOf("/");
                String path = null;
                String fileName = null;
                File searchPath = null;

                if (pos >= 0) {
                    path = config.substring(0, pos);
                    searchPath = getFile(path);
                    fileName = config.substring(pos + 1);
                } else {
                    searchPath = getFile("./");
                    fileName = config;
                }
                String s = fileName.replace('.', '#');
                s = s.replaceAll("#", "\\\\.");
                s = s.replace('*', '#');
                s = s.replaceAll("#", ".*");
                s = s.replace('?', '#');
                s = s.replaceAll("#", ".?");
                s = "^" + s + "$";
                final Pattern pattern = Pattern.compile(s);

                File[] files = searchPath.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {

                        return pattern.matcher(name).find();
                    }

                });
                if (files != null) {
                    for (File f : files) {
                        fileList.add(f);
                    }
                }
            } else {
                fileList.add(getFile(configFile));
            }
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    public static void main(String[] args) throws Exception {
        String configFile = "classpath:spring/app*.xml,classpath:spring/action*.xml";
        File[] files = getResources(configFile);
        System.out.println("files: " + (files == null ? "null" : files.length));
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
        }
    }
}
