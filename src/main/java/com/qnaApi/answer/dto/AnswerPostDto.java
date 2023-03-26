package com.qnaApi.answer.dto;

import lombok.Getter;

@Getter
public class AnswerPostDto {
    private String comment;
    private Long qnaForumId;
    private Long memberId;
}
