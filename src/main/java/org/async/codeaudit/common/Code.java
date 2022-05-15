package org.async.codeaudit.common;

import org.async.codeaudit.entiy.User;

public class Code {
    private int code;
    private String message;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Code(int code, String message, User user) {
        this.code = code;
        this.message = message;
        this.user = user;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Code{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Code() {
    }
}
