package com.momo.momopjt.alarm;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarm is a Querydsl query type for Alarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarm extends EntityPathBase<Alarm> {

    private static final long serialVersionUID = 1515668607L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarm alarm = new QAlarm("alarm");

    public final StringPath alarmContent = createString("alarmContent");

    public final DateTimePath<java.time.Instant> alarmCreateDate = createDateTime("alarmCreateDate", java.time.Instant.class);

    public final NumberPath<Long> AlarmNo = createNumber("AlarmNo", Long.class);

    public final StringPath alarmType = createString("alarmType");

    public final ComparablePath<Character> isRead = createComparable("isRead", Character.class);

    public final com.momo.momopjt.user.QUser userNo;

    public QAlarm(String variable) {
        this(Alarm.class, forVariable(variable), INITS);
    }

    public QAlarm(Path<? extends Alarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarm(PathMetadata metadata, PathInits inits) {
        this(Alarm.class, metadata, inits);
    }

    public QAlarm(Class<? extends Alarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

