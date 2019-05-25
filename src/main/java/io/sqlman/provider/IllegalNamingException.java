package io.sqlman.provider;

/**
 * 不正确的SQL资源命名异常
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 21:22
 */
public class IllegalNamingException extends RuntimeException {
    private final String name;

    public IllegalNamingException(String name) {
        this.name = name;
    }

    public IllegalNamingException(String message, String name) {
        super(message);
        this.name = name;
    }

    public IllegalNamingException(String message, Throwable cause, String name) {
        super(message, cause);
        this.name = name;
    }

    public IllegalNamingException(Throwable cause, String name) {
        super(cause);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
