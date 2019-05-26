package io.sqlman.exception;

/**
 * 版本重复异常
 * 当SQL资源中包含重复的版本号时抛出
 *
 * @author Payne 646742615@qq.com
 * 2019/5/25 21:14
 */
public class DuplicatedVersionException extends Exception {
    private final String version;

    public DuplicatedVersionException(String version) {
        this.version = version;
    }

    public DuplicatedVersionException(String message, String version) {
        super(message);
        this.version = version;
    }

    public DuplicatedVersionException(String message, Throwable cause, String version) {
        super(message, cause);
        this.version = version;
    }

    public DuplicatedVersionException(Throwable cause, String version) {
        super(cause);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
