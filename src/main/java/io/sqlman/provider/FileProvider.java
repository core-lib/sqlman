package io.sqlman.provider;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.SqlProvider;
import io.sqlman.SqlResolver;
import io.sqlman.SqlScript;
import io.sqlman.resolver.URLResolver;
import io.sqlman.utils.Laziness;
import io.sqlman.utils.Nullable;
import io.sqlman.utils.Supplier;

import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

/**
 * 缺省的SQL脚本提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:02
 */
public class FileProvider implements SqlProvider, Supplier<Enumeration<SqlScript>, URL> {
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private String location = "sqlman";
    private boolean recursively = false;
    private SqlResolver<URL> resolver = new URLResolver();

    @Override
    public Enumeration<SqlScript> acquire() throws Exception {
        Set<URL> resources = new TreeSet<>(resolver);
        Enumeration<Resource> enumeration = Loaders.std(classLoader).load(location, recursively);
        while (enumeration.hasMoreElements()) {
            Resource resource = enumeration.nextElement();
            URL url = resource.getUrl();
            if (resolver.validate(url) && !resources.add(url)) {
                throw new IllegalStateException("duplicate sql script version of " + resource.getName());
            }
        }
        return Laziness.forMultiple(Collections.enumeration(resources), this);
    }

    @Override
    public Enumeration<SqlScript> acquire(String version) throws Exception {
        Set<URL> resources = new TreeSet<>(resolver);
        Enumeration<Resource> enumeration = Loaders.std(classLoader).load(location, recursively);
        while (enumeration.hasMoreElements()) {
            Resource resource = enumeration.nextElement();
            URL url = resource.getUrl();
            if (resolver.validate(url) && resolver.contrast(url, version) >= 0 && !resources.add(url)) {
                throw new IllegalStateException("duplicate sql script version of " + resource.getName());
            }
        }
        return Laziness.forMultiple(Collections.enumeration(resources), this);
    }

    @Override
    public Nullable<Enumeration<SqlScript>> supply(URL param) throws Exception {
        return Nullable.ofNullable(resolver.resolve(param));
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isRecursively() {
        return recursively;
    }

    public void setRecursively(boolean recursively) {
        this.recursively = recursively;
    }

    public SqlResolver<URL> getResolver() {
        return resolver;
    }

    public void setResolver(SqlResolver<URL> resolver) {
        this.resolver = resolver;
    }

}
