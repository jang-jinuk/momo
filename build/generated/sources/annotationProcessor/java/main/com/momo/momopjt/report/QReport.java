package com.momo.momopjt.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReport is a Querydsl query type for Report
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReport extends EntityPathBase<Report> {

    private static final long serialVersionUID = -760888135L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReport report = new QReport("report");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reportCategory = createString("reportCategory");

    public final DateTimePath<java.time.Instant> reportDate = createDateTime("reportDate", java.time.Instant.class);

    public final com.momo.momopjt.user.QUser reportedNo;

    public final com.momo.momopjt.user.QUser reporterNo;

    public final ComparablePath<Character> reportResult = createComparable("reportResult", Character.class);

    public QReport(String variable) {
        this(Report.class, forVariable(variable), INITS);
    }

    public QReport(Path<? extends Report> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReport(PathMetadata metadata, PathInits inits) {
        this(Report.class, metadata, inits);
    }

    public QReport(Class<? extends Report> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedNo = inits.isInitialized("reportedNo") ? new com.momo.momopjt.user.QUser(forProperty("reportedNo")) : null;
        this.reporterNo = inits.isInitialized("reporterNo") ? new com.momo.momopjt.user.QUser(forProperty("reporterNo")) : null;
    }

}

