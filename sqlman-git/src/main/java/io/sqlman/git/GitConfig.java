package io.sqlman.git;

/**
 * Git配置
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 15:30
 */
public class GitConfig {
    final GitCloneConfig clone;
    final GitCheckoutConfig checkout;
    final GitCleanConfig clean;
    final GitPullConfig pull;

    public GitConfig(GitCloneConfig clone, GitCheckoutConfig checkout, GitCleanConfig clean, GitPullConfig pull) {
        this.clone = clone;
        this.checkout = checkout;
        this.clean = clean;
        this.pull = pull;
    }

    public GitCloneConfig getClone() {
        return clone;
    }

    public GitCheckoutConfig getCheckout() {
        return checkout;
    }

    public GitCleanConfig getClean() {
        return clean;
    }

    public GitPullConfig getPull() {
        return pull;
    }
}
