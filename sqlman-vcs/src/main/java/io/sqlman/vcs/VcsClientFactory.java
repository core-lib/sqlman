package io.sqlman.vcs;

/**
 * 版本管理系统客户端工厂
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 13:42
 */
public interface VcsClientFactory {

    /**
     * 生产
     *
     * @return VCS客户端
     */
    VcsClient produce();

    /**
     * 回收
     *
     * @param vcsClient VCS客户端
     */
    void release(VcsClient vcsClient);

}
