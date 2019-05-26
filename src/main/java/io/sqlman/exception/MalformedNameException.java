package io.sqlman.exception;

/**
 * 不正确的SQL资源命名异常
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 21:22
 */
public class MalformedNameException extends RuntimeException {
    private final String name;

    public MalformedNameException(String name) {
        this.name = name;
    }

    public MalformedNameException(String message, String name) {
        super(message);
        this.name = name;
    }

    public MalformedNameException(String message, Throwable cause, String name) {
        super(message, cause);
        this.name = name;
    }

    public MalformedNameException(Throwable cause, String name) {
        super(cause);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
