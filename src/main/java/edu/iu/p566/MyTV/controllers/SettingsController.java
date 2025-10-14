package edu.iu.p566.MyTV.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String settingsPage(Model model) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "settings";
    }

    @PostMapping("/add")
    public String addVideo(@ModelAttribute Schedule schedule) {
        videoService.saveVideos(schedule);
        return "redirect:/settings";
    }

    @PostMapping("/delete/{id}")
    public String deleteVideo(@PathVariable Long id) {
        videoService.deleteVideos(id);
        return "redirect:/settings";
    }

    @PostMapping("/update")
    public String updateVideo(@ModelAttribute Schedule schedule) {
        videoService.updateVideo(schedule);
        return "redirect:/settings"; 
    }
}
