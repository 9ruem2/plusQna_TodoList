package com.qnaApi.member.controller;

import com.qnaApi.member.dto.MemberPatchDto;
import com.qnaApi.member.dto.MemberPostDto;
import com.qnaApi.member.service.MemberService;
import com.qnaApi.member.entity.Member;
import com.qnaApi.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/v1/members")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberPostDto memberPostDto){
        Member member = mapper.memberDtoToMember(memberPostDto);
        Member response = memberService.createMember(member);

        return new ResponseEntity(mapper.memberToMemberResponseDto(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberPatchDto memberPatchDto) {
        memberPatchDto.setMemberId(memberId);

        return new ResponseEntity<>(memberPatchDto, HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id")long memberId){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") long memberId){
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
