package io.sqlman;

import java.io.IOException;
import java.io.InputStream;

/**
 * SQL脚本资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:28
 */
public interface SqlResource {

    /**
     * 资源名称
     *
     * @return 资源名称
     */
    String name();

    /**
     * 资源输入流
     *
     * @return 资源输入流
     * @throws IOException I/O异常
     */
    InputStream stream() throws IOException;

}
