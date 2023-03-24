package com.QnaApi.board.mapper;

import com.QnaApi.answer.dto.AnswerResponseDto;
import com.QnaApi.answer.entity.Answer;
import com.QnaApi.board.dto.*;
import com.QnaApi.board.entity.Board;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(source = "memberId" ,target = "member.memberId") //소스:포스트DTO -> board// memberId->
    Board boardPostDtoToBoard(BoardPostDto boardPostDto);
    @Mapping(source = "memberId", target = "member.memberId")
    Board boardPatchDtoToBoard(BoardPatchDto boardPatchDto);

    @Mapping(source = "memberId", target = "member.memberId")
    Board boardDeleteDtoToBoard(BoardDeleteDto boardDeleteDto);

    @Mapping(source="memberId", target="member.memberId")
    Board boardGetDtoToBoard(BoardGetDto boardGetDto);


    default BoardResponseDto BoardToBoardResponseDto(Board response){
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        AnswerResponseDto answerResponseDto = new AnswerResponseDto();
        if(response.getAnswer()==null){
            answerResponseDto.setBoardId(null);
            answerResponseDto.setComment("empty answer");
            answerResponseDto.setMemberId(null);
            answerResponseDto.setAnswerId(null);
        }else{
            Answer answer = response.getAnswer();
            answerResponseDto.setAnswerId(answer.getAnswerId());
            answerResponseDto.setBoardId(answer.getBoard().getBoardId());
            answerResponseDto.setMemberId(answer.getMember().getMemberId());
            answerResponseDto.setComment(answer.getComment());
        }
        boardResponseDto.setAnswerResponseDto(answerResponseDto);
        boardResponseDto.setBoardId(response.getBoardId());
        boardResponseDto.setTitle(response.getTitle());
        boardResponseDto.setContent(response.getContent());
        boardResponseDto.setCreatedAt(response.getCreatedAt());
        return boardResponseDto;
    }


    MultiResponseDto BoardToMultiResponsDto(BoardGetDto boardGetDto);

    List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards);
}
