package com.team18.backend.service;

import com.team18.backend.model.enums.OwnerType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface StorageService {

    String store(MultipartFile file, OwnerType owner, String filename);

    Optional<Resource> loadAsResource(String fileName, OwnerType ownerType);
}
