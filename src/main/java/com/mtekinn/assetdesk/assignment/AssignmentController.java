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
    public List<AssignmentResponse> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @PostMapping
    public AssignmentResponse createAssignment(@Valid @RequestBody CreateAssignmentRequest request) {
        return assignmentService.createAssignment(request);
    }
}