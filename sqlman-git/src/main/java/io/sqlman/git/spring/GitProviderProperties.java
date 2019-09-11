package io.sqlman.git.spring;

import io.sqlman.git.GitCheckoutConfig;
import io.sqlman.git.GitCleanConfig;
import io.sqlman.git.GitCloneConfig;
import io.sqlman.git.GitPullConfig;
import io.sqlman.vcs.spring.VcsProviderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Git资源提供器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/9/11 16:15
 */
@ConfigurationProperties(prefix = "sqlman.script")
public class GitProviderProperties extends VcsProviderProperties {
    private String uri;
    private String username;
    private String password;
    private int timeout;
    private GitCloneConfig clone;
    private GitCheckoutConfig checkout;
    private GitCleanConfig clean;
    private GitPullConfig pull;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public GitCloneConfig getClone() {
        return clone;
    }

    public void setClone(GitCloneConfig clone) {
        this.clone = clone;
    }

    public GitCheckoutConfig getCheckout() {
        return checkout;
    }

    public void setCheckout(GitCheckoutConfig checkout) {
        this.checkout = checkout;
    }

    public GitCleanConfig getClean() {
        return clean;
    }

    public void setClean(GitCleanConfig clean) {
        this.clean = clean;
    }

    public GitPullConfig getPull() {
        return pull;
    }

    public void setPull(GitPullConfig pull) {
        this.pull = pull;
    }
}
