package com.qnaApi.answer.mapper;

import com.qnaApi.answer.dto.AnswerPostDto;
import com.qnaApi.answer.dto.AnswerResponseDto;
import com.qnaApi.answer.entity.Answer;
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
