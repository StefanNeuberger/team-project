package com.team18.backend.service.impl;

import com.team18.backend.model.Photo;
import com.team18.backend.model.enums.OwnerType;
import com.team18.backend.repository.PhotoRepository;
import com.team18.backend.service.PhotoService;
import com.team18.backend.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;
    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(StorageService storageService, PhotoRepository photoRepository) {
        this.storageService = storageService;
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo uploadPhoto(MultipartFile file, OwnerType ownerType, String ownerId) {
        String photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, ownerType, photoId);

        Photo photo = new Photo(url);
        photo.setOwnerType(ownerType);
        photo.setOwnerId(ownerId);

        return photoRepository.save(photo);
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return photoRepository.getPhotoByUrl(id)
                .flatMap(photo -> getPhotoAsResource(photo.getUrl(), photo.getOwnerType()));
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String filename, OwnerType ownerType) {
        return storageService.loadAsResource(filename, ownerType);
    }

    @Override
    public List<String> getAllPhotoUrls(OwnerType ownerType, String ownerId) {
        return photoRepository.getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId)
                .stream()
                .map(Photo::getUrl)
                .filter(url -> url != null && !url.trim().isEmpty())
                .filter(url -> storageService.loadAsResource(url, ownerType).isPresent()) // Используем существующий метод
                .toList();
    }
}
