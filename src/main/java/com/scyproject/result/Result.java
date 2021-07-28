package com.scyproject.result;

import org.springframework.web.bind.annotation.ResponseBody;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /*
    * 成功的时候调用
    * */
    public static <T> Result<T>success(T data){
        return new Result<T>(data);
    }

    public static <T> Result<T>success(int code ,String msg,T data){
        return new Result(code,msg,data);
    }
    /*
    * 失败时调用
    * */
    public static <T>Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    private Result(CodeMsg msg) {
        if(msg == null)
            return;
        this.msg = msg.getMsg();
        this.code = msg.getCode();
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }
}
