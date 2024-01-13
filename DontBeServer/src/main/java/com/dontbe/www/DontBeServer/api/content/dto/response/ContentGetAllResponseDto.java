package com.dontbe.www.DontBeServer.api.content.dto.response;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record ContentGetAllResponseDto (
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
        int commentNumber
) {
//    public ContentGetAllResponseDto(Long id, String profileUrl, String nickname, Long id1, String contentText, String time, boolean isGhost, int memberGhost, boolean isLiked, String time1, int likedNumber, int commentNumber, String contentText1) {
//    }

    public static ContentGetAllResponseDto of(Member writerMember, Content content, boolean isGhost, boolean isLiked, String time, int likedNumber, int commentNumber) {
        return new ContentGetAllResponseDto(
                writerMember.getId(),
                writerMember.getProfileUrl(),
                writerMember.getNickname(),
                content.getId(),
                content.getContentText(),
                time,
                isGhost,
                writerMember.getMemberGhost(),
                isLiked,
                likedNumber,
                commentNumber
        );
    }
}
