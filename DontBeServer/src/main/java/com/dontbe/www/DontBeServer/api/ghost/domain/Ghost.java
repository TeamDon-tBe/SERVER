package com.dontbe.www.DontBeServer.api.ghost.domain;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Ghost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member_id")
    private Member ghostTargetMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_member_id")
    private Member ghostTriggerMember;

    private boolean isRecovered;
}
