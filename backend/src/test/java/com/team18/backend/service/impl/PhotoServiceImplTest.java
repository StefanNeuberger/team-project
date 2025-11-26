package com.team18.backend.service.impl;

import com.team18.backend.model.Photo;
import com.team18.backend.model.enums.OwnerType;
import com.team18.backend.repository.PhotoRepository;
import com.team18.backend.service.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    @Mock
    private StorageService storageService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Test
    void uploadPhoto_shouldCallStorageServiceAndReturnPhoto() {
        OwnerType ownerType = OwnerType.ITEM;
        String ownerId = "123";
        String storedFilename = "generated.png";

        when(storageService.store(any(), eq(ownerType), anyString()))
                .thenReturn(storedFilename);

        when(photoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Photo result = photoService.uploadPhoto(file, ownerType, ownerId);

        assertNotNull(result);
        assertEquals(storedFilename, result.getUrl());
        assertEquals(ownerType, result.getOwnerType());
        assertEquals(ownerId, result.getOwnerId());

        ArgumentCaptor<String> filenameCaptor = ArgumentCaptor.forClass(String.class);

        verify(storageService, times(1))
                .store(eq(file), eq(ownerType), filenameCaptor.capture());

        assertEquals(36, filenameCaptor.getValue().length());
    }

    @Test
    void getPhotoAsResource_shouldReturnResourceWhenExists() {
        OwnerType ownerType = OwnerType.ITEM;
        String photoId = "photo123";
        Resource mockResource = mock(Resource.class);

        when(storageService.loadAsResource(photoId, ownerType))
                .thenReturn(Optional.of(mockResource));

        Optional<Resource> result = photoService.getPhotoAsResource(photoId, ownerType);

        assertTrue(result.isPresent());
        assertEquals(mockResource, result.get());

        verify(storageService, times(1)).loadAsResource(photoId, ownerType);
    }

    @Test
    void getPhotoAsResource_shouldReturnEmptyWhenResourceNotFound() {
        OwnerType ownerType = OwnerType.ITEM;
        String photoId = "nonexistent";

        when(storageService.loadAsResource(photoId, ownerType))
                .thenReturn(Optional.empty());

        Optional<Resource> result = photoService.getPhotoAsResource(photoId, ownerType);

        assertTrue(result.isEmpty());

        verify(storageService, times(1)).loadAsResource(photoId, ownerType);
    }

    @Test
    void getPhotoAsResource_withId_shouldReturnResourceWhenPhotoExists() {
        String photoId = "photo123";
        OwnerType ownerType = OwnerType.ITEM;
        String filename = "stored-file.png";
        Resource mockResource = mock(Resource.class);
        Photo mockPhoto = new Photo(filename);
        mockPhoto.setOwnerType(ownerType);

        when(photoRepository.getPhotoByUrl(photoId))
                .thenReturn(Optional.of(mockPhoto));
        when(storageService.loadAsResource(filename, ownerType))
                .thenReturn(Optional.of(mockResource));

        Optional<Resource> result = photoService.getPhotoAsResource(photoId);

        assertTrue(result.isPresent());
        assertEquals(mockResource, result.get());

        verify(photoRepository, times(1)).getPhotoByUrl(photoId);
        verify(storageService, times(1)).loadAsResource(filename, ownerType);
    }

    @Test
    void getPhotoAsResource_withId_shouldReturnEmptyWhenPhotoNotFound() {
        String photoId = "nonexistent";

        when(photoRepository.getPhotoByUrl(photoId))
                .thenReturn(Optional.empty());

        Optional<Resource> result = photoService.getPhotoAsResource(photoId);

        assertTrue(result.isEmpty());

        verify(photoRepository, times(1)).getPhotoByUrl(photoId);
        verify(storageService, never()).loadAsResource(anyString(), any(OwnerType.class));
    }

    @Test
    void getPhotoAsResource_withId_shouldReturnEmptyWhenResourceNotFound() {
        String photoId = "photo123";
        OwnerType ownerType = OwnerType.ITEM;
        String filename = "stored-file.png";
        Photo mockPhoto = new Photo(filename);
        mockPhoto.setOwnerType(ownerType);

        when(photoRepository.getPhotoByUrl(photoId))
                .thenReturn(Optional.of(mockPhoto));
        when(storageService.loadAsResource(filename, ownerType))
                .thenReturn(Optional.empty());

        Optional<Resource> result = photoService.getPhotoAsResource(photoId);

        assertTrue(result.isEmpty());

        verify(photoRepository, times(1)).getPhotoByUrl(photoId);
        verify(storageService, times(1)).loadAsResource(filename, ownerType);
    }

    @Test
    void getPhotoAsResource_withId_shouldHandleNullOwnerType() {
        String photoId = "photo123";
        String filename = "stored-file.png";
        Photo mockPhoto = new Photo(filename);
        mockPhoto.setOwnerType(null); // ownerType is null

        when(photoRepository.getPhotoByUrl(photoId))
                .thenReturn(Optional.of(mockPhoto));
        when(storageService.loadAsResource(filename, null))
                .thenReturn(Optional.empty());

        Optional<Resource> result = photoService.getPhotoAsResource(photoId);

        assertTrue(result.isEmpty());

        verify(photoRepository, times(1)).getPhotoByUrl(photoId);
        verify(storageService, times(1)).loadAsResource(filename, null);
    }

    @Test
    void getPhotoAsResource_withIdAndOwnerType_shouldHandleNullFilename() {
        OwnerType ownerType = OwnerType.ITEM;
        String photoId = null;

        when(storageService.loadAsResource(null, ownerType))
                .thenReturn(Optional.empty());

        Optional<Resource> result = photoService.getPhotoAsResource(photoId, ownerType);

        assertTrue(result.isEmpty());

        verify(storageService, times(1)).loadAsResource(null, ownerType);
    }

    @Test
    void getAllPhotoUrls_shouldReturnFilteredUrlsWhenPhotosExist() {
        OwnerType ownerType = OwnerType.ITEM;
        String ownerId = "123";

        List<Photo> mockPhotos = List.of(
                createPhoto("photo1.png"),
                createPhoto("photo2.png"),
                createPhoto("photo3.png"),
                createPhoto(null),
                createPhoto(""),
                createPhoto("   ")
        );

        when(photoRepository.getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId))
                .thenReturn(mockPhotos);

        when(storageService.loadAsResource("photo1.png", ownerType))
                .thenReturn(Optional.of(mock(Resource.class)));
        when(storageService.loadAsResource("photo2.png", ownerType))
                .thenReturn(Optional.of(mock(Resource.class)));
        when(storageService.loadAsResource("photo3.png", ownerType))
                .thenReturn(Optional.empty());

        List<String> result = photoService.getAllPhotoUrls(ownerType, ownerId);

        assertEquals(2, result.size());
        assertTrue(result.contains("photo1.png"));
        assertTrue(result.contains("photo2.png"));

        verify(photoRepository, times(1)).getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId);
        verify(storageService, times(1)).loadAsResource("photo1.png", ownerType);
        verify(storageService, times(1)).loadAsResource("photo2.png", ownerType);
        verify(storageService, times(1)).loadAsResource("photo3.png", ownerType);

        verify(storageService, never()).loadAsResource(null, ownerType);
        verify(storageService, never()).loadAsResource("", ownerType);
        verify(storageService, never()).loadAsResource("   ", ownerType);
    }

    @Test
    void getAllPhotoUrls_shouldReturnEmptyListWhenNoPhotosFound() {

        OwnerType ownerType = OwnerType.ITEM;
        String ownerId = "nonexistent";

        when(photoRepository.getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId))
                .thenReturn(List.of());


        List<String> result = photoService.getAllPhotoUrls(ownerType, ownerId);


        assertTrue(result.isEmpty());
        verify(photoRepository, times(1)).getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId);
        verify(storageService, never()).loadAsResource(anyString(), any(OwnerType.class));
    }

    @Test
    void getAllPhotoUrls_shouldReturnEmptyListWhenAllPhotosHaveInvalidUrls() {

        OwnerType ownerType = OwnerType.ITEM;
        String ownerId = "456";

        List<Photo> mockPhotos = List.of(
                createPhoto(null),
                createPhoto(""),
                createPhoto("   "),
                createPhoto("missing.png")
        );

        when(photoRepository.getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId))
                .thenReturn(mockPhotos);

        when(storageService.loadAsResource("missing.png", ownerType))
                .thenReturn(Optional.empty());


        List<String> result = photoService.getAllPhotoUrls(ownerType, ownerId);


        assertTrue(result.isEmpty());
        verify(photoRepository, times(1)).getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId);
        verify(storageService, times(1)).loadAsResource("missing.png", ownerType);
    }

    @Test
    void getAllPhotoUrls_shouldHandleNullOwnerType() {
        // Arrange
        OwnerType ownerType = null;
        String ownerId = "789";

        List<Photo> mockPhotos = List.of(
                createPhoto("photo1.png"),
                createPhoto("photo2.png")
        );

        when(photoRepository.getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId))
                .thenReturn(mockPhotos);

        when(storageService.loadAsResource("photo1.png", ownerType))
                .thenReturn(Optional.of(mock(Resource.class)));
        when(storageService.loadAsResource("photo2.png", ownerType))
                .thenReturn(Optional.of(mock(Resource.class)));


        List<String> result = photoService.getAllPhotoUrls(ownerType, ownerId);


        assertEquals(2, result.size());
        verify(photoRepository, times(1)).getPhotosByOwnerTypeAndOwnerId(ownerType, ownerId);
        verify(storageService, times(1)).loadAsResource("photo1.png", ownerType);
        verify(storageService, times(1)).loadAsResource("photo2.png", ownerType);
    }


    private Photo createPhoto(String url) {
        return new Photo(url);
    }
}
