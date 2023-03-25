package com.qnaApi.exception;

import lombok.Getter;

public enum ExceptionCode { //예외처리 받는 로직
    MEMBER_NOT_FOUND(404, "Member not found"), //멤버 찾을수없음
    MEMBER_EXISTS(409, "Member exists"), // 멤버가 존재함
    MEMBER_DOES_NOT_MATCH(403,"Member does not match"),
    COFFEE_NOT_FOUND(404, "Coffee not found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code exists"),
    ORDER_NOT_FOUND(404, "Order not found"),
    CANNOT_CHANGE_ORDER(403, "Order can not change"),
    BOARD_NOT_FOUND(404, "BOARD not found"),
    CANNOT_CHANGE_ANSWERED_QUESTION(403, "CANNOT_CHANGE_ANSWERED_QUESTION"),
    CANNOT_CHANGE_BOARD(403, "Board can not change & delete "),
    BOARD_HAS_BEEN_DELETED(403, "Board has been deleted"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_MEMBER_STATUS(400, "Invalid member status");  // TO 추가된 부분

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
