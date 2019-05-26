package io.sqlman.provider;

/**
 * 标准命名策略
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 22:16
 */
public class BasicNamingStrategy implements SqlNamingStrategy {
    private char separator = '/';
    private String delimiter = "-";
    private String extension = ".sql";

    public BasicNamingStrategy() {
    }

    public BasicNamingStrategy(char separator, String delimiter, String extension) {
        if (delimiter == null || delimiter.trim().isEmpty()) {
            throw new IllegalArgumentException("delimiter must not be null or blank string");
        }
        if (extension == null || extension.trim().isEmpty()) {
            throw new IllegalArgumentException("extension must not be null or blank string");
        }
        this.separator = separator;
        this.delimiter = delimiter;
        this.extension = extension;
    }

    @Override
    public boolean check(String name) {
        if (name == null) {
            return false;
        }
        if (!name.endsWith(extension)) {
            return false;
        }

        // 去掉后缀
        name = name.substring(0, name.length() - extension.length());

        // 取最后一截
        name = name.substring(name.lastIndexOf(separator) + 1);

        // 分隔符前面的是版本号后面是描述，描述可以没有
        int index = name.indexOf(delimiter);
        String version = index < 0 ? name : name.substring(0, index);

        return version.matches("v?\\d+(\\.\\d+)*");
    }

    @Override
    public SqlInfo parse(String name) throws MalformedNameException {
        if (!check(name)) {
            throw new MalformedNameException("invalid name ： " + name, name);
        }
        // 去掉后缀
        String n = name.substring(0, name.length() - extension.length());

        // 取最后一截
        n = n.substring(n.lastIndexOf(separator) + 1);

        // 分隔符前面的是版本号后面是描述，描述可以没有
        int index = n.indexOf(delimiter);
        String version = index < 0 ? n : n.substring(0, index);
        String description = index < 0 ? "" : n.substring(index + 1);

        return new SqlInfo(name, version, description);
    }

    /**
     * 版本号对比
     *
     * @param aVer 版本a
     * @param bVer 版本b
     * @return 如果{@code a == b} 则返回 {@code 0}，如果{@code a < b} 则返回 {@code -1} 否则返回 {@code 1}
     */
    @Override
    public int compare(String aVer, String bVer) {
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

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
