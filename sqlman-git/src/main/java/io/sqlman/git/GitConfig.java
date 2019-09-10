package io.sqlman.git;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.lib.BranchConfig;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.SubmoduleConfig;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Git配置
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 15:30
 */
public class GitConfig {
    final Clone clone;
    final Checkout checkout;
    final Clean clean;
    final Pull pull;

    public GitConfig(Clone clone, Checkout checkout, Clean clean, Pull pull) {
        this.clone = clone;
        this.checkout = checkout;
        this.clean = clean;
        this.pull = pull;
    }

    public static class Clone {
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

    public static class Checkout {
        boolean forceRefUpdate = false;
        boolean forced = false;
        boolean createBranch = false;
        boolean orphan = false;
        CreateBranchCommand.SetupUpstreamMode upstreamMode;
        String startPoint = null;
        CheckoutCommand.Stage checkoutStage = null;
        boolean checkoutAllPaths;

        public boolean isForceRefUpdate() {
            return forceRefUpdate;
        }

        public void setForceRefUpdate(boolean forceRefUpdate) {
            this.forceRefUpdate = forceRefUpdate;
        }

        public boolean isForced() {
            return forced;
        }

        public void setForced(boolean forced) {
            this.forced = forced;
        }

        public boolean isCreateBranch() {
            return createBranch;
        }

        public void setCreateBranch(boolean createBranch) {
            this.createBranch = createBranch;
        }

        public boolean isOrphan() {
            return orphan;
        }

        public void setOrphan(boolean orphan) {
            this.orphan = orphan;
        }

        public CreateBranchCommand.SetupUpstreamMode getUpstreamMode() {
            return upstreamMode;
        }

        public void setUpstreamMode(CreateBranchCommand.SetupUpstreamMode upstreamMode) {
            this.upstreamMode = upstreamMode;
        }

        public String getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(String startPoint) {
            this.startPoint = startPoint;
        }

        public CheckoutCommand.Stage getCheckoutStage() {
            return checkoutStage;
        }

        public void setCheckoutStage(CheckoutCommand.Stage checkoutStage) {
            this.checkoutStage = checkoutStage;
        }

        public boolean isCheckoutAllPaths() {
            return checkoutAllPaths;
        }

        public void setCheckoutAllPaths(boolean checkoutAllPaths) {
            this.checkoutAllPaths = checkoutAllPaths;
        }
    }

    public static class Clean {
        Set<String> paths = Collections.emptySet();
        boolean dryRun;
        boolean directories;
        boolean ignore = true;
        boolean force = false;

        public Set<String> getPaths() {
            return paths;
        }

        public void setPaths(Set<String> paths) {
            this.paths = paths;
        }

        public boolean isDryRun() {
            return dryRun;
        }

        public void setDryRun(boolean dryRun) {
            this.dryRun = dryRun;
        }

        public boolean isDirectories() {
            return directories;
        }

        public void setDirectories(boolean directories) {
            this.directories = directories;
        }

        public boolean isIgnore() {
            return ignore;
        }

        public void setIgnore(boolean ignore) {
            this.ignore = ignore;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }
    }

    public static class Pull {
        BranchConfig.BranchRebaseMode pullRebaseMode;
        String remote;
        String remoteBranchName;
        TagOpt tagOption;
        MergeCommand.FastForwardMode fastForwardMode;
        SubmoduleConfig.FetchRecurseSubmodulesMode submoduleRecurseMode;

        public BranchConfig.BranchRebaseMode getPullRebaseMode() {
            return pullRebaseMode;
        }

        public void setPullRebaseMode(BranchConfig.BranchRebaseMode pullRebaseMode) {
            this.pullRebaseMode = pullRebaseMode;
        }

        public String getRemote() {
            return remote;
        }

        public void setRemote(String remote) {
            this.remote = remote;
        }

        public String getRemoteBranchName() {
            return remoteBranchName;
        }

        public void setRemoteBranchName(String remoteBranchName) {
            this.remoteBranchName = remoteBranchName;
        }

        public TagOpt getTagOption() {
            return tagOption;
        }

        public void setTagOption(TagOpt tagOption) {
            this.tagOption = tagOption;
        }

        public MergeCommand.FastForwardMode getFastForwardMode() {
            return fastForwardMode;
        }

        public void setFastForwardMode(MergeCommand.FastForwardMode fastForwardMode) {
            this.fastForwardMode = fastForwardMode;
        }

        public SubmoduleConfig.FetchRecurseSubmodulesMode getSubmoduleRecurseMode() {
            return submoduleRecurseMode;
        }

        public void setSubmoduleRecurseMode(SubmoduleConfig.FetchRecurseSubmodulesMode submoduleRecurseMode) {
            this.submoduleRecurseMode = submoduleRecurseMode;
        }
    }

    public Clone getClone() {
        return clone;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public Clean getClean() {
        return clean;
    }

    public Pull getPull() {
        return pull;
    }
}
