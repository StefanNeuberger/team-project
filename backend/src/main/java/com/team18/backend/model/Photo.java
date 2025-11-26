package com.team18.backend.model;

import com.team18.backend.model.enums.OwnerType;
import org.springframework.data.annotation.Id;

public class Photo {

    @Id
    private String url;

    private OwnerType ownerType;

    private String ownerId;

    public Photo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
