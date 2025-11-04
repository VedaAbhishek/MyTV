package edu.iu.p566.MyTV.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.iu.p566.MyTV.model.Schedule;
import edu.iu.p566.MyTV.repository.VideoRepository;

@Service
public class VideoService {
    private final VideoRepository repo;

    public VideoService(VideoRepository repo) {
        this.repo = repo;
    }

    public List<Schedule> getAllVideos() {
        return repo.findAll();
    }

    public void saveVideos(Schedule schedule) {
        if (hasOverlap(schedule, false)) {
            throw new IllegalArgumentException("Video schedule overlaps with another existing video.");
        }
        schedule.setYoutubeLink(convertVideoLink(schedule.getYoutubeLink()));
        repo.save(schedule);
    }

    public void deleteVideos(Long id) {
        repo.deleteById(id);
    }

    public void updateVideo(Schedule schedule) {
        if (hasOverlap(schedule, true)) {
            throw new IllegalArgumentException("Video schedule overlaps with another existing video.");
        }
        schedule.setYoutubeLink(convertVideoLink(schedule.getYoutubeLink()));
        repo.update(schedule);
    }

    public String convertVideoLink(String url) {
        if (url == null)
            return url;
        if (url.contains("embed"))
            return url;
        if (url.contains("watch?v=")) {
            return url.replace("watch?v=", "embed/");
        } else if (url.contains("youtu.be/")) {
            return url.replace("youtu.be/", "youtube.com/embed/");
        }
        return url;
    }

    private boolean hasOverlap(Schedule newVideo, boolean isUpdate) {
        List<Schedule> allVidoes = repo.findAll();

        LocalTime start = LocalTime.parse(newVideo.getScheduledTime());
        LocalTime end = start.plusSeconds(newVideo.getDurationInSeconds());

        for (Schedule existing : allVidoes) {
            if (isUpdate && existing.getId().equals(newVideo.getId()))
                continue;

            if (!existing.getScheduledDate().equals(newVideo.getScheduledDate()))
                continue;

            LocalTime existingStart = LocalTime.parse(existing.getScheduledTime());
            LocalTime existingEnd = existingStart.plusSeconds(existing.getDurationInSeconds());

            boolean overlap = start.isBefore(existingEnd) && end.isAfter(existingStart);
            if (overlap)
                return true;
        }

        return false;
    }
}