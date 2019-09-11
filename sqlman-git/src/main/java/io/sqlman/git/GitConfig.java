package io.sqlman.git;

import org.eclipse.jgit.transport.CredentialsProvider;

/**
 * Git配置
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 15:30
 */
public class GitConfig {
    final String uri;
    final CredentialsProvider credentialsProvider;
    final int timeout;
    final GitCloneConfig clone;
    final GitCheckoutConfig checkout;
    final GitCleanConfig clean;
    final GitPullConfig pull;

    public GitConfig(String uri, CredentialsProvider credentialsProvider, int timeout, GitCloneConfig clone, GitCheckoutConfig checkout, GitCleanConfig clean, GitPullConfig pull) {
        this.uri = uri;
        this.credentialsProvider = credentialsProvider;
        this.timeout = timeout;
        this.clone = clone;
        this.checkout = checkout;
        this.clean = clean;
        this.pull = pull;
    }

    public String getUri() {
        return uri;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public int getTimeout() {
        return timeout;
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
