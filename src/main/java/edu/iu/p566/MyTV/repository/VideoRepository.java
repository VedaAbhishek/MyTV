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

    public int countVideos() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM videos", Integer.class);
    }

    private RowMapper<Schedule> rowMapper = (rs, rowNum) -> {
        Schedule v = new Schedule();
        v.setId(rs.getLong("id"));
        v.setYoutubeLink(rs.getString("youtube_link"));
        v.setDurationInSeconds(rs.getInt("duration_seconds"));
        return v;
    };

    public List<Schedule> findAll() {

        List<Schedule> x = jdbcTemplate.query("SELECT * FROM videos ORDER BY id ASC", rowMapper);
        return x;
    }

    public void save(Schedule schedule) {
        String sql = "INSERT INTO videos (youtube_link, duration_seconds) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getYoutubeLink());
            ps.setInt(2, schedule.getDurationInSeconds());
            return ps;
        });
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM videos WHERE id = ?", id);
    }

    public void update(Schedule schedule) {
        jdbcTemplate.update(
                "UPDATE videos SET youtube_link = ?, duration_seconds = ? WHERE id = ?",
                schedule.getYoutubeLink(), schedule.getDurationInSeconds(),
                schedule.getId());
    }
}
