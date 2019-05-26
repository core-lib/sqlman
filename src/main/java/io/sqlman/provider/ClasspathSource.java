package io.sqlman.provider;

import io.sqlman.SqlSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * 标准脚本资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:57
 */
public class ClasspathSource implements SqlSource {
    private final String name;
    private final String version;
    private final String description;
    private final URL url;

    public ClasspathSource(String name, String version, String description, URL url) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        this.name = name;
        this.version = version;
        this.description = description;
        this.url = url;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public InputStream open() throws IOException {
        return url.openStream();
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
