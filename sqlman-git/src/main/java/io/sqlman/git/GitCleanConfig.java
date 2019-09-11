package io.sqlman.git;

import java.util.Collections;
import java.util.Set;

public class GitCleanConfig {
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