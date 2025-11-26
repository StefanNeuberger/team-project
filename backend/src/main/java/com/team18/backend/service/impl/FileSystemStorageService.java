package com.team18.backend.service.impl;

import com.team18.backend.exception.StorageException;
import com.team18.backend.model.enums.OwnerType;
import com.team18.backend.service.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${app.storage.location:backend/uploads}")
    private String storageLocation;

    @PostConstruct
    public void init() {
        Arrays.stream(OwnerType.values())
                .map(this::getRootLocation)
                .forEach(location -> {
                    try {
                        Files.createDirectories(Paths.get(location));
                    } catch (IOException e) {
                        throw new StorageException("Could not initialize storage location", e);
                    }
                });
    }

    @Override
    public String store(MultipartFile file, OwnerType ownerType, String suggestedFilename) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Cannot save an empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (extension.length() > 10 || extension.contains("/") || extension.contains("\\")) {
                extension = "";
            }
        }

        String safeFilename = UUID.randomUUID().toString() + extension;

        if (suggestedFilename != null && !suggestedFilename.isBlank()) {
            String sanitized = suggestedFilename
                    .replaceAll("[^a-zA-Z0-9_.-]", "_")
                    .replaceAll("(\\.{2,})", "")
                    .replaceAll("^[./\\\\]+", "")
                    .trim();

            if (sanitized.length() >= 3) {
                safeFilename = sanitized + extension;
            }
        }

        Path rootLocation = Paths.get(getRootLocation(ownerType));
        Path destinationFile = rootLocation.resolve(safeFilename).normalize();

        if (!destinationFile.toAbsolutePath().startsWith(rootLocation.toAbsolutePath())) {
            throw new StorageException("Invalid path detected");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + safeFilename, e);
        }

        return safeFilename;
    }


    @Override
    public Optional<Resource> loadAsResource(String fileName, OwnerType ownerType) {
        try {
            Path rootLocation = Paths.get(getRootLocation(ownerType));
            Path file = rootLocation.resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }


    private String getRootLocation(OwnerType ownerType) {
        return storageLocation + "/" + ownerType.toString().toLowerCase();
    }
}


