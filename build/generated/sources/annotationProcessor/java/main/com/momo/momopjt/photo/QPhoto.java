package com.momo.momopjt.photo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPhoto is a Querydsl query type for Photo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPhoto extends EntityPathBase<Photo> {

    private static final long serialVersionUID = -1732862079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPhoto photo = new QPhoto("photo");

    public final com.momo.momopjt.article.QArticle articleNo;

    public final SetPath<com.momo.momopjt.club.Club, com.momo.momopjt.club.QClub> clubs = this.<com.momo.momopjt.club.Club, com.momo.momopjt.club.QClub>createSet("clubs", com.momo.momopjt.club.Club.class, com.momo.momopjt.club.QClub.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.Instant> photoCreateDate = createDateTime("photoCreateDate", java.time.Instant.class);

    public final StringPath photoOriginalName = createString("photoOriginalName");

    public final NumberPath<Integer> photoSize = createNumber("photoSize", Integer.class);

    public final StringPath photoThumbnail = createString("photoThumbnail");

    public final StringPath photoUuid = createString("photoUuid");

    public final com.momo.momopjt.user.QUser userNo;

    public QPhoto(String variable) {
        this(Photo.class, forVariable(variable), INITS);
    }

    public QPhoto(Path<? extends Photo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPhoto(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPhoto(PathMetadata metadata, PathInits inits) {
        this(Photo.class, metadata, inits);
    }

    public QPhoto(Class<? extends Photo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.articleNo = inits.isInitialized("articleNo") ? new com.momo.momopjt.article.QArticle(forProperty("articleNo")) : null;
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

