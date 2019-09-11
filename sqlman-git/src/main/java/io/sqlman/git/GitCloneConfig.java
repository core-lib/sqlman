package io.sqlman.git;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.util.Collection;

public class GitCloneConfig {
    CredentialsProvider credentialsProvider;
    int timeout;
    String uri;
    File gitDir;
    boolean bare;
    FS fs;
    String remote = Constants.DEFAULT_REMOTE_NAME;
    String branch = Constants.HEAD;
    boolean cloneAllBranches;
    boolean cloneSubmodules;
    boolean noCheckout;
    Collection<String> branchesToClone;

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getGitDir() {
        return gitDir;
    }

    public void setGitDir(File gitDir) {
        this.gitDir = gitDir;
    }

    public boolean isBare() {
        return bare;
    }

    public void setBare(boolean bare) {
        this.bare = bare;
    }

    public FS getFs() {
        return fs;
    }

    public void setFs(FS fs) {
        this.fs = fs;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public boolean isCloneAllBranches() {
        return cloneAllBranches;
    }

    public void setCloneAllBranches(boolean cloneAllBranches) {
        this.cloneAllBranches = cloneAllBranches;
    }

    public boolean isCloneSubmodules() {
        return cloneSubmodules;
    }

    public void setCloneSubmodules(boolean cloneSubmodules) {
        this.cloneSubmodules = cloneSubmodules;
    }

    public boolean isNoCheckout() {
        return noCheckout;
    }

    public void setNoCheckout(boolean noCheckout) {
        this.noCheckout = noCheckout;
    }

    public Collection<String> getBranchesToClone() {
        return branchesToClone;
    }

    public void setBranchesToClone(Collection<String> branchesToClone) {
        this.branchesToClone = branchesToClone;
    }
}