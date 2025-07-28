package com.example.vmoProject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(999,"Invalid key.", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001,"User existed.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"Username need length > 8", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002,"Password need length > 8", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003,"User not existed.",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004,"UnAuthenticated.",HttpStatus.UNAUTHORIZED),
    ROLE_EXISTED(1005,"Role existed.",HttpStatus.BAD_REQUEST),
    ROLE_NULL(1006,"List roles is empty.",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1007,"Role not found.",HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1008,"Permission existed.",HttpStatus.BAD_REQUEST),
    PERMISSION_NULL(1009,"List permission is empty.",HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1010,"Permission not found.",HttpStatus.NOT_FOUND),
    LIST_USER_NULL(1011,"List user is empty.",HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code,String message,HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
