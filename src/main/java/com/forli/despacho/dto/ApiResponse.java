package com.forli.despacho.dto;

public class ApiResponse<T> {
    private boolean ok;
    private T data;
    private ErrorDetail error;

    public ApiResponse() {}

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.ok = true;
        r.data = data;
        return r;
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.ok = false;
        r.error = new ErrorDetail(code, message);
        return r;
    }

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public ErrorDetail getError() { return error; }
    public void setError(ErrorDetail error) { this.error = error; }

    public static class ErrorDetail {
        private String code;
        private String message;

        public ErrorDetail() {}

        public ErrorDetail(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
