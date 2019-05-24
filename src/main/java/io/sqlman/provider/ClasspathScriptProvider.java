package io.sqlman.provider;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.SqlResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 缺省的SQL脚本提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:02
 */
public class ClasspathScriptProvider implements SqlScriptProvider {
    private String scriptLocation = "sqlman/**/*.sql";
    private SqlNamingStrategy namingStrategy = new CommonNamingStrategy();

    public ClasspathScriptProvider() {
    }

    public ClasspathScriptProvider(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    public ClasspathScriptProvider(String scriptLocation, SqlNamingStrategy namingStrategy) {
        this.scriptLocation = scriptLocation;
        this.namingStrategy = namingStrategy;
    }

    @Override
    public Enumeration<SqlResource> acquire() throws Exception {
        Enumeration<Resource> enumeration = Loaders.ant().load(scriptLocation);
        List<SqlResource> resources = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo identity = namingStrategy.parse(name);
            SqlResource resource = new ClasspathResource(identity.getVersion(), identity.getDescription(), element.getUrl());
            resources.add(resource);
        }
        Collections.sort(resources, new SqlVersionComparator(namingStrategy));
        return Collections.enumeration(resources);
    }

    @Override
    public Enumeration<SqlResource> acquire(String version, boolean included) throws Exception {
        Enumeration<Resource> enumeration = Loaders.ant().load(scriptLocation);
        List<SqlResource> resources = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            Resource element = enumeration.nextElement();
            String name = element.getName();
            SqlInfo identity = namingStrategy.parse(name);
            int comparision = namingStrategy.compare(identity.getVersion(), version);
            if (comparision > 0 || (comparision == 0 && included)) {
                SqlResource resource = new ClasspathResource(identity.getVersion(), identity.getDescription(), element.getUrl());
                resources.add(resource);
            }
        }
        Collections.sort(resources, new SqlVersionComparator(namingStrategy));
        return Collections.enumeration(resources);
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
