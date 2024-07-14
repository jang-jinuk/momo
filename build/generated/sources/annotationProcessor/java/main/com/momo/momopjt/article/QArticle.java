package com.momo.momopjt.article;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 1132327561L;

    public static final QArticle article = new QArticle("article");

    public final StringPath articleContent = createString("articleContent");

    public final DateTimePath<java.time.Instant> articleCreateDate = createDateTime("articleCreateDate", java.time.Instant.class);

    public final NumberPath<Integer> articleScore = createNumber("articleScore", Integer.class);

    public final ComparablePath<Character> articleState = createComparable("articleState", Character.class);

    public final StringPath articleTitle = createString("articleTitle");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QArticle(String variable) {
        super(Article.class, forVariable(variable));
    }

    public QArticle(Path<? extends Article> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArticle(PathMetadata metadata) {
        super(Article.class, metadata);
    }

}

