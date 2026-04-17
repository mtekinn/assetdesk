package com.mtekinn.assetdesk.asset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsByAssetCode(String assetCode);

    boolean existsBySerialNumber(String serialNumber);

    List<Asset> findByStatus(AssetStatus status);
}