package com.team18.backend.dto.shipmentlineitem;

import com.team18.backend.model.Item;
import com.team18.backend.model.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Schema(description = "ShipmentLineItem with all details")
public record ShipmentLineItemResponseDTO(
        @Schema(
                description = "Unique identifier",
                accessMode = Schema.AccessMode.READ_ONLY)

        String id,
        @DocumentReference
        @Schema(
                description = "Related shipment entity",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Shipment shipment,
        @DocumentReference
        @Schema(
                description = "Related item entity",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Item item,
        @Schema(
                description = "Expected item quantity",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer expectedQuantity,
        @Schema(
                description = "Received item quantity",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer receivedQuantity,
        @Schema(
                description = "Last modified timestamp",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Instant createdDate,
        @Schema(
                description = "Created timestamp",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Instant lastModifiedDate

) {
}
