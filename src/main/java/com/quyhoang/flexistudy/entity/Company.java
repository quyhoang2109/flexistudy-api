package com.quyhoang.flexistudy.entity;

import com.quyhoang.flexistudy.exception.CompanyVerifiedStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false, length = 150, unique = true)  // ✅ đặt unique cho tên công ty nếu cần
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    String logoUrl;

    String website;

    int memberNumber;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    List<User> recruiters = new ArrayList<>();
}
