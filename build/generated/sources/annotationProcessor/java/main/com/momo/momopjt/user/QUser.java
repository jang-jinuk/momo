package com.momo.momopjt.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -718506215L;

    public static final QUser user = new QUser("user");

    public final ComparablePath<Character> isSocial = createComparable("isSocial", Character.class);

    public final StringPath userAddress = createString("userAddress");

    public final DatePath<java.time.LocalDate> userBirth = createDate("userBirth", java.time.LocalDate.class);

    public final StringPath userCategory = createString("userCategory");

    public final DateTimePath<java.time.Instant> userCreateDate = createDateTime("userCreateDate", java.time.Instant.class);

    public final StringPath userEmail = createString("userEmail");

    public final ComparablePath<Character> userGender = createComparable("userGender", Character.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Integer> userLikeNumber = createNumber("userLikeNumber", Integer.class);

    public final StringPath userMbti = createString("userMbti");

    public final DateTimePath<java.time.Instant> userModifyDate = createDateTime("userModifyDate", java.time.Instant.class);

    public final StringPath userNickname = createString("userNickname");

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public final StringPath userPhoto = createString("userPhoto");

    public final StringPath userPw = createString("userPw");

    public final ComparablePath<Character> userState = createComparable("userState", Character.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

