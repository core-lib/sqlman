package io.sqlman.core.spring.script;

/**
 * 抽象的脚本资源解析器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:28
 */
public class AbstractResolverProperties {
    /**
     * SQL script resolver implementation
     */
    private String resolver = "druid";

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }
}
