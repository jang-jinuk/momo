package com.momo.momopjt.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = -1002286599L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final NumberPath<Integer> a = createNumber("a", Integer.class);

    public final StringPath clubArea = createString("clubArea");

    public final StringPath clubCategory = createString("clubCategory");

    public final StringPath clubContent = createString("clubContent");

    public final DateTimePath<java.time.Instant> clubCreateDate = createDateTime("clubCreateDate", java.time.Instant.class);

    public final ComparablePath<Character> clubGender = createComparable("clubGender", Character.class);

    public final NumberPath<Integer> clubMax = createNumber("clubMax", Integer.class);

    public final StringPath clubName = createString("clubName");

    public final NumberPath<Long> clubNo = createNumber("clubNo", Long.class);

    public final com.momo.momopjt.photo.QPhoto clubPhoto;

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clubPhoto = inits.isInitialized("clubPhoto") ? new com.momo.momopjt.photo.QPhoto(forProperty("clubPhoto"), inits.get("clubPhoto")) : null;
    }

}

