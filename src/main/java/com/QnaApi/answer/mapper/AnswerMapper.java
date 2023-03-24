package com.QnaApi.answer.mapper;

import com.QnaApi.answer.dto.AnswerPostDto;
import com.QnaApi.answer.dto.AnswerResponseDto;
import com.QnaApi.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "boardId", target = "board.boardId")
    public Answer answerPostDtoToAnswer(AnswerPostDto answerPostDto);

    @Mapping(source = "answer.member.memberId", target = "memberId")
    @Mapping(source = "answer.board.boardId", target = "boardId")
    AnswerResponseDto AnswerToAnswerResponseDto(Answer answer);
}
