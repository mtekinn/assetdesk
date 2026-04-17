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

    public List<AssignmentResponse> getAssignments(Long userId, Long assetId, AssignmentFilterStatus status) {
        if (userId != null && !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        if (assetId != null && !assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Asset not found with id: " + assetId);
        }

        return assignmentRepository.findAll()
                .stream()
                .filter(assignment -> userId == null || assignment.getUser().getId().equals(userId))
                .filter(assignment -> assetId == null || assignment.getAsset().getId().equals(assetId))
                .filter(assignment -> {
                    if (status == null) {
                        return true;
                    }
                    return switch (status) {
                        case ACTIVE -> assignment.getReturnedAt() == null;
                        case RETURNED -> assignment.getReturnedAt() != null;
                    };
                })
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

    public AssignmentResponse returnAssignment(Long id) {
        AssetAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        if (assignment.getReturnedAt() != null) {
            throw new ConflictException("Assignment is already returned with id: " + id);
        }

        assignment.setReturnedAt(LocalDateTime.now());

        Asset asset = assignment.getAsset();
        asset.setStatus(AssetStatus.IN_STOCK);

        AssetAssignment updatedAssignment = assignmentRepository.save(assignment);
        assetRepository.save(asset);

        return mapToResponse(updatedAssignment);
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