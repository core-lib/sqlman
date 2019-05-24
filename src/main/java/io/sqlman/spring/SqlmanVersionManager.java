package io.sqlman.spring;

import io.sqlman.manager.SqlVersionManager;
import io.sqlman.manager.StandardVersionManager;

/**
 * Sqlman升级器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/24 16:45
 */
public class SqlmanVersionManager extends StandardVersionManager implements SqlVersionManager {

    @Override
    public void upgrade() throws Exception {
        super.upgrade();
    }

}
