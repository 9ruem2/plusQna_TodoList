package com.QnaApi.answer.dto;

import com.QnaApi.board.entity.Board;
import com.QnaApi.member.entity.Member;
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
