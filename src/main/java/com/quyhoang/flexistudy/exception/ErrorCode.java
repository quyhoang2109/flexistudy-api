package com.quyhoang.flexistudy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid Key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at leat {min}", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1009, "File not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1010, "Invalid Request", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(1011, "Company not found", HttpStatus.BAD_REQUEST),
    JOB_NOT_FOUND(1011, "Job is not found", HttpStatus.BAD_REQUEST),
    SKILL_NOT_FOUND(1012, "Skill not found", HttpStatus.BAD_REQUEST),
    JOB_REQUIRED_EXISTED(1013, "Job required is existed", HttpStatus.BAD_REQUEST),
    JOB_SKILL_REQUIRED_IS_NOT_FOUND(1014, "Job required Skill is not found", HttpStatus.BAD_REQUEST),
    SKILL_EXISTED(1015, "Skill is existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1016, "Role is invalid", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
