package com.team18.backend.repository;

import com.team18.backend.model.Photo;
import com.team18.backend.model.enums.OwnerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {

    Optional<Photo> getPhotoByUrl(String url);

    Optional<Photo> getPhotoByOwnerTypeAndOwnerId(OwnerType ownerType, String ownerId);

    List<Photo> getPhotosByOwnerTypeAndOwnerId(OwnerType ownerType, String ownerId);
}
