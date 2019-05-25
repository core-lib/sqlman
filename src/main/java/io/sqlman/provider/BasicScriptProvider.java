package io.sqlman.provider;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.SqlResource;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

/**
 * 标准脚本资源提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:02
 */
public class BasicScriptProvider implements SqlScriptProvider {
    private ClassLoader classLoader;
    private String scriptLocation = "sqlman/**/*.sql";
    private SqlNamingStrategy namingStrategy = new BasicNamingStrategy();

    public BasicScriptProvider() {
    }

    public BasicScriptProvider(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    public BasicScriptProvider(String scriptLocation, SqlNamingStrategy namingStrategy) {
        this.scriptLocation = scriptLocation;
        this.namingStrategy = namingStrategy;
    }

    @Override
    public Enumeration<SqlResource> acquire() throws Exception {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> enumeration = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlResource> resources = new TreeSet<>(new BasicVersionComparator(namingStrategy));
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo info = namingStrategy.parse(name);
            SqlResource resource = new BasicResource(info.getName(), info.getVersion(), info.getDescription(), element.getUrl());
            if (!resources.add(resource)) {
                throw new IllegalStateException("duplicate SQL script version: " + resource.version());
            }
        }
        return Collections.enumeration(resources);
    }

    @Override
    public Enumeration<SqlResource> acquire(String version, boolean included) throws Exception {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> enumeration = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlResource> resources = new TreeSet<>(new BasicVersionComparator(namingStrategy));
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo info = namingStrategy.parse(name);
            int comparision = namingStrategy.compare(info.getVersion(), version);
            SqlResource resource = new BasicResource(info.getName(), info.getVersion(), info.getDescription(), element.getUrl());
            boolean newer = comparision > 0 || (comparision == 0 && included);
            if (newer && !resources.add(resource)) {
                throw new IllegalStateException("duplicate SQL script version: " + resource.version());
            }
        }
        return Collections.enumeration(resources);
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

    public SqlNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(SqlNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }
}
