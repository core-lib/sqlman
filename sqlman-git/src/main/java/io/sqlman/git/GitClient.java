package io.sqlman.git;

import io.sqlman.vcs.VcsClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

/**
 * Git客户端
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 13:53
 */
public class GitClient implements VcsClient {
    private final GitConfig config;
    private Git git;

    public GitClient(GitConfig config) {
        this.config = config;
    }

    @Override
    public void init(File directory) throws IOException {
        try {
            git = Git.cloneRepository()
                    .setCredentialsProvider(config.clone.credentialsProvider)
                    .setTimeout(config.clone.timeout)
                    .setURI(config.clone.uri)
                    .setGitDir(config.clone.gitDir)
                    .setBare(config.clone.bare)
                    .setFs(config.clone.fs)
                    .setRemote(config.clone.remote)
                    .setBranch(config.clone.branch)
                    .setCloneAllBranches(config.clone.cloneAllBranches)
                    .setCloneSubmodules(config.clone.cloneSubmodules)
                    .setNoCheckout(config.clone.noCheckout)
                    .setBranchesToClone(config.clone.branchesToClone)
                    .call();
        } catch (Exception ex) {
            git = Git.open(directory);
        }
    }

    @Override
    public void checkout(String branch) throws IOException {
        try {
            git.checkout()
                    .setName(branch)
                    .setForceRefUpdate(config.checkout.forceRefUpdate)
                    .setForced(config.checkout.forced)
                    .setCreateBranch(config.checkout.createBranch)
                    .setOrphan(config.checkout.orphan)
                    .setUpstreamMode(config.checkout.upstreamMode)
                    .setStartPoint(config.checkout.startPoint)
                    .setStage(config.checkout.checkoutStage)
                    .setAllPaths(config.checkout.checkoutAllPaths)
                    .call();
        } catch (GitAPIException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void clean() throws IOException {
        try {
            git.clean()
                    .setPaths(config.clean.paths)
                    .setDryRun(config.clean.dryRun)
                    .setCleanDirectories(config.clean.directories)
                    .setIgnore(config.clean.ignore)
                    .setForce(config.clean.force)
                    .call();
        } catch (GitAPIException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void pull() throws IOException {
        try {
            PullResult result = git.pull()
                    .setRebase(config.pull.pullRebaseMode)
                    .setRemote(config.pull.remote)
                    .setRemoteBranchName(config.pull.remoteBranchName)
                    .setTagOpt(config.pull.tagOption)
                    .setFastForward(config.pull.fastForwardMode)
                    .setRecurseSubmodules(config.pull.submoduleRecurseMode)
                    .call();
            if (!result.isSuccessful()) {
                throw new IOException(result.toString());
            }
        } catch (GitAPIException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() {
        if (git != null) {
            git.close();
        }
    }
}
