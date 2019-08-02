package io.sqlman.version;

import java.util.Set;

/**
 * 模式
 *
 * @author Payne 646742615@qq.com
 * 2019/8/2 15:07
 */
public enum JdbcMode {

    /**
     * 安全模式
     */
    SAFETY,
    /**
     * 危险模式
     */
    DANGER;

    /**
     * 从指令集中获取模式，如果没有模式指令则返回{@code null}
     *
     * @param instructions 指令集
     * @return 对应模式或{@code null}当没有模式指令时
     */
    public static JdbcMode valueOf(Set<String> instructions) {
        if (instructions == null || instructions.isEmpty()) {
            return null;
        }
        JdbcMode mode = null;
        for (JdbcMode value : values()) {
            if (instructions.contains(value.name())) {
                if (mode != null) {
                    throw new IllegalArgumentException("multiple mode instructions");
                } else {
                    mode = value;
                }
            }
        }
        return mode;
    }

}
