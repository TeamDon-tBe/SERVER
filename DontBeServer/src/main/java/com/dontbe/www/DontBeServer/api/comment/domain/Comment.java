package com.dontbe.www.DontBeServer.api.comment.domain;

import com.dontbe.www.DontBeServer.api.content.domain.Content;
import com.dontbe.www.DontBeServer.api.member.domain.Member;
import com.dontbe.www.DontBeServer.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {

    private static final long COMMENT_RETENTION_PERIOD = 14L;   //답글 삭제 후 보유기간 14일로 설정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //댓글 작성자 id
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private String commentText;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLiked> commentLikeds = new ArrayList<>();

    @Builder
    public Comment(Member member, Content content, String commentText) {
        this.member = member;
        this.content = content;
        this.commentText = commentText;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deleteAt = LocalDateTime.now().plusDays(COMMENT_RETENTION_PERIOD);
    }
}
