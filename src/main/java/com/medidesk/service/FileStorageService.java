package com.medidesk.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".pdf", ".png", ".jpg", ".jpeg");

    private final Path uploadDirectory =
            Path.of("uploads").toAbsolutePath().normalize();

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Attachment must be 5 MB or smaller.");
        }

        String originalName =
                StringUtils.cleanPath(file.getOriginalFilename() == null
                        ? ""
                        : file.getOriginalFilename());

        int dotPosition = originalName.lastIndexOf('.');
        if (dotPosition < 0) {
            throw new IllegalArgumentException(
                    "Use a PDF, PNG, JPG, or JPEG attachment.");
        }

        String extension = originalName.substring(dotPosition)
                .toLowerCase(Locale.ROOT);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Use a PDF, PNG, JPG, or JPEG attachment.");
        }

        String storedName = UUID.randomUUID() + extension;
        Files.createDirectories(uploadDirectory);

        Path target = uploadDirectory.resolve(storedName).normalize();
        if (!target.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid attachment name.");
        }

        Files.copy(file.getInputStream(), target,
                StandardCopyOption.REPLACE_EXISTING);

        return storedName;
    }
}