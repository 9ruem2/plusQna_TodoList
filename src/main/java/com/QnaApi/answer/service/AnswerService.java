package com.QnaApi.answer.service;

import com.QnaApi.answer.entity.Answer;
import com.QnaApi.answer.repository.AnswerRepository;
import com.QnaApi.board.repository.BoardRepository;
import com.QnaApi.member.repository.MemberRepository;
import com.QnaApi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Answer createAnswer(Answer answer){
        //
        return answerRepository.save(answer);
    }
}
