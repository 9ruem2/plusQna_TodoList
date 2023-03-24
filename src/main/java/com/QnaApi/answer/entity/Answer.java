package com.QnaApi.answer.entity;

import com.QnaApi.audit.Auditable;
import com.QnaApi.board.entity.Board;
import com.QnaApi.member.entity.Member;
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
    @JoinColumn(name = "boardId")
    private Board board;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
