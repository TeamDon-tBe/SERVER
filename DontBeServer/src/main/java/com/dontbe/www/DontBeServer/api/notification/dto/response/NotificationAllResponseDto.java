package com.dontbe.www.DontBeServer.api.notification.dto.response;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import com.dontbe.www.DontBeServer.common.util.TimeUtilCustom;

public record NotificationAllResponseDto(
        long memberId,   //사용하는 유저Id
        String memberNickname,  //사용하는 유저 닉네임
        String triggerMemberNickname,	//노티 유발자의 닉네임
        String triggerMemberProfileUrl,	//노티 유발자 프로필 사진url
        String notificationTriggerType,
        String time,	//	노티가 발생한 시간을 (년-월-일 시:분:초)
        Long notificationTriggerId ,    //	노티 발생 시 해당 경우의 Id
        String notificationText, //	댓글 노티에 나올 댓글 내용
        boolean isNotificationChecked    //	유저가 확인한 노티인지 아닌지
) {
    public static NotificationAllResponseDto of(Member usingMember, String triggerMemberNickname, Notification notification,
                                                boolean isNotificationChecked, Long notificationTriggerId, String imageUrl) {
        return new NotificationAllResponseDto(
                usingMember.getId(),
                usingMember.getNickname(),
                triggerMemberNickname,
                imageUrl,
                notification.getNotificationTriggerType(),
                TimeUtilCustom.refineTime(notification.getCreatedAt()),
                notificationTriggerId,
                notification.getNotificationText(),
                isNotificationChecked
        );
    }

}

