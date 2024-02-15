package com.dontbe.www.DontBeServer.api.content.domain;

import com.dontbe.www.DontBeServer.api.comment.domain.Comment;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "isDeleted = false")
public class Content extends BaseTimeEntity {

    private static final long CONTENT_RETENTION_PERIOD = 14L;   // 게시글 삭제 후 보유기간 14일로 설정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String contentText;

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private List<ContentLiked> contentLikeds = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    private LocalDateTime deleteAt;

    @Builder
    public Content(Member member, String contentText) {
        this.member = member;
        this.contentText = contentText;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deleteAt = LocalDateTime.now().plusDays(CONTENT_RETENTION_PERIOD);
    }

}