package io.sqlman.vcs.source;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.core.SqlNaming;
import io.sqlman.core.SqlSource;
import io.sqlman.core.exception.DuplicatedVersionException;
import io.sqlman.core.exception.MalformedNameException;
import io.sqlman.core.source.AbstractSourceProvider;
import io.sqlman.vcs.VcsClient;
import io.sqlman.vcs.VcsClientFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 版本控制系统资源提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/9/10 13:25
 */
public class VcsSourceProvider extends AbstractSourceProvider {
    protected VcsClientFactory clientFactory;
    protected File directory;
    protected String branch;
    protected VcsUpdateStrategy updateStrategy = VcsUpdateStrategy.CLEAN_TO_UPDATE;
    protected String scriptLocation = "**/*.sql";

    protected void update() throws IOException {
        VcsClient vcsClient = clientFactory.produce();
        try {
            updateStrategy.update(vcsClient, directory, branch);
        } finally {
            clientFactory.release(vcsClient);
        }
    }

    @Override
    public Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException {
        update();
        Enumeration<Resource> resources = Loaders.ant(Loaders.file(directory)).load(scriptLocation);
        Set<SqlSource> sources = new TreeSet<>(new Comparator<SqlSource>() {
            @Override
            public int compare(SqlSource o1, SqlSource o2) {
                return namingStrategy.compare(o1.version(), o2.version());
            }
        });
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            SqlNaming naming = namingStrategy.parse(name);
            SqlSource source = new VcsSource(naming.getName(), naming.getVersion(), naming.getParameters(), naming.getDescription(), resource.getUrl());
            if (!sources.add(source)) {
                throw new DuplicatedVersionException("duplicate SQL script version: " + source.version(), source.version());
            }
        }
        return Collections.enumeration(sources);
    }

    @Override
    public Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException {
        update();
        Enumeration<Resource> resources = Loaders.ant(Loaders.file(directory)).load(scriptLocation);
        Set<SqlSource> sources = new TreeSet<>(new Comparator<SqlSource>() {
            @Override
            public int compare(SqlSource o1, SqlSource o2) {
                return namingStrategy.compare(o1.version(), o2.version());
            }
        });
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            SqlNaming naming = namingStrategy.parse(name);
            int comparision = namingStrategy.compare(naming.getVersion(), version);
            SqlSource source = new VcsSource(naming.getName(), naming.getVersion(), naming.getParameters(), naming.getDescription(), resource.getUrl());
            boolean newer = comparision > 0 || (comparision == 0 && included);
            if (newer && !sources.add(source)) {
                throw new DuplicatedVersionException("duplicate SQL script version: " + source.version(), source.version());
            }
        }
        return Collections.enumeration(sources);
    }

    public VcsClientFactory getClientFactory() {
        return clientFactory;
    }

    public void setClientFactory(VcsClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

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

    public String getScriptLocation() {
        return scriptLocation;
    }

    public void setScriptLocation(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }
}
