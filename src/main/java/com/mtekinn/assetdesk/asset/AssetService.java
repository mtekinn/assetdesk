package com.mtekinn.assetdesk.asset;

import com.mtekinn.assetdesk.asset.dto.AssetResponse;
import com.mtekinn.assetdesk.asset.dto.CreateAssetRequest;
import com.mtekinn.assetdesk.asset.dto.UpdateAssetRequest;
import org.springframework.stereotype.Service;
import com.mtekinn.assetdesk.common.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssetResponse getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        return mapToResponse(asset);
    }

    public AssetResponse createAsset(CreateAssetRequest request) {
        Asset asset = new Asset();
        asset.setAssetCode(request.getAssetCode());
        asset.setName(request.getName());
        asset.setAssetType(request.getAssetType());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setStatus(request.getStatus());

        Asset savedAsset = assetRepository.save(asset);
        return mapToResponse(savedAsset);
    }

    public AssetResponse updateAsset(Long id, UpdateAssetRequest request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        asset.setAssetCode(request.getAssetCode());
        asset.setName(request.getName());
        asset.setAssetType(request.getAssetType());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setStatus(request.getStatus());

        Asset updatedAsset = assetRepository.save(asset);
        return mapToResponse(updatedAsset);
    }

    private AssetResponse mapToResponse(Asset asset) {
        AssetResponse response = new AssetResponse();
        response.setId(asset.getId());
        response.setAssetCode(asset.getAssetCode());
        response.setName(asset.getName());
        response.setAssetType(asset.getAssetType());
        response.setBrand(asset.getBrand());
        response.setModel(asset.getModel());
        response.setSerialNumber(asset.getSerialNumber());
        response.setStatus(asset.getStatus());

        return response;
    }
}