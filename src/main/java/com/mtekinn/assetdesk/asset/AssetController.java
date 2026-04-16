package com.mtekinn.assetdesk.asset;

import com.mtekinn.assetdesk.asset.dto.AssetResponse;
import com.mtekinn.assetdesk.asset.dto.CreateAssetRequest;
import com.mtekinn.assetdesk.asset.dto.UpdateAssetRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<AssetResponse> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{id}")
    public AssetResponse getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }

    @PostMapping
    public AssetResponse createAsset(@Valid @RequestBody CreateAssetRequest request) {
        return assetService.createAsset(request);
    }

    @PutMapping("/{id}")
    public AssetResponse updateAsset(@PathVariable Long id,
                                     @Valid @RequestBody UpdateAssetRequest request) {
        return assetService.updateAsset(id, request);
    }
}