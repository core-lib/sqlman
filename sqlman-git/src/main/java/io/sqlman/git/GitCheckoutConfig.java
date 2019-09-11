package io.sqlman.git;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;

public class GitCheckoutConfig {
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