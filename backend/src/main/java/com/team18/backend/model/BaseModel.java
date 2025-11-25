package com.team18.backend.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import java.time.Instant;

/**
 * Abstract base class for persistable entities with automatic timestamp management.
 * <p>
 * This class implements Spring Data's {@link Persistable} interface and provides
 * basic functionality for all entities, including:
 * <ul>
 *   <li>Unique string-based ID management</li>
 *   <li>Automatic capture of creation timestamp</li>
 *   <li>Automatic update of modification timestamp</li>
 *   <li>Distinction between new and existing entities</li>
 * </ul>
 *
 * @author Team18
 * @version 1.0
 */
public class BaseModel implements Persistable<String>{

    /**
     * The unique identifier of this entity.
     */
    @Id
    private String id;

    /**
     * The timestamp when this entity was created.
     * Automatically set by Spring Data when the entity is first persisted.
     */
    @CreatedDate
    private Instant createdDate = null;

    /**
     * The timestamp of the last modification of this entity.
     * Automatically set by Spring Data on every update.
     */
    @LastModifiedDate
    private Instant lastModifiedDate = null;

    /**
     * Constructs a new BaseModel with the specified ID.
     *
     * @param id the unique identifier for this entity
     */
    public BaseModel(String id) {
        this.id = id;
    }

    /**
     * Determines whether this entity is new or already exists in the database.
     * <p>
     * An entity is considered new if its creation date has not been set yet.
     * This method is used by Spring Data to decide whether to perform an INSERT
     * or UPDATE operation.
     *
     * @return {@code true} if the entity is new, {@code false} otherwise
     */
    @Override
    public boolean isNew() {
        return this.createdDate == null;
    }

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the entity's ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the timestamp when this entity was created.
     *
     * @return the creation timestamp, or {@code null} if not yet set
     */
    public Instant getCreatedDate() {
        return createdDate;
    }

    /**
     * Returns the timestamp of the last modification of this entity.
     *
     * @return the last modification timestamp, or {@code null} if not yet set
     */
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Manually sets the last modification timestamp.
     * <p>
     * Note: This value is typically set automatically by Spring Data
     * and should not be modified manually.
     *
     * @param lastModifiedDate the new modification timestamp
     */
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Manually sets the creation timestamp.
     * <p>
     * Note: This value is typically set automatically by Spring Data
     * and should not be modified manually.
     *
     * @param createdDate the new creation timestamp
     */
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

}
