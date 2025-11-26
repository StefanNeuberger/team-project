package com.team18.backend.mapper;

import com.team18.backend.model.Photo;
import com.team18.backend.dto.PhotoDTO;
import org.springframework.stereotype.Component;

@Component
public class PhotoMapper {

    public PhotoDTO toDto(Photo photo) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setUrl(photo.getUrl());
        photoDTO.setOwnerType(photo.getOwnerType());
        photoDTO.setOwnerId(photo.getOwnerId());

        return photoDTO;
    }
}
