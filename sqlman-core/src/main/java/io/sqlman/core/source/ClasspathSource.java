package io.sqlman.core.source;

import io.sqlman.core.SqlSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;


/**
 * 标准脚本资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:57
 */
public class ClasspathSource implements SqlSource {
    private final String name;
    private final String version;
    private final Set<String> parameters;
    private final String description;
    private final URL url;

    public ClasspathSource(String name, String version, Set<String> parameters, String description, URL url) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("description must not be null");
        }
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        this.name = name;
        this.version = version;
        this.parameters = parameters;
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
    public Set<String> parameters() {
        return parameters;
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
