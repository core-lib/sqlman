package io.sqlman.provider;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.SqlSource;

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
public class BasicSourceProvider extends AbstractSourceProvider implements SqlSourceProvider {
    private ClassLoader classLoader;
    private String scriptLocation = "sqlman/**/*.sql";

    public BasicSourceProvider() {
    }

    public BasicSourceProvider(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    public BasicSourceProvider(String scriptLocation, SqlNamingStrategy namingStrategy) {
        this.scriptLocation = scriptLocation;
        this.namingStrategy = namingStrategy;
    }

    @Override
    public Enumeration<SqlSource> acquire() throws Exception {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> enumeration = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlSource> resources = new TreeSet<>(new BasicVersionComparator(namingStrategy));
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo info = namingStrategy.parse(name);
            SqlSource resource = new BasicSource(info.getName(), info.getVersion(), info.getDescription(), element.getUrl());
            if (!resources.add(resource)) {
                throw new IllegalStateException("duplicate SQL script version: " + resource.version());
            }
        }
        return Collections.enumeration(resources);
    }

    @Override
    public Enumeration<SqlSource> acquire(String version, boolean included) throws Exception {
        ClassLoader resourceLoader = classLoader;
        if (resourceLoader == null) {
            resourceLoader = Thread.currentThread().getContextClassLoader();
        }
        if (resourceLoader == null) {
            resourceLoader = this.getClass().getClassLoader();
        }
        Enumeration<Resource> enumeration = Loaders.ant(resourceLoader).load(scriptLocation);
        Set<SqlSource> resources = new TreeSet<>(new BasicVersionComparator(namingStrategy));
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo info = namingStrategy.parse(name);
            int comparision = namingStrategy.compare(info.getVersion(), version);
            SqlSource resource = new BasicSource(info.getName(), info.getVersion(), info.getDescription(), element.getUrl());
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

}
