package com.qnaApi.member.entity;

import com.qnaApi.audit.Auditable;
import com.qnaApi.qnaForum.entity.QnaForum;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "MEMBER")
@RequiredArgsConstructor //컴파일러가 필요한 생성자를 자동으로 만들어줌
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    @OneToMany(mappedBy = "member") //member와 board 간의 1대N 연관관계를 매핑
    private List<QnaForum> QnaForums =new ArrayList<>();

    @Enumerated(value = EnumType.STRING) // enum타입의 필드와 매핑한 뒤 String타입으로 저장함. <->EnumType.ORDINAL은 이넘 타입을 숫자로 표현할 수 있는데 enum순서가 바뀌면 데이터의 무결성이 깨질 수 있어서 가급적으로는 사용하지 않는다.
    @Column(length =20, nullable = false) // null 허용하지 않음
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @AllArgsConstructor
    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;
    }
}
