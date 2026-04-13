package com.mtekinn.assetdesk;


import com.mtekinn.assetdesk.asset.Asset;
import com.mtekinn.assetdesk.asset.AssetRepository;
import com.mtekinn.assetdesk.asset.AssetStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final AssetRepository assetRepository;

    public DataLoader(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public void run(String... args) {
        if (assetRepository.count() == 0) {
            Asset asset = new Asset();
            asset.setAssetCode("ASSET-001");
            asset.setName("Office Laptop");
            asset.setAssetType("LAPTOP");
            asset.setBrand("Lenovo");
            asset.setModel("ThinkPad E14");
            asset.setSerialNumber("SN-123456");
            asset.setStatus(AssetStatus.IN_STOCK);

            assetRepository.save(asset);
        }
    }
}
