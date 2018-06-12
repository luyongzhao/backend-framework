package com.lyz.backend.framework.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * URL常用工具
 *
 * @author Leo
 */
public class URLUtil {
    public static URL url(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String url(String baseURL, String relativeURL) {
        return url(baseURL, relativeURL, false);
    }

    /**
     * 获取基于BaseURL的绝对地址
     *
     * @param baseURL
     * @param relativeURL
     * @return
     */
    public static String url(String baseURL, String relativeURL, boolean withQuery) {
        try {
            URL absoluteUrl = new URL(baseURL);
            URL parseUrl = new URL(absoluteUrl, relativeURL);
            return cleanURL(parseUrl, withQuery);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static URL URL(String baseURL, String relativeURL) {
        return URL(baseURL, relativeURL, false);
    }

    public static URL URL(String baseURL, String relativeURL, boolean withQuery) {
        try {
            return new URL(url(baseURL, relativeURL, withQuery));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String cleanUrl(String url) {
        try {
            return cleanURL(new URL(url), false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String cleanURL(URL url, boolean withQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(url.getProtocol());
        sb.append("://");
        sb.append(url.getHost());
        if (url.getPort() != -1) {
            sb.append(":").append(url.getPort());
        }

        String path = url.getPath();
        path = path.replaceAll("\\.\\.\\/", "").replaceAll("\\.\\/", "");
        sb.append(path);

        if (withQuery) {
            String query = url.getQuery();
            if (StringUtils.isNotBlank(query)) {
                sb.append("?").append(query);
            }
        }

        return sb.toString();
    }

    /**
     * URL编码
     *
     * @param url
     * @return
     */
    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * URL解码
     *
     * @param url
     * @return
     */
    public static String decode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getFileNameWithoutExt(String url) {
        try {
            URL u = new URL(url);
            String path = u.getPath();

            int idx = path.lastIndexOf('/');
            if (idx != -1) {
                path = path.substring(idx + 1);
            }

            idx = path.lastIndexOf('.');
            if (idx != -1) {
                path = path.substring(0, idx);
            }

            return path;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getExtForURL(String href) {
        String path = URLUtil.getFileName(href);
        int idx = path.lastIndexOf('.');
        if (idx != -1) {
            path = path.substring(idx + 1);
        } else {
            return null;
        }

        if (path.indexOf('/') == -1) {
            return null;
        } else {
            return path;
        }
    }

    public static String getFileName(String url) {
        try {
            URL u = new URL(url);
            String path = u.getPath();

            int idx = path.lastIndexOf('/');
            if (idx != -1) {
                path = path.substring(idx + 1);
            }

            return path;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(URLUtil.url("http://99diary.com", "./feed/a1.html#1", true));
        System.out.println(URLUtil.url("http://99diary.com", ".//ab/d/feed/a33.html#1", true));
        System.out.println(URLUtil.url("http://99diary.com", "./../e/feed/a.html?id=1#3", true));
        System.out.println(URLUtil.url("http://99diary.com/abc/d?id=1", "?id=1#3", true));
    }
}
