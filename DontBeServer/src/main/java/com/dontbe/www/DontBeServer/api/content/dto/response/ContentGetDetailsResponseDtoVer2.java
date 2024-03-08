package com.dontbe.www.DontBeServer.api.content.dto.response;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record ContentGetDetailsResponseDtoVer2(
        Long memberId,
        String memberProfileUrl,
        String memberNickname,
        boolean isGhost,
        int memberGhost,
        boolean isLiked,
        String time,
        int likedNumber,
        int commentNumber,
        String contentText,
        Boolean isDeleted
) {
    public static ContentGetDetailsResponseDtoVer2 of(Member member, int memberGhost, Content content, boolean isGhost, boolean isLiked, String time, int likedNumber, int commentNumber){
        return new ContentGetDetailsResponseDtoVer2(
                member.getId(),
                member.getProfileUrl(),
                member.getNickname(),
                isGhost,
                memberGhost,
                isLiked,
                time,
                likedNumber,
                commentNumber,
                content.getContentText(),
                member.isDeleted()
        );
    }
}
