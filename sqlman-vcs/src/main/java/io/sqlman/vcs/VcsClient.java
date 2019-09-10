package io.sqlman.vcs;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * 版本控制系统客户端
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 11:37
 */
public interface VcsClient extends Closeable {

    /**
     * 初始化
     *
     * @param directory 根目录
     * @throws IOException I/O 异常
     */
    void init(File directory) throws IOException;

    /**
     * 检出
     *
     * @param branch 分支
     * @throws IOException I/O 异常
     */
    void checkout(String branch) throws IOException;

    /**
     * 清理，删除没有版本追踪的文件。
     *
     * @throws IOException I/O 异常
     */
    void clean() throws IOException;

    /**
     * 拉取更新
     *
     * @throws IOException I/O 异常
     */
    void pull() throws IOException;

}
