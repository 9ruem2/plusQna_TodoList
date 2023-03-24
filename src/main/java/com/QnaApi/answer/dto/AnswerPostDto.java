package com.QnaApi.answer.dto;

import com.QnaApi.board.entity.Board;
import lombok.Getter;

@Getter
public class AnswerPostDto {
    private String comment;
    private Long boardId;
    private Long memberId;
}
