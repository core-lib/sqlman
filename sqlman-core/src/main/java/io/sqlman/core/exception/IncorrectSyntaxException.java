package io.sqlman.core.exception;

/**
 * 错误的语法异常
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 21:34
 */
public class IncorrectSyntaxException extends RuntimeException {

    public IncorrectSyntaxException() {
    }

    public IncorrectSyntaxException(String message) {
        super(message);
    }

    public IncorrectSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectSyntaxException(Throwable cause) {
        super(cause);
    }
}
