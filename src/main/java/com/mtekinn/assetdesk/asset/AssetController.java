package com.mtekinn.assetdesk.asset;

import com.mtekinn.assetdesk.asset.dto.AssetResponse;
import com.mtekinn.assetdesk.asset.dto.CreateAssetRequest;
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

    @PostMapping
    public AssetResponse createAsset(@Valid @RequestBody CreateAssetRequest request) {
        return assetService.createAsset(request);
    }
}