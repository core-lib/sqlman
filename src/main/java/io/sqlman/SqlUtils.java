package io.sqlman;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * SQL脚本工具类
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:56
 */
public class SqlUtils {

    private SqlUtils() {
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
     * 将输入流内容转换成字符串
     *
     * @param in      输入流
     * @param charset 字符集
     * @return 输入流内容
     * @throws IOException I/O异常
     */
    public static String stringify(InputStream in, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        return out.toString(charset);
    }

    public static String ifEmpty(String value, String defaultValue) {
        return value == null || value.isEmpty() ? defaultValue : value;
    }

}
