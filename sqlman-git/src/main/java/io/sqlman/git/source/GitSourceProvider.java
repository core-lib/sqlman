package io.sqlman.git.source;

import io.sqlman.core.SqlNamingStrategy;
import io.sqlman.vcs.VcsClientFactory;
import io.sqlman.vcs.source.VcsSourceProvider;

/**
 * Git资源提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/9/11 16:19
 */
public class GitSourceProvider extends VcsSourceProvider {

    public GitSourceProvider() {
    }

    public GitSourceProvider(VcsClientFactory clientFactory) {
        super(clientFactory);
    }

    public GitSourceProvider(SqlNamingStrategy namingStrategy, VcsClientFactory clientFactory) {
        super(namingStrategy, clientFactory);
    }

}
