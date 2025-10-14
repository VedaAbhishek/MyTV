package edu.iu.p566.MyTV.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.iu.p566.MyTV.service.VideoService;

@Controller
public class VideoController {
    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }

    @GetMapping("/vid")
    public String showVideos(Model model) {
        model.addAttribute("videos", service.getAllVideos());
        return "player";
    }
}
