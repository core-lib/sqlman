package io.sqlman;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 基础资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:57
 */
public class BasicResource implements SqlResource {
    private final URL url;

    public BasicResource(URL url) {
        this.url = url;
    }

    @Override
    public String name() {
        return url.getPath();
    }

    @Override
    public InputStream stream() throws IOException {
        return url.openStream();
    }
}
