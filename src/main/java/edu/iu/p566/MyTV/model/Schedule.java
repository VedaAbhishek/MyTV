package edu.iu.p566.MyTV.model;

import lombok.Data;

@Data
public class Schedule {

    private Long id;
    private String youtubeLink;
    private int durationInSeconds;
    private String scheduledDate;
    private String scheduledTime;
}
