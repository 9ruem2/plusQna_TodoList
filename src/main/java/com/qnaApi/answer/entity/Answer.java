package com.qnaApi.answer.entity;

import com.qnaApi.audit.Auditable;
import com.qnaApi.qnaForum.entity.QnaForum;
import com.qnaApi.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Answer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column
    private String comment;

    @OneToOne
    @JoinColumn(name = "qnaForumId")
    private QnaForum qnaForum;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
