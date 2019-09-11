package io.sqlman.vcs.source;

import io.sqlman.core.SqlUtils;
import io.sqlman.vcs.VcsClient;

import java.io.File;
import java.io.IOException;

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
        public void update(VcsClient client, File directory, String branch) throws IOException {
            SqlUtils.delete(directory, true);
            client.init(directory);
            client.checkout(branch);
            client.pull();
        }
    },

    /**
     * 清理再更新：即将本地库的未版本追踪文件删除再更新。
     */
    CLEAN_TO_UPDATE {
        @Override
        public void update(VcsClient client, File directory, String branch) throws IOException {
            client.init(directory);
            client.clean();
            client.checkout(branch);
            client.pull();
        }
    },

    /**
     * 永远不更新：即程序不自动更新，需要人工更新。
     */
    NEVER_DO_UPDATE {
        @Override
        public void update(VcsClient client, File directory, String branch) {
            // do nothing!!!
        }
    };

    /**
     * 更新本地库
     *
     * @param client VCS客户端
     */
    public abstract void update(VcsClient client, File directory, String branch) throws IOException;

}
