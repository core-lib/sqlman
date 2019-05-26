package io.sqlman.spring.script;

/**
 * 抽象的脚本资源提供器配置属性
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 9:10
 */
public abstract class AbstractProviderProperties {
    /**
     * SQL source provider implementation
     */
    private String provider = "classpath";

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
