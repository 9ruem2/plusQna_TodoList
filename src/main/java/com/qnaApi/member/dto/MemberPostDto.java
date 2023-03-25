package com.qnaApi.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class MemberPostDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "이름은 공백이 아니어야 한다")
    private String name;

    @Pattern(message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 한다.",
             regexp = "^010-\\d{3,4}-\\d{4}$")
    private String phone;


}
