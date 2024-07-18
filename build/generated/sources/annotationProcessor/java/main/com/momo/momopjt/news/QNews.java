package com.momo.momopjt.news;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNews is a Querydsl query type for News
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNews extends EntityPathBase<News> {

    private static final long serialVersionUID = -757162983L;

    public static final QNews news = new QNews("news");

    public final StringPath newsContent = createString("newsContent");

    public final DateTimePath<java.time.Instant> newsCreateDate = createDateTime("newsCreateDate", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> newsModifyDate = createDateTime("newsModifyDate", java.time.Instant.class);

    public final NumberPath<Long> newsNo = createNumber("newsNo", Long.class);

    public final StringPath newsTag = createString("newsTag");

    public final StringPath newsTitle = createString("newsTitle");

    public QNews(String variable) {
        super(News.class, forVariable(variable));
    }

    public QNews(Path<? extends News> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNews(PathMetadata metadata) {
        super(News.class, metadata);
    }

}

