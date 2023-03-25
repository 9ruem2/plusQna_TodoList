package com.qnaApi.member.mapper;

import com.qnaApi.member.entity.Member;
import com.qnaApi.member.dto.MemberPostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberDtoToMember(MemberPostDto memberPostDto);

    MemberPostDto memberToMemberResponseDto(Member response);
}
