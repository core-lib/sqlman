package io.sqlman;

import io.loadkit.Loaders;
import io.loadkit.Resource;

import java.util.Enumeration;

/**
 * 缺省的SQL脚本提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:02
 */
public class SimpleProvider implements SqlProvider {

    @Override
    public Enumeration<SqlResource> acquire(SqlConfig config) throws Exception {
        final String location = config.getLocation();
        final Enumeration<Resource> enumeration = Loaders.ant().load(location);
        return new Enumeration<SqlResource>() {
            @Override
            public boolean hasMoreElements() {
                return enumeration.hasMoreElements();
            }

            @Override
            public SqlResource nextElement() {
                Resource resource = enumeration.nextElement();
                return new BasicResource(resource.getUrl());
            }
        };
    }


}
