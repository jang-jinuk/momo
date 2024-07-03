package com.momo.momopjt.schedule;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = -1174644711L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final com.momo.momopjt.club.QClub clubNo;

    public final StringPath scheduleContent = createString("scheduleContent");

    public final NumberPath<Integer> scheduleMax = createNumber("scheduleMax", Integer.class);

    public final NumberPath<Long> scheduleNo = createNumber("scheduleNo", Long.class);

    public final StringPath schedulePhoto = createString("schedulePhoto");

    public final StringPath schedulePlace = createString("schedulePlace");

    public final DateTimePath<java.time.Instant> scheduleStartDate = createDateTime("scheduleStartDate", java.time.Instant.class);

    public final StringPath scheduleTitle = createString("scheduleTitle");

    public final SetPath<com.momo.momopjt.userAndSchedule.UserAndSchedule, com.momo.momopjt.userAndSchedule.QUserAndSchedule> userAndSchedules = this.<com.momo.momopjt.userAndSchedule.UserAndSchedule, com.momo.momopjt.userAndSchedule.QUserAndSchedule>createSet("userAndSchedules", com.momo.momopjt.userAndSchedule.UserAndSchedule.class, com.momo.momopjt.userAndSchedule.QUserAndSchedule.class, PathInits.DIRECT2);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubNo = inits.isInitialized("clubNo") ? new com.momo.momopjt.club.QClub(forProperty("clubNo"), inits.get("clubNo")) : null;
    }

}

