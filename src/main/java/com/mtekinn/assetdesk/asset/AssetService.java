package com.mtekinn.assetdesk.asset;

import com.mtekinn.assetdesk.asset.dto.AssetResponse;
import com.mtekinn.assetdesk.asset.dto.CreateAssetRequest;
import com.mtekinn.assetdesk.asset.dto.UpdateAssetRequest;
import org.springframework.stereotype.Service;
import com.mtekinn.assetdesk.common.exception.ResourceNotFoundException;
import com.mtekinn.assetdesk.common.exception.ConflictException;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }


    private void validateUniqueFields(String assetCode, String serialNumber) {
        if (assetRepository.existsByAssetCode(assetCode)) {
            throw new ConflictException("Asset code already exists: " + assetCode);
        }

        if (serialNumber != null
                && !serialNumber.isBlank()
                && assetRepository.existsBySerialNumber(serialNumber)) {
            throw new ConflictException("Serial number already exists: " + serialNumber);
        }
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

    public List<AssetResponse> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssetResponse createAsset(CreateAssetRequest request) {
        validateUniqueFields(request.getAssetCode(), request.getSerialNumber());

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

        if (!asset.getAssetCode().equals(request.getAssetCode())
                && assetRepository.existsByAssetCode(request.getAssetCode())) {
            throw new ConflictException("Asset code already exists: " + request.getAssetCode());
        }

        if (request.getSerialNumber() != null
                && !request.getSerialNumber().isBlank()
                && !request.getSerialNumber().equals(asset.getSerialNumber())
                && assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new ConflictException("Serial number already exists: " + request.getSerialNumber());
        }

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

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        assetRepository.delete(asset);
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