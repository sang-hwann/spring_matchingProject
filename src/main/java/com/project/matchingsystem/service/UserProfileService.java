package com.project.matchingsystem.service;

import com.project.matchingsystem.domain.User;
import com.project.matchingsystem.dto.response.ResponseStatusDto;
import com.project.matchingsystem.exception.ErrorCode;
import com.project.matchingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserRepository userRepository;

    @Value("${profile.default.image.path}")
    private String defaultProfileImagePath;

    @Value("${profile.image.dir}")
    private String imageDir;

    public Resource downloadUserProfileImage(Long userId) throws MalformedURLException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );

        String path = "file:";
        if (user.getImagePath().equals(defaultProfileImagePath)) {
            path += defaultProfileImagePath;
        } else {
            path += getFullPath(user.getImagePath());
        }
        return new UrlResource(path);
    }

    @Transactional
    public ResponseStatusDto uploadUserProfileImage(MultipartFile image, Long userId) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.EMPTY_FILE.getMessage());
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
        );
        String originalFilename = image.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        String fullPath;
        do {
            fullPath = getFullPath(storeFileName);
        } while (userRepository.existsByImagePath(fullPath));

        image.transferTo(new File(fullPath));

        user.updateImage(storeFileName);
        return new ResponseStatusDto(HttpStatus.OK.toString(), "프로필 업로드 성공");
    }

    private String getFullPath(String filename) {
        return imageDir + filename;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
