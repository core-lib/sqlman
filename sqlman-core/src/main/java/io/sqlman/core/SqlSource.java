package io.sqlman.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * SQL脚本资源
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 17:28
 */
public interface SqlSource {

    /**
     * 脚本名称
     *
     * @return 脚本名称
     */
    String name();

    /**
     * 脚本版本号
     *
     * @return 脚本版本号
     */
    String version();

    /**
     * 参数列表
     *
     * @return 参数列表
     */
    Set<String> parameters();

    /**
     * 脚本描述
     *
     * @return 脚本描述
     */
    String description();

    /**
     * 脚本输入流
     *
     * @return 脚本输入流
     * @throws IOException I/O异常
     */
    InputStream open() throws IOException;

}
