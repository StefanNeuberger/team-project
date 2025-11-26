package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.exception.StorageException;
import com.team18.backend.model.Photo;
import com.team18.backend.model.enums.OwnerType;
import com.team18.backend.repository.PhotoRepository;
import com.team18.backend.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhotoService photoService;

    @Autowired
    private PhotoRepository photoRepository;

    @BeforeEach
    void setUp() {
        photoRepository.deleteAll();
    }

    @Test
    void uploadPhoto_whenValid_shouldReturnPhotoJson() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "abc".getBytes()
        );

        Photo returnedPhoto = new Photo("url123.jpg");
        returnedPhoto.setOwnerType(OwnerType.ITEM);
        returnedPhoto.setOwnerId("42");

        when(photoService.uploadPhoto(any(), eq(OwnerType.ITEM), eq("42")))
                .thenReturn(returnedPhoto);

        mockMvc.perform(
                        multipart("/api/photos/item/42")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("url123.jpg"))
                .andExpect(jsonPath("$.ownerType").value("ITEM"))
                .andExpect(jsonPath("$.ownerId").value("42"));

        verify(photoService, times(1))
                .uploadPhoto(any(), eq(OwnerType.ITEM), eq("42"));
    }

    @Test
    void uploadPhoto_whenStorageFails_shouldReturn500() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "data".getBytes());

        when(photoService.uploadPhoto(any(), eq(OwnerType.ITEM), eq("999")))
                .thenThrow(new StorageException("Disk full or something"));

        mockMvc.perform(multipart("/api/photos/item/999")
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unable to save or retrieve resources at this time"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void uploadPhoto_whenInvalidOwnerType_shouldReturn400() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "abc".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/photos/invalid/42")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getAllPhotos_whenValid_shouldReturnPhotoDtoList() throws Exception {
        when(photoService.getAllPhotoUrls(OwnerType.ITEM, "42"))
                .thenReturn(List.of("url1.jpg", "url2.png"));

        mockMvc.perform(
                        get("/api/photos/item/42")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].url").value("url1.jpg"))
                .andExpect(jsonPath("$[0].ownerType").value("ITEM"))
                .andExpect(jsonPath("$[0].ownerId").value("42"))
                .andExpect(jsonPath("$[1].url").value("url2.png"))
                .andExpect(jsonPath("$[1].ownerType").value("ITEM"))
                .andExpect(jsonPath("$[1].ownerId").value("42"));

        verify(photoService, times(1))
                .getAllPhotoUrls(OwnerType.ITEM, "42");
    }

    @Test
    void getPhoto_whenExists_shouldReturnResource() throws Exception {
        byte[] data = "image-binary-data".getBytes();
        Resource resource = new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "test.jpg";
            }
        };

        when(photoService.getPhotoAsResource("test.jpg"))
                .thenReturn(Optional.of(resource));

        mockMvc.perform(get("/api/photos/test.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline"))
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        verify(photoService, times(1))
                .getPhotoAsResource("test.jpg");
    }

    @Test
    void getPhoto_whenNotExists_shouldReturn404() throws Exception {
        when(photoService.getPhotoAsResource("missing.jpg"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/photos/missing.jpg"))
                .andExpect(status().isNotFound());

        verify(photoService, times(1))
                .getPhotoAsResource("missing.jpg");
    }

}
