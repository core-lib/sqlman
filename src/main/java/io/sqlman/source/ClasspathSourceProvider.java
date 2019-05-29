package io.sqlman.source;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.SqlNaming;
import io.sqlman.SqlNamingStrategy;
import io.sqlman.SqlSource;
import io.sqlman.SqlSourceProvider;
import io.sqlman.exception.DuplicatedVersionException;
import io.sqlman.exception.MalformedNameException;

import java.io.IOException;
import java.util.*;

/**
 * 标准脚本资源提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:02
 */
public class ClasspathSourceProvider extends AbstractSourceProvider implements SqlSourceProvider {
    private ClassLoader classLoader;
    private String scriptLocation = "sqlman/**/*.sql";

    public ClasspathSourceProvider() {
        super();
    }

    public ClasspathSourceProvider(String scriptLocation) {
        this(null, scriptLocation);
    }

    public ClasspathSourceProvider(String scriptLocation, SqlNamingStrategy namingStrategy) {
        this(null, scriptLocation, namingStrategy);
    }

    public ClasspathSourceProvider(ClassLoader classLoader, String scriptLocation) {
        if (scriptLocation == null || scriptLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("scriptLocation must not be null or blank string");
        }
        this.classLoader = classLoader;
        this.scriptLocation = scriptLocation;
    }

    public ClasspathSourceProvider(ClassLoader classLoader, String scriptLocation, SqlNamingStrategy namingStrategy) {
        super(namingStrategy);
        if (scriptLocation == null || scriptLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("scriptLocation must not be null or blank string");
        }
        this.classLoader = classLoader;
        this.scriptLocation = scriptLocation;
    }

    @Override
    public Enumeration<SqlSource> acquire() throws MalformedNameException, DuplicatedVersionException, IOException {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> resources = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlSource> sources = new TreeSet<>(new Comparator<SqlSource>() {
            @Override
            public int compare(SqlSource o1, SqlSource o2) {
                return namingStrategy.compare(o1.version(), o2.version());
            }
        });
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            SqlNaming info = namingStrategy.parse(name);
            SqlSource source = new ClasspathSource(info.getName(), info.getVersion(), info.getDescription(), resource.getUrl());
            if (!sources.add(source)) {
                throw new DuplicatedVersionException("duplicate SQL script version: " + source.version(), source.version());
            }
        }
        return Collections.enumeration(sources);
    }

    @Override
    public Enumeration<SqlSource> acquire(String version, boolean included) throws MalformedNameException, DuplicatedVersionException, IOException {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> resources = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlSource> sources = new TreeSet<>(new Comparator<SqlSource>() {
            @Override
            public int compare(SqlSource o1, SqlSource o2) {
                return namingStrategy.compare(o1.version(), o2.version());
            }
        });
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            SqlNaming info = namingStrategy.parse(name);
            int comparision = namingStrategy.compare(info.getVersion(), version);
            SqlSource source = new ClasspathSource(info.getName(), info.getVersion(), info.getDescription(), resource.getUrl());
            boolean newer = comparision > 0 || (comparision == 0 && included);
            if (newer && !sources.add(source)) {
                throw new DuplicatedVersionException("duplicate SQL script version: " + source.version(), source.version());
            }
        }
        return Collections.enumeration(sources);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getScriptLocation() {
        return scriptLocation;
    }

    public void setScriptLocation(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

}
