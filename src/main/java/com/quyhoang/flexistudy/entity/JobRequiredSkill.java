package com.quyhoang.flexistudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(
        name = "job_required_skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "skill_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobRequiredSkill {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    Job job;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    Skill skill;

    @Column(name = "min_level", nullable = false)
    int minLevel;

    @Column(name = "weight", nullable = false, precision = 4, scale = 3)
    java.math.BigDecimal weight;
}

