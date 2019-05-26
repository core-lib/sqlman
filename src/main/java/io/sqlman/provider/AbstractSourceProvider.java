package io.sqlman.provider;

/**
 * 抽象的SQL脚本资源提供器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 20:57
 */
public abstract class AbstractSourceProvider implements SqlSourceProvider {
    protected SqlNamingStrategy namingStrategy = new BasicNamingStrategy();

    protected AbstractSourceProvider() {
    }

    protected AbstractSourceProvider(SqlNamingStrategy namingStrategy) {
        if (namingStrategy == null) {
            throw new IllegalArgumentException("namingStrategy must not be null");
        }
        this.namingStrategy = namingStrategy;
    }

    @Override
    public boolean check(String name) {
        return namingStrategy.check(name);
    }

    @Override
    public SqlInfo parse(String name) throws MalformedNameException {
        return namingStrategy.parse(name);
    }

    @Override
    public int compare(String o1, String o2) {
        return namingStrategy.compare(o1, o2);
    }

    public SqlNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(SqlNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }
}
