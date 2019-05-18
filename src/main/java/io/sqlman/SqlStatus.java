package io.sqlman;

/**
 * SQL状态
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:27
 */
public class SqlStatus {
    /**
     * SQL脚本版本
     */
    private final String version;
    /**
     * SQL脚本的语句下标
     */
    private final int index;
    /**
     * SQL版本的所有SQL语句是否以执行完成
     */
    private final boolean completed;

    public SqlStatus(String version, int index, boolean completed) {
        this.version = version;
        this.index = index;
        this.completed = completed;
    }

    public String getVersion() {
        return version;
    }

    public int getIndex() {
        return index;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return version + "/" + index + ":" + (completed ? "completed" : "uncompleted");
    }
}
