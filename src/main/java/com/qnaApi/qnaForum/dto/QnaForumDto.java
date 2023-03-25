package com.qnaApi.qnaForum.dto;

import com.qnaApi.answer.dto.AnswerResponseDto;
import com.qnaApi.qnaForum.entity.QnaForum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class QnaForumDto {
    @Setter
    @Getter
    public static class Patch {
        private Long memberId;
        private Long boardId;
        private String content;
        private String title;
        private QnaForum.ContentStatus contentStatus; //Enum은 정의할때만 쓰는거라 타입만 쓰면 됨
    }

    @Getter
//@Validated
    public static class Post {
        @Positive
        private Long memberId;
        @NotBlank
        private String content;
        @NotBlank
        private String title;

        @NotNull
        private QnaForum.ContentStatus contentStatus; //공개 비공개
    }

    @Getter
    public static class Delete {
        private Long memberId;
        private Long qnaForumId;

    }

    @Getter
    @Setter
    public static class SingleResponse {
        private Long qnaForumId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private AnswerResponseDto answerResponseDto;

    }

    @Getter
    public static class MultiResponse<T> {
        private List<T> data;
        private PageInfo pageInfo;


        public MultiResponse(List<T> data, Page page){
            this.data = data;
            this.pageInfo = new PageInfo(page.getNumber() + 1,
                    page.getSize(), page.getTotalElements(), page.getTotalPages());
        }
    }





}
