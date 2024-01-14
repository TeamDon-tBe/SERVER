package com.dontbe.www.DontBeServer.api.comment.dto.response;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;

public record CommentAllByMemberResponseDto(
        long memberId,	//	답글 작성자 Id
        String memberProfileUrl, //	작성자 프로필 사진url
        String memberNickname,   //	답글 작성자 닉네임
        Boolean isLiked,	//	유저가 답글에 대해 좋아요를 눌렀는지
        Boolean isGhost, //답글 작성자를 투명도 처리 했는지
        int memberGhost,  //	답글 작성자의 전체 투명도
        int commentLikedNumber,  //	답글 좋아요 개수
        String commentText,  //	답글 내용
        String time, //답글이 작성된 시간 (년-월-일 시:분:초)
        long    commentId,    //댓글 Id
        long contentId //	해당 댓글이 적힌 게시물 Id
) {
    public static CommentAllByMemberResponseDto of(Member writerMember, boolean isLiked, boolean isGhost,
    int memberGhost, int commentLikedNumber, Comment comment){
        return new CommentAllByMemberResponseDto(
                writerMember.getId() ,
                writerMember.getProfileUrl(),
                writerMember.getNickname(),
                isLiked,
                isGhost,
                memberGhost,
                commentLikedNumber,
                comment.getCommentText(),
                TimeUtilCustom.refineTime(comment.getCreatedAt()),
                comment.getId(),
                comment.getContent().getId()
        );
    }
}
