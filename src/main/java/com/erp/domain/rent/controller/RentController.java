package com.erp.domain.rent.controller;

import com.erp.domain.branch.dto.request.BranchSearchRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rent")
public class RentController {
    @GetMapping("/time")
    public String showRentTimePage() {
        return "rent/rent-time-selection";
    }

    @GetMapping("/branches")
    public String showBranchSelectionPage(@ModelAttribute("searchRequest") BranchSearchRequest request) {
        return "rent/branch-selection";
    }
}
