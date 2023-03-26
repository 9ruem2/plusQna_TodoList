package com.qnaApi.qnaForum.mapper;

import com.qnaApi.answer.dto.AnswerResponseDto;
import com.qnaApi.answer.entity.Answer;
import com.qnaApi.qnaForum.dto.QnaForumDto;
import com.qnaApi.qnaForum.entity.QnaForum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QnaForumMapper {
    @Mapping(source = "memberId", target = "member.memberId")
        //소스:포스트DTO -> board// memberId->
    QnaForum qnaForumPostDtoToQnaForum(QnaForumDto.Post qnaForumPostDto);

    @Mapping(source = "memberId", target = "member.memberId")
    QnaForum qnaForumPatchDtoToQnaForum(QnaForumDto.Patch qnaForumPatchDto);

    @Mapping(source = "memberId", target = "member.memberId")
    QnaForum qnaForumDeleteDtoToQnaForum(QnaForumDto.Delete qnAForumDeleteDto);

    default QnaForumDto.SingleResponse qnaForumToQnaForumSingleResponseDto(QnaForum response) {
        QnaForumDto.SingleResponse qnaForumResponseDto = new QnaForumDto.SingleResponse();
        AnswerResponseDto answerResponseDto = new AnswerResponseDto();
        if (response.getAnswer() == null) {
            answerResponseDto.setQnaForumId(null);
            answerResponseDto.setComment("empty answer");
            answerResponseDto.setMemberId(null);
            answerResponseDto.setAnswerId(null);
        } else {
            Answer answer = response.getAnswer();
            answerResponseDto.setAnswerId(answer.getAnswerId());
            answerResponseDto.setQnaForumId(answer.getQnaForum().getQnaForumId());
            answerResponseDto.setMemberId(answer.getMember().getMemberId());
            answerResponseDto.setComment(answer.getComment());
        }
        qnaForumResponseDto.setAnswerResponseDto(answerResponseDto);
        qnaForumResponseDto.setQnaForumId(response.getQnaForumId());
        qnaForumResponseDto.setTitle(response.getTitle());
        qnaForumResponseDto.setContent(response.getContent());
        qnaForumResponseDto.setCreatedAt(response.getCreatedAt());
        return qnaForumResponseDto;
    }

    List<QnaForumDto.SingleResponse> qnaForumsToQnaForumsResponseDtos(List<QnaForum> qnaForums);


}
