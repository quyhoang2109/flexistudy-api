package com.quyhoang.flexistudy.repository;

import com.quyhoang.flexistudy.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job,String> {
    Page<Job> findByTitleContainingIgnoreCaseOrCompany_NameContainingIgnoreCase(
            String titleKeyword,
            String companyKeyword,
            Pageable pageable
    );
}
