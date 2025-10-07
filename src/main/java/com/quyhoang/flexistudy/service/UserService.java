package com.quyhoang.flexistudy.service;

import com.quyhoang.flexistudy.constant.PredefinedRole;
import com.quyhoang.flexistudy.dto.PageResponse;
import com.quyhoang.flexistudy.dto.request.UserCreationRequest;
import com.quyhoang.flexistudy.dto.request.UserUpdateRequest;
import com.quyhoang.flexistudy.dto.response.UserResponse;
import com.quyhoang.flexistudy.entity.Role;
import com.quyhoang.flexistudy.entity.User;
import com.quyhoang.flexistudy.exception.AppException;
import com.quyhoang.flexistudy.exception.ErrorCode;
import com.quyhoang.flexistudy.mapper.UserMapper;
import com.quyhoang.flexistudy.repository.RoleRepository;
import com.quyhoang.flexistudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Value("${app.file.storage-dir}")
    @NonFinal
    String storageDir;

    @Value("${app.file.download-prefix}")
    @NonFinal
    String urlPrefix;

    public UserResponse createUser(UserCreationRequest request) {

        //map data into user
        User user = userMapper.toUser(request);

        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(int page, int size, String search, String username, String email) {
        log.info("get all users: page={}, size={}, search={}, username={}, email={}", page, size, search, username, email);

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> userPage;

        // Ưu tiên: nếu username hoặc email được truyền riêng → lọc tương ứng
        if (username != null && !username.isBlank()) {
            userPage = userRepository.findByUsernameContainingIgnoreCase(username, pageable);
        } else if (email != null && !email.isBlank()) {
            userPage = userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (search != null && !search.isBlank()) {
            // Lọc theo search chung (username hoặc email)
            userPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            // Không có filter → lấy tất cả
            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .currentPage(userPage.getNumber() + 1)
                .totalPages(userPage.getTotalPages())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .data(userResponses)
                .build();
    }




    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Áp dụng mapper để cập nhật các trường thông thường
        userMapper.updateUser(user, request);

        // ✅ Chỉ update password nếu client có gửi lên
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Cập nhật roles (nếu request.getRoles() là List<String> hoặc List<UUID>)
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        // Lưu lại thay đổi
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    public String uploadAvatar(String userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        try {
            // 1) Tạo thư mục lưu trữ avatar
            Path uploadPath = Paths.get(storageDir, "avatars");
            Files.createDirectories(uploadPath);

            // 2) Tạo tên file unique, tránh null filename
            String safeOriginal = (file.getOriginalFilename() == null) ? "unknown" : file.getOriginalFilename();
            String filename = userId + "_" + System.currentTimeMillis() + "_" + safeOriginal;
            Path filePath = uploadPath.resolve(filename);

            // 3) Ghi file mới (ghi đè nếu trùng tên)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 4) Xoá avatar cũ (nếu có), KHÔNG để lỗi xoá làm fail
            String oldUrl = user.getAvatarUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                try {
                    String oldPathPart = java.net.URI.create(oldUrl).getPath();
                    String oldFileName = Paths.get(oldPathPart).getFileName().toString();
                    Path oldFilePath = Paths.get(storageDir, "avatars", oldFileName);
                    Files.deleteIfExists(oldFilePath);
                } catch (Exception delEx) {
                    log.warn("Cannot delete old avatar for user {}: {}", userId, delEx.getMessage());
                }
            }

            // 5) Tạo URL public trả về (dùng urlPrefix giống như logo)
            String fileUrl = (urlPrefix.endsWith("/"))
                    ? (urlPrefix + "avatars/" + filename)
                    : (urlPrefix + "/avatars/" + filename);

            user.setAvatarUrl(fileUrl);
            userRepository.save(user);

            return fileUrl;
        } catch (Exception e) {
            log.error("Không thể upload avatar cho user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Không thể upload avatar: " + e.getMessage(), e);
        }
    }


}
