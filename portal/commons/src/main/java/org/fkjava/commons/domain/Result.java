package org.fkjava.commons.domain;

public class Result {
    private int code;
    private String message;
    private Object attachment;

    public static Result ok() {
        return ok(null);
    }

    public static Result ok(String message) {
        Result result = new Result();
        result.message = message;
        result.code = 1;
        return result;
    }

    public static Result error() {
        return error(null);
    }

    public static Result error(String message) {
        Result result = new Result();
        result.message = message;
        result.code = 2;
        return result;
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

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
