package com.momo.momopjt.userandclub;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAndClub is a Querydsl query type for UserAndClub
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAndClub extends EntityPathBase<UserAndClub> {

    private static final long serialVersionUID = -1201562271L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAndClub userAndClub = new QUserAndClub("userAndClub");

    public final com.momo.momopjt.club.QClub clubNo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLeader = createBoolean("isLeader");

    public final ComparablePath<Character> isWish = createComparable("isWish", Character.class);

    public final DateTimePath<java.time.Instant> joinDate = createDateTime("joinDate", java.time.Instant.class);

    public final com.momo.momopjt.user.QUser userNo;

    public QUserAndClub(String variable) {
        this(UserAndClub.class, forVariable(variable), INITS);
    }

    public QUserAndClub(Path<? extends UserAndClub> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAndClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAndClub(PathMetadata metadata, PathInits inits) {
        this(UserAndClub.class, metadata, inits);
    }

    public QUserAndClub(Class<? extends UserAndClub> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubNo = inits.isInitialized("clubNo") ? new com.momo.momopjt.club.QClub(forProperty("clubNo"), inits.get("clubNo")) : null;
        this.userNo = inits.isInitialized("userNo") ? new com.momo.momopjt.user.QUser(forProperty("userNo")) : null;
    }

}

