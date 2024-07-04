package com.momo.momopjt.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserClub is a Querydsl query type for UserClub
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserClub extends EntityPathBase<UserClub> {

    private static final long serialVersionUID = -63210780L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserClub userClub = new QUserClub("userClub");

    public final QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ComparablePath<Character> isLeader = createComparable("isLeader", Character.class);

    public final ComparablePath<Character> isWish = createComparable("isWish", Character.class);

    public final DateTimePath<java.time.Instant> joinDate = createDateTime("joinDate", java.time.Instant.class);

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public QUserClub(String variable) {
        this(UserClub.class, forVariable(variable), INITS);
    }

    public QUserClub(Path<? extends UserClub> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserClub(PathMetadata metadata, PathInits inits) {
        this(UserClub.class, metadata, inits);
    }

    public QUserClub(Class<? extends UserClub> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
    }

}

