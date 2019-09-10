package io.sqlman.vcs.source;

import io.sqlman.vcs.VcsClient;

import java.io.File;

/**
 * 版本控制系统资源更新策略
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 16:24
 */
public enum VcsUpdateStrategy {

    /**
     * 清空再更新：即将本地库清空再克隆最新副本。
     */
    CLEAR_TO_UPDATE {
        @Override
        public void update(VcsClient vcsClient, File directory, String branch) {

        }
    },

    /**
     * 清理再更新：即将本地库的未版本追踪文件删除再更新。
     */
    CLEAN_TO_UPDATE {
        @Override
        public void update(VcsClient vcsClient, File directory, String branch) {

        }
    },

    /**
     * 永远不更新：即程序不自动更新，需要人工更新。
     */
    NEVER_DO_UPDATE {
        @Override
        public void update(VcsClient vcsClient, File directory, String branch) {

        }
    };

    /**
     * 更新本地库
     *
     * @param vcsClient VCS客户端
     */
    public abstract void update(VcsClient vcsClient, File directory, String branch);

}
