package io.sqlman;


import java.sql.Date;

/**
 * 数据库版本
 *
 * @author Payne 646742615@qq.com
 * 2019/5/18 13:48
 */
public class SqlVersion {
    private Integer id;
    private String version;
    private Integer ordinal;
    private String description;
    private String author;
    private Integer sqlQuantity;
    private Boolean success;
    private Integer rowEffected;
    private Integer errorCode;
    private String errorState;
    private Date dateExecuted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getSqlQuantity() {
        return sqlQuantity;
    }

    public void setSqlQuantity(Integer sqlQuantity) {
        this.sqlQuantity = sqlQuantity;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getRowEffected() {
        return rowEffected;
    }

    public void setRowEffected(Integer rowEffected) {
        this.rowEffected = rowEffected;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorState() {
        return errorState;
    }

    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    public Date getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(Date dateExecuted) {
        this.dateExecuted = dateExecuted;
    }

    @Override
    public String toString() {
        return "" + version + "/" + ordinal;
    }
}
