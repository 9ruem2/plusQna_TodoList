package com.QnaApi.board.dto;

import com.QnaApi.answer.dto.AnswerResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private AnswerResponseDto answerResponseDto;

}
