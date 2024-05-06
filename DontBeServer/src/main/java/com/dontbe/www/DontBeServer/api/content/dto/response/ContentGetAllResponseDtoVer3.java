package com.dontbe.www.DontBeServer.api.content.dto.response;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record ContentGetAllResponseDtoVer3(
        Long memberId,
        String memberProfileUrl,
        String memberNickname,
        Long contentId,
        String contentText,
        String time,
        boolean isGhost,
        int memberGhost,
        boolean isLiked,
        int likedNumber,
        int commentNumber,
        Boolean isDeleted,
        String contentImageUrl
) {
    public static ContentGetAllResponseDtoVer3 of(Member writerMember, Content content, boolean isGhost, int refinedMemberGhost, boolean isLiked, String time, int likedNumber, int commentNumber) {
        return new ContentGetAllResponseDtoVer3(
                writerMember.getId(),
                writerMember.getProfileUrl(),
                writerMember.getNickname(),
                content.getId(),
                content.getContentText(),
                time,
                isGhost,
                refinedMemberGhost,
                isLiked,
                likedNumber,
                commentNumber,
                writerMember.isDeleted(),
                content.getContentImage()
        );
    }
}