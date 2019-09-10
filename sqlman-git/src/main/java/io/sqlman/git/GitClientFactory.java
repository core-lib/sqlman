package io.sqlman.git;

import io.sqlman.vcs.VcsClient;
import io.sqlman.vcs.VcsClientFactory;

import java.io.IOException;

/**
 * Git客户端工厂
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 17:03
 */
public class GitClientFactory implements VcsClientFactory {
    private final GitConfig config;

    public GitClientFactory(GitConfig config) {
        this.config = config;
    }

    @Override
    public VcsClient produce() {
        return new GitClient(config);
    }

    @Override
    public void release(VcsClient vcsClient) {
        try {
            vcsClient.close();
        } catch (IOException e) {
            // ignored
        }
    }
}
