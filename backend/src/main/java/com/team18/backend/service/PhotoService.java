package com.team18.backend.service;

import com.team18.backend.model.Photo;
import com.team18.backend.model.enums.OwnerType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    Photo uploadPhoto(MultipartFile file, OwnerType ownerType, String ownerId);

    Optional<Resource> getPhotoAsResource(String id, OwnerType ownerType);

    Optional<Resource> getPhotoAsResource(String id);

    List<String> getAllPhotoUrls(OwnerType ownerType, String ownerId);
}
