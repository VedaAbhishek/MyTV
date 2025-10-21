package edu.iu.p566.MyTV.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import edu.iu.p566.MyTV.model.Schedule;

@Repository
public class VideoRepository {
    private final JdbcTemplate jdbcTemplate;

    public VideoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Schedule> rowMapper = (rs, rowNum) -> {
        Schedule v = new Schedule();
        v.setId(rs.getLong("id"));
        v.setYoutubeLink(rs.getString("youtube_link"));
        v.setDurationInSeconds(rs.getInt("duration_seconds"));

        v.setScheduledDate(rs.getString("scheduled_date"));
        v.setScheduledTime(rs.getString("scheduled_time"));
        return v;
    };

    public List<Schedule> findAll() {
        return jdbcTemplate.query("SELECT * FROM videos ORDER BY id ASC", rowMapper);
    }

    public void save(Schedule schedule) {
        String sql = "INSERT INTO videos (youtube_link, duration_seconds, scheduled_date, scheduled_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getYoutubeLink());
            ps.setInt(2, schedule.getDurationInSeconds());
            ps.setString(3, schedule.getScheduledDate());
            ps.setString(4, schedule.getScheduledTime());
            return ps;
        });
    }

    public void update(Schedule schedule) {
        jdbcTemplate.update(
                "UPDATE videos SET youtube_link = ?, duration_seconds = ?, scheduled_date = ?, scheduled_time = ? WHERE id = ?",
                schedule.getYoutubeLink(),
                schedule.getDurationInSeconds(),
                schedule.getScheduledDate(),
                schedule.getScheduledTime(),
                schedule.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM videos WHERE id = ?", id);
    }
}
