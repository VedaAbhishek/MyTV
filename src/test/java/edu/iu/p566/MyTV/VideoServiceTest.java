package edu.iu.p566.MyTV;

import edu.iu.p566.MyTV.model.Schedule;
import edu.iu.p566.MyTV.repository.VideoRepository;
import edu.iu.p566.MyTV.service.VideoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    private VideoRepository repo;
    private VideoService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(VideoRepository.class);
        service = new VideoService(repo);
    }

    @Test
    void testConvertVideoLink_watchUrl() {
        String url = "https://www.youtube.com/watch?v=c5LeLdbK_-A";
        String expected = "https://www.youtube.com/embed/c5LeLdbK_-A";
        assertEquals(expected, service.convertVideoLink(url));
    }

    @Test
    void testConvertVideoLink_shortUrl() {
        String url = "https://www.youtube.com/watch?v=7ufMcAqIv2U&t=2s";
        String expected = "https://www.youtube.com/embed/7ufMcAqIv2U&t=2s";
        assertEquals(expected, service.convertVideoLink(url));
    }

    @Test
    void testHasOverlap() {
        Schedule s1 = new Schedule();
        s1.setId(1L);
        s1.setScheduledDate("2025-11-04");
        s1.setScheduledTime("14:00:00");
        s1.setDurationInSeconds(1800);

        Schedule s2 = new Schedule();
        s2.setId(2L);
        s2.setScheduledDate("2025-11-04");
        s2.setScheduledTime("14:15:00");
        s2.setDurationInSeconds(1800);

        when(repo.findAll()).thenReturn(List.of(s1));

        assertThrows(IllegalArgumentException.class, () -> service.saveVideos(s2));
    }

    @Test
    void testSaveVideo_convertsLinkBeforeSaving() {
        Schedule s = new Schedule();
        s.setYoutubeLink("https://www.youtube.com/watch?v=7ufMcAqIv2U&t=2s");
        s.setScheduledDate("2025-11-04");
        s.setScheduledTime("10:00:00");
        s.setDurationInSeconds(120);

        when(repo.findAll()).thenReturn(List.of());
        service.saveVideos(s);

        verify(repo).save(argThat(v -> v.getYoutubeLink().equals("https://www.youtube.com/embed/7ufMcAqIv2U&t=2s")));
    }

    @Test
    void testUpdateVideo_callsRepositoryUpdate() {
        Schedule s = new Schedule();
        s.setId(1L);
        s.setYoutubeLink("https://www.youtube.com/watch?v=7ufMcAqIv2U&t=2s");
        s.setScheduledDate("2025-10-21");
        s.setScheduledTime("10:00:00");
        s.setDurationInSeconds(120);

        when(repo.findAll()).thenReturn(List.of());

        service.updateVideo(s);

        verify(repo).update(any(Schedule.class));
    }

    @Test
    void testPlaybackLogic_basedOnTime() {
        // Simulate your frontend playback rule: if current time within scheduled window
        Schedule s = new Schedule();
        s.setId(1L);
        s.setScheduledDate("2025-10-21");
        s.setScheduledTime(LocalTime.now().minusMinutes(5).toString()); // started 5 mins ago
        s.setDurationInSeconds(1800); // 30 mins
        s.setYoutubeLink("https://www.youtube.com/embed/test");

        List<Schedule> list = List.of(s);
        when(repo.findAll()).thenReturn(list);

        List<Schedule> all = service.getAllVideos();
        Schedule current = all.stream()
                .filter(v -> {
                    LocalTime start = LocalTime.parse(v.getScheduledTime());
                    LocalTime end = start.plusSeconds(v.getDurationInSeconds());
                    LocalTime now = LocalTime.now();
                    return now.isAfter(start) && now.isBefore(end);
                })
                .findFirst().orElse(null);

        assertNotNull(current, "Should find an active video");
        assertEquals("https://www.youtube.com/embed/test", current.getYoutubeLink());
    }
}
