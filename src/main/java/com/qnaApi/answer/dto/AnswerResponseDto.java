package com.qnaApi.answer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponseDto {
    private Long answerId;
    private String comment;

    private Long memberId;
    private Long boardId;

}
