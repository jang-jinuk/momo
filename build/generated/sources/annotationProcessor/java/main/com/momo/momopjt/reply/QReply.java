package com.momo.momopjt.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 1274535665L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final com.momo.momopjt.article.QArticle articleNo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.time.Instant> replyCreateDate = createDateTime("replyCreateDate", java.time.Instant.class);

    public final NumberPath<Integer> replyLikeNumber = createNumber("replyLikeNumber", Integer.class);

    public final DateTimePath<java.time.Instant> replyModifyDate = createDateTime("replyModifyDate", java.time.Instant.class);

    public final ComparablePath<Character> replyState = createComparable("replyState", Character.class);

    public final com.momo.momopjt.schedule.QSchedule scheduleNo;

    public final com.momo.momopjt.user.QUser userNo;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.articleNo = inits.isInitialized("articleNo") ? new com.momo.momopjt.article.QArticle(forProperty("articleNo")) : null;
        this.scheduleNo = inits.isInitialized("scheduleNo") ? new com.momo.momopjt.schedule.QSchedule(forProperty("scheduleNo"), inits.get("scheduleNo")) : null;
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

