package io.sqlman.git;

import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.lib.BranchConfig;
import org.eclipse.jgit.lib.SubmoduleConfig;
import org.eclipse.jgit.transport.TagOpt;

public class GitPullConfig {
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