package io.sqlman.provider;

import io.sqlman.SqlResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;


/**
 * 标准脚本资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:57
 */
public class BasicResource implements SqlResource {
    private final String version;
    private final String description;
    private final URL url;

    public BasicResource(String version, String description, URL url) {
        this.version = version;
        this.description = description;
        this.url = url;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicResource that = (BasicResource) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return url.toString();
    }
}