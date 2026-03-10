package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentPageController {

    private final AppointmentService appointmentService;

    public AppointmentPageController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/book")
    public String showBookingForm(Model model) {
        // Provide available slots for a dropdown list
        model.addAttribute("slots", appointmentService.listAvailableSlots());
        return "book";
    }

    @PostMapping("/book")
    public String submitBooking(@RequestParam long candidateId,
                                @RequestParam long slotId,
                                @RequestParam long serviceId,
                                RedirectAttributes redirectAttributes) {
        boolean ok = appointmentService.book(candidateId, slotId, serviceId);
        redirectAttributes.addFlashAttribute("success", ok);
        redirectAttributes.addFlashAttribute("slotId", slotId);
        return "redirect:/confirmation";
    }
}
