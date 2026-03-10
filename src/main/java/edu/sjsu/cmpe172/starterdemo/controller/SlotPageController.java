package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SlotPageController {

    private final AppointmentService appointmentService;

    public SlotPageController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/slots")
    public String slots(Model model) {
        model.addAttribute("slots", appointmentService.listAvailableSlots());
        return "slots";
    }
}
