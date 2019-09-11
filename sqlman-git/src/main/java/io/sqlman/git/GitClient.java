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
                    .setDirectory(directory)
                    .setCredentialsProvider(config.credentialsProvider)
                    .setTimeout(config.timeout)
                    .setURI(config.uri)
                    .setGitDir(config.clone == null ? null : config.clone.gitDir)
                    .setBare(config.clone != null && config.clone.bare)
                    .setFs(config.clone == null ? null : config.clone.fs)
                    .setRemote(config.clone == null ? null : config.clone.remote)
                    .setBranch(config.clone == null ? null : config.clone.branch)
                    .setCloneAllBranches(config.clone != null && config.clone.cloneAllBranches)
                    .setCloneSubmodules(config.clone != null && config.clone.cloneSubmodules)
                    .setNoCheckout(config.clone != null && config.clone.noCheckout)
                    .setBranchesToClone(config.clone == null ? null : config.clone.branchesToClone)
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
                    .setForceRefUpdate(config.checkout != null && config.checkout.forceRefUpdate)
                    .setForced(config.checkout != null && config.checkout.forced)
                    .setCreateBranch(config.checkout != null && config.checkout.createBranch)
                    .setOrphan(config.checkout != null && config.checkout.orphan)
                    .setUpstreamMode(config.checkout == null ? null : config.checkout.upstreamMode)
                    .setStartPoint(config.checkout == null ? null : config.checkout.startPoint)
                    .setStage(config.checkout == null ? null : config.checkout.checkoutStage)
                    .setAllPaths(config.checkout != null && config.checkout.checkoutAllPaths)
                    .call();
        } catch (GitAPIException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void clean() throws IOException {
        try {
            git.clean()
                    .setPaths(config.clean == null ? null : config.clean.paths)
                    .setDryRun(config.clean != null && config.clean.dryRun)
                    .setCleanDirectories(config.clean != null && config.clean.directories)
                    .setIgnore(config.clean == null || config.clean.ignore)
                    .setForce(config.clean != null && config.clean.force)
                    .call();
        } catch (GitAPIException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void pull() throws IOException {
        try {
            PullResult result = git.pull()
                    .setCredentialsProvider(config.credentialsProvider)
                    .setTimeout(config.timeout)
                    .setRebase(config.pull == null ? null : config.pull.pullRebaseMode)
                    .setRemote(config.pull == null ? null : config.pull.remote)
                    .setRemoteBranchName(config.pull == null ? null : config.pull.remoteBranchName)
                    .setTagOpt(config.pull == null ? null : config.pull.tagOption)
                    .setFastForward(config.pull == null ? null : config.pull.fastForwardMode)
                    .setRecurseSubmodules(config.pull == null ? null : config.pull.submoduleRecurseMode)
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
