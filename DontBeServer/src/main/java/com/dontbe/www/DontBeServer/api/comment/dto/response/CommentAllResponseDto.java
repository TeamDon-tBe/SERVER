package com.dontbe.www.DontBeServer.api.comment.dto.response;

import com.dontbe.www.DontBeServer.api.member.domain.Member;

public record CommentAllResponseDto(
        Long commentId, //답글 고유 id
        Long memberId,  //	댓글 작성자 Id
        String memberProfileUrl,     //작성자 프로필 사진url
        String memberNickname,	//댓글 작성자 닉네임
        Boolean isGhost,	//현재 유저가 작성자에 대해 투명도 처리를 했는지
        int memberGhost,	//작성자의 전체 투명도
        Boolean isLiked,	//유저가 게시물에 대해 좋아요를 눌렀는지
        int commentLikedNumber,	//댓글의 좋아요 개수
        String commentText,	//댓글 내용
        String  time	//답글이 작성된 시간을 (년-월-일 시:분:초)
) {
    public static CommentAllResponseDto of(Long commentId, Member writerMember, boolean isGhost, int memberGhost,
                                           boolean isLiked, String time, int likedNumber, String commentText){
        return new CommentAllResponseDto(
                commentId,
                writerMember.getId(),
                writerMember.getProfileUrl(),
                writerMember.getNickname(),
                isGhost,
                memberGhost,
                isLiked,
                likedNumber,
                commentText,
                time
        );
    }
}