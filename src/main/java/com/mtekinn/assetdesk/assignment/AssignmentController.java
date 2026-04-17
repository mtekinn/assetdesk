package com.mtekinn.assetdesk.assignment;

import com.mtekinn.assetdesk.assignment.dto.AssignmentResponse;
import com.mtekinn.assetdesk.assignment.dto.CreateAssignmentRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<AssignmentResponse> getAssignments(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long assetId,
            @RequestParam(required = false) AssignmentFilterStatus status
    ) {
        return assignmentService.getAssignments(userId, assetId, status);
    }

    @PostMapping
    public AssignmentResponse createAssignment(@Valid @RequestBody CreateAssignmentRequest request) {
        return assignmentService.createAssignment(request);
    }

    @PostMapping("/{id}/return")
    public AssignmentResponse returnAssignment(@PathVariable Long id) {
        return assignmentService.returnAssignment(id);
    }
}