package com.quyhoang.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "file_mgmt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMgmt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    String id;

    @Column(name = "owner_id", nullable = false, length = 50)
    String ownerId;

    @Column(name = "content_type", nullable = false, length = 100)
    String contentType;

    @Column(name = "size", nullable = false)
    long size;

    @Column(name = "md5_checksum", length = 64)
    String md5Checksum;

    @Column(name = "path", nullable = false, length = 500)
    String path;
}
