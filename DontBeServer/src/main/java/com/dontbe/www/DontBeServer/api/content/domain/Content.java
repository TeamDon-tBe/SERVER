package com.dontbe.www.DontBeServer.api.content.domain;

import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Content extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String contentText;

    @OneToMany(mappedBy = "content", cascade = ALL)
    private List<ContentLiked> contentLikeds = new ArrayList<>();

    @Builder
    public Content(Member member, String contentText) {
        this.member = member;
        this.contentText = contentText;
    }
}
