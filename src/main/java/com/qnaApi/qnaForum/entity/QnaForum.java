package com.qnaApi.qnaForum.entity;

import com.qnaApi.answer.entity.Answer;
import com.qnaApi.audit.Auditable;
import com.qnaApi.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity(name = "QNAFORUM")
@Getter
@Setter
@NoArgsConstructor
public class QnaForum extends Auditable {
    @Id //보드인식번호
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaForumId;

    @Enumerated(EnumType.STRING) //질문상태, 답변완료, 질문삭제
    @Column(length = 30, nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTRATION;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false) //공개, 비공개글
    private ContentStatus contentStatus = ContentStatus.PUBLIC; // public으로 기본타입 설정

    @ManyToOne
    @JoinColumn(name = "MEMBERID")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    public enum QuestionStatus {
        QUESTION_REGISTRATION, //질문 등록, 답변대기
        QUESTION_ANSWERED, //답변완료
        QUESTION_DELETE; // 질문삭제
    }

    public enum ContentStatus {
        PUBLIC,
        SECRET;
    }

    @OneToOne(mappedBy = "qnaForum", cascade = CascadeType.REMOVE)
    private Answer answer;
}
