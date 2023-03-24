package com.QnaApi.member.mapper;

import com.QnaApi.member.entity.Member;
import com.QnaApi.member.dto.MemberPostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberDtoToMember(MemberPostDto memberPostDto);

    MemberPostDto memberToMemberResponseDto(Member response);
}
