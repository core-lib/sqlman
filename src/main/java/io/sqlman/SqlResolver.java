package io.sqlman;

import java.util.Comparator;

/**
 * SQL脚本解析器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 10:38
 */
public interface SqlResolver extends Comparator<SqlResource> {

    /**
     * 对比源对象版本与指定版本的新旧
     *
     * @param resource 脚本资源
     * @param version  指定版本
     * @return 如果是源对象版本在指定版本之后则返回{@code 1}, 如果在指定版本之前则返回{@code -1}, 如果版本一致则返回@{code 0}
     */
    int compare(SqlResource resource, String version);

    /**
     * 解析SQL脚本
     *
     * @param resource 脚本资源
     * @param dbType   数据库类型
     * @return SQL脚本
     * @throws Exception 解析异常
     */
    SqlScript resolve(SqlResource resource, String dbType) throws Exception;

}
