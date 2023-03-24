package com.QnaApi.board.dto;

import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class BoardGetDto {

    @Positive
    private Long memberId;
    @Positive
    private Long boardId;
}
