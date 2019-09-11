package io.sqlman.vcs.spring;

import io.sqlman.core.spring.script.AbstractProviderProperties;
import io.sqlman.vcs.source.VcsUpdateStrategy;

import java.io.File;

/**
 * VCS资源提供器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/9/11 13:53
 */
public abstract class VcsProviderProperties extends AbstractProviderProperties {
    /**
     * VCS Local Directory
     */
    private File directory;
    /**
     * VCS Remote Branch
     */
    private String branch;
    /**
     * VCS Update Strategy
     */
    private VcsUpdateStrategy updateStrategy = VcsUpdateStrategy.CLEAN_TO_UPDATE;
    /**
     * SQL script location
     */
    private String location = "**/*.sql";

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public VcsUpdateStrategy getUpdateStrategy() {
        return updateStrategy;
    }

    public void setUpdateStrategy(VcsUpdateStrategy updateStrategy) {
        this.updateStrategy = updateStrategy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
