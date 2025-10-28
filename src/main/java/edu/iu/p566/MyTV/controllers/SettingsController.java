package edu.iu.p566.MyTV.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.iu.p566.MyTV.model.Schedule;
import edu.iu.p566.MyTV.service.VideoService;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final VideoService videoService;

    public SettingsController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping()
    public String settingsPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("videos", videoService.getAllVideos());
        if (error != null)
            model.addAttribute("error", error);
        return "settings";
    }

    @PostMapping("/add")
    public String addVideo(@ModelAttribute Schedule schedule, Model model) {
        try {
            videoService.saveVideos(schedule);
            return "redirect:/settings";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("videos", videoService.getAllVideos());
            return "settings";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteVideo(@PathVariable Long id) {
        videoService.deleteVideos(id);
        return "redirect:/settings";
    }

    @PostMapping("/update")
    public String updateVideo(@ModelAttribute Schedule schedule, Model model) {
        try {
            videoService.updateVideo(schedule);
            return "redirect:/settings";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("videos", videoService.getAllVideos());
            return "settings";
        }
    }
}
