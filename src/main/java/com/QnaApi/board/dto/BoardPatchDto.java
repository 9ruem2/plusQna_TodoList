package com.QnaApi.board.dto;

import com.QnaApi.board.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardPatchDto {
    private Long memberId;
    private Long boardId;
    private String content;
    private String title;
    private Board.ContentStatus contentStatus; //Enum은 정의할때만 쓰는거라 타입만 쓰면 됨
}
