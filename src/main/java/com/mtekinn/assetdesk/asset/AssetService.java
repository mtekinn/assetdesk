package com.mtekinn.assetdesk.asset;

import com.mtekinn.assetdesk.asset.dto.CreateAssetRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset createAsset(CreateAssetRequest request) {
        Asset asset = new Asset();
        asset.setAssetCode(request.getAssetCode());
        asset.setName(request.getName());
        asset.setAssetType(request.getAssetType());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setStatus(request.getStatus());

        return assetRepository.save(asset);
    }
}