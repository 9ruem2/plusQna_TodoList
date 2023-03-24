package com.QnaApi.member.service;

import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.entity.Member;
import com.QnaApi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j //심플로그를 쓸 수 있게 해줌
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    //회원가입 로직
    public Member createMember(Member member){
        // 이미 등록된 이메일인지 확인하기
        verifyExistsEmail(member.getEmail());

        //직접출력한거 확인하기
//        log.info(member.getEmail());
//        log.error("log출력");
//        log.warn("위험{}",member.getMemberId());

        return memberRepository.save(member); //repository에 저장
    }

    //등록된 이메일 확인하는 로직
    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email); //Optional<T>클래스는 해당 변수가 null값을 혹시라도 가지고 있을 경우, NPE가 발생하지 않는다.
        if(member.isPresent()) //null이 아닌경우로 Optional이 만들어져 있다면
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS); //MEMBER_EXISTS:멤버로 확인됨
    }

    //멤버정보 수정하기
    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getMemberId()); // 멤버 아이디를 가지고 멤버정보 가져오기

        // 수정할 정보들이 늘어나면 반복되는 코드가 늘어나는 문제점이 있음
        // optional은 rapper클래스라고 할 수 있음
        //.ofNullable()를 통해 null이 아니면 값을 가지는 Optional객체를 생성하여 반환해주고 null이면 비어있는 optional객체를 반환함
        Optional.ofNullable(member.getName()).ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findMember.setPhone(phone));
        // 추가된 부분
        Optional.ofNullable(member.getMemberStatus()).ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));
//        findMember.setModifiedAt(LocalDateTime.now());

        return memberRepository.save(findMember);
    }

    // 멤버가 있는지 없는지 확인하는 로직
    public Member findVerifiedMember(Long memberId) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

/*
     이 아이디에 해당하는 member가 저장소에 있냐없냐 확인하는로직.
     항상 저장소에서 값을 가지고 올 때는 null일수도 아닐 수도있다는 사실을 생각해야한다.
     옵셔널을 사용하지 않고 member가 Null인지 아닌지 확인하는 로직
    if(member==null){ //멤버가 Null이라면?
        throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND); // 예외를 던져라
    }
    return member;
*/



    // //게시글이 존재하는지 확인
    //    public Board findVerifiedBoard(Long boardId){
    //        Optional<Board> optionalBoard = boardRepository.findById(boardId);
    //        Board findBoard =
    //                optionalBoard.orElseThrow(()->
    //                    new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    //        return findBoard;
    //    }


    public List<Member> findMembers(){
        //List<Member> members = List.of(
                //new Member(1,"hgd@gmail.com","홍길동","010-1234-1234"),
                //new Member(2,"qwe@gmail.com","이길동","010-1234-5678"));
//        return members;
        return null;
    }

    public void deleteMember(long memberId){

    }


}
