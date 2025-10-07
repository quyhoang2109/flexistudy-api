package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.dto.PageResponse;
import com.quyhoang.flexistudy.dto.request.JobCreationRequest;
import com.quyhoang.flexistudy.dto.request.JobUpdateRequest;
import com.quyhoang.flexistudy.dto.response.JobResponse;
import com.quyhoang.flexistudy.entity.Company;
import com.quyhoang.flexistudy.entity.Job;
import com.quyhoang.flexistudy.entity.JobRequiredSkill;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.JobMapper;
import com.quyhoang.flexistudy.repository.CompanyRepository;
import com.quyhoang.flexistudy.repository.JobRepository;
import com.quyhoang.flexistudy.repository.SkillRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {
    JobRepository jobRepository;
    CompanyRepository companyRepository;
    SkillRepository skillRepository;
    JobMapper jobMapper;

    @Transactional
    public JobResponse createJob(JobCreationRequest req) {

        Company c = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));

        Job job = jobMapper.toJob(req);
        job.setCompany(c);
        job.setRequiredSkills(new ArrayList<>());

        if (req.getSkillIds() != null && !req.getSkillIds().isEmpty()) {
            var skills = skillRepository.findAllById(req.getSkillIds());
            for (var s : skills) {
                job.getRequiredSkills().add(
                        JobRequiredSkill.builder()
                                .job(job)
                                .skill(s)
                                .minLevel(1)
                                .weight(new BigDecimal("1.000"))
                                .build()
                );
            }
        }

        Job saved = jobRepository.save(job);
        return jobMapper.toJobResponse(saved);
    }




    public PageResponse<JobResponse> getAllJobs(int page, int size) {
        Sort sort = Sort.by("postedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Job> jobPage = jobRepository.findAll(pageable);

        List<JobResponse> jobResponses = jobPage.getContent()
                .stream()
                .map(jobMapper::toJobResponse)
                .toList();

        return PageResponse.<JobResponse>builder()
                .currentPage(jobPage.getNumber() + 1)   // base 1 để khớp FE
                .totalPages(jobPage.getTotalPages())
                .pageSize(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .data(jobResponses)
                .build();
    }

    public JobResponse getJobById(String id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));
        return jobMapper.toJobResponse(job);
    }

    @Transactional
    public JobResponse updateJob(String id, JobUpdateRequest request) {
        // Tìm job cần update
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_FOUND));

        //  Cập nhật các trường cơ bản
        jobMapper.updateJob(job, request);

        // Nếu có danh sách skill mới thì cập nhật
        if (request.getSkillIds() != null) {
            // Xóa hết các yêu cầu kỹ năng cũ
            job.getRequiredSkills().clear();

            // Lấy skill theo ID
            var skills = skillRepository.findAllById(request.getSkillIds());

            // (Optional) Kiểm tra số lượng có khớp không
            if (skills.size() != request.getSkillIds().size()) {
                throw new AppException(ErrorCode.SKILL_NOT_FOUND);
            }

            // Thêm lại danh sách kỹ năng yêu cầu mới
            for (var s : skills) {
                job.getRequiredSkills().add(
                        JobRequiredSkill.builder()
                                .job(job)
                                .skill(s)
                                .minLevel(1) // TODO: Cho chỉnh từ request nếu cần
                                .weight(new BigDecimal("1.000"))
                                .build()
                );
            }
        }

        // Lưu job một lần duy nhất
        Job updatedJob = jobRepository.save(job);

        //Trả về response
        return jobMapper.toJobResponse(updatedJob);
    }

    public void deleteJob(String id) {
        if (!jobRepository.existsById(id)) {
            throw new AppException(ErrorCode.JOB_NOT_FOUND);
        }
        jobRepository.deleteById(id);
    }
}
