package com.ZerodaySolution.Billing.controller;

import com.ZerodaySolution.Billing.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashboaardData(){

    Map<String,Object> data = dashboardService.getDashboardData();
    return ResponseEntity.ok(data);
    }

}
