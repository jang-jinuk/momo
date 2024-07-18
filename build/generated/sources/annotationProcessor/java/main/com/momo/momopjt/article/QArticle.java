package com.momo.momopjt.article;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 1132327561L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final StringPath articleContent = createString("articleContent");

    public final DateTimePath<java.time.Instant> articleCreateDate = createDateTime("articleCreateDate", java.time.Instant.class);

    public final NumberPath<Long> articleNo = createNumber("articleNo", Long.class);

    public final NumberPath<Integer> articleScore = createNumber("articleScore", Integer.class);

    public final ComparablePath<Character> articleState = createComparable("articleState", Character.class);

    public final StringPath articleTitle = createString("articleTitle");

    public final com.momo.momopjt.club.QClub clubNo;

    public final com.momo.momopjt.user.QUser userNo;

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubNo = inits.isInitialized("clubNo") ? new com.momo.momopjt.club.QClub(forProperty("clubNo"), inits.get("clubNo")) : null;
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

