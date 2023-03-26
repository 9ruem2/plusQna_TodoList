package com.qnaApi.answer.service;

import com.qnaApi.answer.entity.Answer;
import com.qnaApi.answer.repository.AnswerRepository;
import com.qnaApi.qnaForum.repository.BoardRepository;
import com.qnaApi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    public Answer createAnswer(Answer answer){
        //
        return answerRepository.save(answer);
    }
}
