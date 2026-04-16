package com.mtekinn.assetdesk.asset.dto;

import com.mtekinn.assetdesk.asset.AssetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAssetRequest {

    @NotBlank(message = "Asset code is required")
    private String assetCode;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Asset type is required")
    private String assetType;

    private String brand;
    private String model;
    private String serialNumber;

    @NotNull(message = "Status is required")
    private AssetStatus status;
}
