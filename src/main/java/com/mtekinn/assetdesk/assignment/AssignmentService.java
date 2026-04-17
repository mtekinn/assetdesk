package com.mtekinn.assetdesk.assignment;

import com.mtekinn.assetdesk.asset.Asset;
import com.mtekinn.assetdesk.asset.AssetRepository;
import com.mtekinn.assetdesk.asset.AssetStatus;
import com.mtekinn.assetdesk.assignment.dto.AssignmentResponse;
import com.mtekinn.assetdesk.assignment.dto.CreateAssignmentRequest;
import com.mtekinn.assetdesk.common.exception.ConflictException;
import com.mtekinn.assetdesk.common.exception.ResourceNotFoundException;
import com.mtekinn.assetdesk.user.User;
import com.mtekinn.assetdesk.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssetRepository assetRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
    }

    public List<AssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssignmentResponse createAssignment(CreateAssignmentRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + request.getAssetId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (assignmentRepository.findByAssetAndReturnedAtIsNull(asset).isPresent()) {
            throw new ConflictException("Asset is already assigned: " + asset.getAssetCode());
        }

        if (asset.getStatus() == AssetStatus.RETIRED) {
            throw new ConflictException("Retired asset cannot be assigned: " + asset.getAssetCode());
        }

        if (asset.getStatus() == AssetStatus.IN_REPAIR) {
            throw new ConflictException("Asset in repair cannot be assigned: " + asset.getAssetCode());
        }

        AssetAssignment assignment = new AssetAssignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setNote(request.getNote());

        asset.setStatus(AssetStatus.ASSIGNED);

        AssetAssignment savedAssignment = assignmentRepository.save(assignment);
        assetRepository.save(asset);

        return mapToResponse(savedAssignment);
    }

    private AssignmentResponse mapToResponse(AssetAssignment assignment) {
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());
        response.setAssetId(assignment.getAsset().getId());
        response.setAssetCode(assignment.getAsset().getAssetCode());
        response.setUserId(assignment.getUser().getId());
        response.setUserFullName(assignment.getUser().getFirstName() + " " + assignment.getUser().getLastName());
        response.setAssignedAt(assignment.getAssignedAt());
        response.setReturnedAt(assignment.getReturnedAt());
        response.setNote(assignment.getNote());

        return response;
    }
}