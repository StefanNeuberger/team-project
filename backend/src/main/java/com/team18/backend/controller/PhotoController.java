package com.team18.backend.controller;

import com.team18.backend.mapper.PhotoMapper;
import com.team18.backend.model.Photo;
import com.team18.backend.dto.PhotoDTO;
import com.team18.backend.model.enums.OwnerType;
import com.team18.backend.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/photos")
@Tag(name = "Photos", description = "upload and download operations for photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    public PhotoController(PhotoService photoService, PhotoMapper photoMapper) {
        this.photoService = photoService;
        this.photoMapper = photoMapper;
    }

    @PostMapping("/{ownerType}/{ownerId}")
    @Operation(summary = "Upload a photo", description = "Uploads a photo for the given owner type and owner ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Photo uploaded successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Photo.class))
    )
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    public Photo uploadPhoto(
            @Parameter(description = "case-insensitive")
            @PathVariable OwnerType ownerType,
            @PathVariable String ownerId,
            @RequestParam("file") @NotNull MultipartFile file
    ) {
        return photoService.uploadPhoto(file, ownerType, ownerId);
    }

    @GetMapping("/{ownerType}/{ownerId}")
    @Operation(summary = "Get all photos for owner", description = "Returns all photos associated with the given owner type and owner ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Photos retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PhotoDTO.class)))
    )
    @ApiResponse(responseCode = "404", description = "Owner not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(
            @Parameter(description = "case-insensitive")
            @PathVariable OwnerType ownerType,
            @PathVariable String ownerId
    ) {

        var photos = photoService.getAllPhotoUrls(ownerType, ownerId)
                .stream().map(url -> {
                    Photo photo = new Photo(url);
                    photo.setOwnerType(ownerType);
                    photo.setOwnerId(ownerId);
                    return photoMapper.toDto(photo);
                })
                .toList();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(photos);
    }

    @GetMapping(path = "/{id:.+}")
    @Operation(summary = "Get photo by ID", description = "Returns the photo file for the given photo ID.")
    @ApiResponse(
            responseCode = "200",
            description = "Photo retrieved successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @ApiResponse(responseCode = "404", description = "Photo not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    public ResponseEntity<Resource> getPhoto(@PathVariable String id) {

        return photoService.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(MediaTypeFactory
                                .getMediaType(photo)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }
}
