package io.sqlman;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import io.sqlman.utils.Enumerations;
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
public class SimpleProvider implements SqlProvider {
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private SqlResolver<URL> resolver = new SimpleResolver();
    private String location = "sqlman/**/*.sql";

    @Override
    public Enumeration<SqlScript> acquire(String dbType) throws Exception {
        Set<URL> resources = new TreeSet<>(resolver);
        Enumeration<Resource> enumeration = Loaders.ant(classLoader).load(location);
        while (enumeration.hasMoreElements()) {
            Resource resource = enumeration.nextElement();
            URL url = resource.getUrl();
            if (!resources.add(url)) {
                throw new IllegalStateException("duplicate sql script version of " + resource.getName());
            }
        }
        return Enumerations.create(Collections.enumeration(resources), new SimpleSupplier(resolver, dbType));
    }

    @Override
    public Enumeration<SqlScript> acquire(String dbType, String version) throws Exception {
        Set<URL> resources = new TreeSet<>(resolver);
        Enumeration<Resource> enumeration = Loaders.ant(classLoader).load(location);
        while (enumeration.hasMoreElements()) {
            Resource resource = enumeration.nextElement();
            URL url = resource.getUrl();
            if (resolver.contrast(url, version) >= 0 && !resources.add(url)) {
                throw new IllegalStateException("duplicate sql script version of " + resource.getName());
            }
        }
        return Enumerations.create(Collections.enumeration(resources), new SimpleSupplier(resolver, dbType));
    }

    private static class SimpleSupplier implements Supplier<Enumeration<SqlScript>, URL> {
        private final SqlResolver<URL> resolver;
        private final String dbType;

        SimpleSupplier(SqlResolver<URL> resolver, String dbType) {
            this.resolver = resolver;
            this.dbType = dbType;
        }

        @Override
        public Nullable<Enumeration<SqlScript>> supply(URL param) throws Exception {
            return Nullable.ofNullable(resolver.resolve(param, dbType));
        }
    }


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public SqlResolver<URL> getResolver() {
        return resolver;
    }

    public void setResolver(SqlResolver<URL> resolver) {
        this.resolver = resolver;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
