package com.momo.momopjt.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAndClubId is a Querydsl query type for UserAndClubId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserAndClubId extends BeanPath<UserAndClubId> {

    private static final long serialVersionUID = 438540218L;

    public static final QUserAndClubId userAndClubId = new QUserAndClubId("userAndClubId");

    public final NumberPath<Long> clubNo = createNumber("clubNo", Long.class);

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public QUserAndClubId(String variable) {
        super(UserAndClubId.class, forVariable(variable));
    }

    public QUserAndClubId(Path<? extends UserAndClubId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAndClubId(PathMetadata metadata) {
        super(UserAndClubId.class, metadata);
    }

}

