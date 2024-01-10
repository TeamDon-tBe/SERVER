package com.dontbe.www.DontBeServer.api.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;


@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommentPostRequestDto {
    @NotNull
    private String commentText;
}
