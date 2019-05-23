package io.sqlman.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * SQL脚本工具类
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:56
 */
public final class Sqls {

    /**
     * 获取文件后缀名，如果文件没有后缀名则返回{@code null}
     *
     * @param filename 文件名称
     * @return 文件后缀名或{@code null}如果文件没有后缀名
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            throw new NullPointerException("filename must not be null");
        }
        int index = filename.lastIndexOf('.');
        return index < 0 ? null : filename.substring(index);
    }

    /**
     * 获取文件名，即不包括后缀的文件名，如果文件名没有后缀则原样返回
     *
     * @param filename 文件名称
     * @return 不包括后缀的文件名，如果文件名没有后缀则原样返回
     */
    public static String getName(String filename) {
        if (filename == null) {
            throw new NullPointerException("filename must not be null");
        }
        int index = filename.lastIndexOf('.');
        return index < 0 ? filename : filename.substring(0, index);
    }

    /**
     * 获取脚本文件版本号
     *
     * @param path 脚本文件路径
     * @return 脚本文件版本号
     */
    public static String getVersion(String path) {
        if (path == null) {
            throw new NullPointerException("path must not be null");
        }
        int index = path.lastIndexOf('/');
        String filename = index < 0 ? path : path.substring(index + 1);
        String name = Sqls.getName(filename);
        int idx = name.indexOf('-');
        return idx < 0 ? name : name.substring(0, idx);
    }

    /**
     * 获取脚本文件描述
     *
     * @param path 脚本文件路径
     * @return 脚本文件描述
     */
    public static String getDescription(String path) {
        if (path == null) {
            throw new NullPointerException("path must not be null");
        }
        int index = path.lastIndexOf('/');
        String filename = index < 0 ? path : path.substring(index + 1);
        String name = Sqls.getName(filename);
        int idx = name.indexOf('-');
        return idx < 0 ? "" : name.substring(idx + 1);
    }

    /**
     * 版本号对比
     *
     * @param aVer 版本a
     * @param bVer 版本b
     * @return 如果{@code a == b} 则返回 {@code 0}，如果{@code a < b} 则返回 {@code -1} 否则返回 {@code 1}
     */
    public static int compare(String aVer, String bVer) {
        if (aVer.startsWith("v") || aVer.startsWith("V")) {
            aVer = aVer.substring(1);
        }
        if (bVer.startsWith("v") || bVer.startsWith("V")) {
            bVer = bVer.substring(1);
        }
        String[] as = aVer.split("\\.");
        String[] bs = bVer.split("\\.");
        for (int i = 0; i < as.length && i < bs.length; i++) {
            int l = Integer.valueOf(as[i]);
            int r = Integer.valueOf(bs[i]);
            int comparision = Integer.compare(l, r);
            if (comparision != 0) {
                return comparision;
            }
        }
        return Integer.compare(as.length, bs.length);
    }

    /**
     * 字符串截断，如果字符串为{@code null} 则返回{@code ""}
     *
     * @param value  字符串
     * @param length 截断长度
     * @return 截断后的字符串
     */
    public static String truncate(String value, int length) {
        if (value == null) {
            return "";
        }
        if (value.length() < length) {
            return value;
        }
        return value.substring(0, length);
    }

    /**
     * 判断字符串为{@code null}或空字符串
     *
     * @param value 字符串
     * @return 如果字符串为{@code null}或空字符串则返回{@code true} 否则返回{@code false}
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String stringify(InputStream in) throws IOException {
        return stringify(in, Charset.defaultCharset().name());
    }

    public static String stringify(InputStream in, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        return out.toString(charset);
    }

}
