package com.team18.backend.dto;

import com.team18.backend.model.enums.OwnerType;

public class PhotoDTO {
    private String url;
    private OwnerType ownerType;
    private String ownerId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
