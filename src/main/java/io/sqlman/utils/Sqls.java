package io.sqlman.utils;

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
        if (filename.startsWith("v") || filename.startsWith("V")) {
            filename = filename.substring(1);
        }
        String name = Sqls.getName(filename);
        int idx = name.indexOf('-');
        return idx < 0 ? name : name.substring(0, idx);
    }

}
