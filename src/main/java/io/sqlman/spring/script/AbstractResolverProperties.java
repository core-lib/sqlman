package io.sqlman.spring.script;

/**
 * 抽象的脚本资源解析器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:28
 */
public class AbstractResolverProperties {
    private String resolver = "basic";

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }
}
