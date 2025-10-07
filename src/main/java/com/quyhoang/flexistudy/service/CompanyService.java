package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.dto.PageResponse;
import com.quyhoang.flexistudy.dto.request.CompanyCreationRequest;
import com.quyhoang.flexistudy.dto.request.CompanyUpdateRequest;
import com.quyhoang.flexistudy.dto.response.CompanyResponse;
import com.quyhoang.flexistudy.entity.Company;
import com.quyhoang.flexistudy.entity.Job;
import com.quyhoang.flexistudy.entity.User;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.CompanyMapper;
import com.quyhoang.flexistudy.repository.CompanyRepository;
import com.quyhoang.flexistudy.repository.JobRepository;
import com.quyhoang.flexistudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    UserRepository userRepository;

    @Value("${app.file.storage-dir}")
    @NonFinal
    String storageDir;

    @Value("${app.file.download-prefix}")
    @NonFinal
    String urlPrefix;

    public CompanyResponse createCompany(CompanyCreationRequest request) {
        Company company = companyMapper.toCompany(request);
        company = companyRepository.save(company);
        return companyMapper.toCompanyResponse(company);
    }

    public PageResponse<CompanyResponse> getAllCompanies(int page, int size, String search) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Company> companyPage;

        if (search != null && !search.isBlank()) {
            companyPage = companyRepository.search(search, pageable);
        } else {
            companyPage = companyRepository.findAll(pageable);
        }

        List<CompanyResponse> companyResponses = companyPage.getContent()
                .stream()
                .map(companyMapper::toCompanyResponse)
                .toList();

        return PageResponse.<CompanyResponse>builder()
                .currentPage(companyPage.getNumber() + 1)
                .totalPages(companyPage.getTotalPages())
                .pageSize(companyPage.getSize())
                .totalElements(companyPage.getTotalElements())
                .data(companyResponses)
                .build();
    }


    public CompanyResponse getCompanyById(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));
        return companyMapper.toCompanyResponse(company);
    }

    public CompanyResponse updateCompany(String companyId, CompanyUpdateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));
        companyMapper.updateCompany(company, request);
        return companyMapper.toCompanyResponse(companyRepository.save(company));
    }

    public void deleteCompany(String companyId) {
        companyRepository.deleteById(companyId);
    }

    public List<Job> getJobsByCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));
        return company.getJobs(); // hoặc jobRepository.findByCompanyId(companyId)
    }

    public CompanyResponse assignRecruiter(String companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));

        User recruiter = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        recruiter.setCompany(company);
        userRepository.save(recruiter);

        return companyMapper.toCompanyResponse(company);
    }

    public String uploadCompanyLogo(String companyId, MultipartFile file) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_FOUND));

        try {
            // 1) Tạo thư mục lưu trữ
            Path uploadPath = Paths.get(storageDir, "company-logos");
            Files.createDirectories(uploadPath);

            // 2) Tạo tên file unique
            String safeOriginal = (file.getOriginalFilename() == null) ? "unknown" : file.getOriginalFilename();
            String filename = companyId + "_" + System.currentTimeMillis() + "_" + safeOriginal;
            Path filePath = uploadPath.resolve(filename);

            // 3) Ghi file mới (REPLACE_EXISTING cũng ok, nhưng tên đã unique)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4) Xoá logo cũ (nếu có) — KHÔNG để lỗi xoá làm hỏng tác vụ upload
            String oldUrl = company.getLogoUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                try {
                    // Lấy đường dẫn từ URL (không có query)
                    String oldPathPart = java.net.URI.create(oldUrl).getPath(); // vd: /static/company-logos/abc.png
                    String oldFileName = Paths.get(oldPathPart).getFileName().toString();
                    Path oldFilePath = Paths.get(storageDir, "company-logos", oldFileName);
                    Files.deleteIfExists(oldFilePath);
                } catch (Exception delEx) {
                    // chỉ cảnh báo, không fail
                    log.warn("Cannot delete old logo for company {}: {}", companyId, delEx.getMessage());
                }
            }
            String fileUrl = (urlPrefix.endsWith("/"))
                    ? (urlPrefix + "company-logos/" + filename)
                    : (urlPrefix + "/company-logos/" + filename);

            company.setLogoUrl(fileUrl);
            companyRepository.save(company);

            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException("Không thể upload logo công ty: " + e.getMessage(), e);
        }
    }

}
