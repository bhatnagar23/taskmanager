package com.taskmanager.controller;

import com.taskmanager.dto.DashboardResponse;
import com.taskmanager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // GET /api/dashboard/{projectId}
    @GetMapping("/{projectId}")
    public ResponseEntity<DashboardResponse> getDashboard(
            @PathVariable Long projectId) {

        DashboardResponse response = dashboardService
                .getDashboard(projectId);
        return ResponseEntity.ok(response);
    }
}
