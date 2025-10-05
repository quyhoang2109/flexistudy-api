package com.quyhoang.flexistudy.entity;

import jakarta.persistence.*;

@Entity
public class JobShift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

}
