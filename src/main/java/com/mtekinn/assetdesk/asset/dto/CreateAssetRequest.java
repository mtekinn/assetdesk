package com.mtekinn.assetdesk.asset.dto;

import com.mtekinn.assetdesk.asset.AssetStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAssetRequest {

    private String assetCode;
    private String name;
    private String assetType;
    private String brand;
    private String model;
    private String serialNumber;
    private AssetStatus status;
}