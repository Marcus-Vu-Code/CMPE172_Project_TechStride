package edu.sjsu.cmpe172.starterdemo.controller;

import edu.sjsu.cmpe172.starterdemo.dto.BookingResponse;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        model.addAttribute("slots", appointmentService.listAvailableSlots());
        return "book";
    }

    @PostMapping("/book")
    public String submitBooking(@RequestParam long candidateId,
                                @RequestParam long slotId,
                                @RequestParam long serviceId,
                                RedirectAttributes redirectAttributes) {
        BookingResponse response = appointmentService.bookAndNotify(candidateId, slotId, serviceId);
        redirectAttributes.addFlashAttribute("success", response.isBooked());
        redirectAttributes.addFlashAttribute("slotId", slotId);
        redirectAttributes.addFlashAttribute("notificationSent", response.isNotificationSent());
        redirectAttributes.addFlashAttribute("notificationStatus", response.getNotificationStatus());
        redirectAttributes.addFlashAttribute("notificationTrackingId", response.getNotificationTrackingId());
        redirectAttributes.addFlashAttribute("message", response.getMessage());
        return "redirect:/confirmation";
    }
}
