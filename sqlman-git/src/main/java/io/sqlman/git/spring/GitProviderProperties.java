package io.sqlman.git.spring;

import io.sqlman.git.GitConfig;
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
    private GitConfig.Clone clone;
    private GitConfig.Checkout checkout;
    private GitConfig.Clean clean;
    private GitConfig.Pull pull;

    public GitConfig.Clone getClone() {
        return clone;
    }

    public void setClone(GitConfig.Clone clone) {
        this.clone = clone;
    }

    public GitConfig.Checkout getCheckout() {
        return checkout;
    }

    public void setCheckout(GitConfig.Checkout checkout) {
        this.checkout = checkout;
    }

    public GitConfig.Clean getClean() {
        return clean;
    }

    public void setClean(GitConfig.Clean clean) {
        this.clean = clean;
    }

    public GitConfig.Pull getPull() {
        return pull;
    }

    public void setPull(GitConfig.Pull pull) {
        this.pull = pull;
    }
}
