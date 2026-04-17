package com.mtekinn.assetdesk.assignment;

import com.mtekinn.assetdesk.asset.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<AssetAssignment, Long> {

    Optional<AssetAssignment> findByAssetAndReturnedAtIsNull(Asset asset);

    List<AssetAssignment> findByUserId(Long userId);

    List<AssetAssignment> findByAssetId(Long assetId);
}