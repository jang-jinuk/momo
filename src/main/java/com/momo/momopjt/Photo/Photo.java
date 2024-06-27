package com.momo.momopjt.Photo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @Column(name = "photoUuid", nullable = false)
    private String photoUuid;

}