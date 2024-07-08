package com.momo.momopjt.userandschedule;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAndSchedule is a Querydsl query type for UserAndSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAndSchedule extends EntityPathBase<UserAndSchedule> {

    private static final long serialVersionUID = -1033353693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAndSchedule userAndSchedule = new QUserAndSchedule("userAndSchedule");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> participants = createNumber("participants", Integer.class);

    public final com.momo.momopjt.schedule.QSchedule scheduleNo;

    public final com.momo.momopjt.user.QUser userNo;

    public QUserAndSchedule(String variable) {
        this(UserAndSchedule.class, forVariable(variable), INITS);
    }

    public QUserAndSchedule(Path<? extends UserAndSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAndSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAndSchedule(PathMetadata metadata, PathInits inits) {
        this(UserAndSchedule.class, metadata, inits);
    }

    public QUserAndSchedule(Class<? extends UserAndSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.scheduleNo = inits.isInitialized("scheduleNo") ? new com.momo.momopjt.schedule.QSchedule(forProperty("scheduleNo"), inits.get("scheduleNo")) : null;
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

