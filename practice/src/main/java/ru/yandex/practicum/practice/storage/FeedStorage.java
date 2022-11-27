package ru.yandex.practicum.practice.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.practice.model.Event;
import ru.yandex.practicum.practice.model.EventTypes;
import ru.yandex.practicum.practice.model.OperationTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
@Component
public class FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    public FeedStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Event addEvent(Event event) {
        String sqlQuery = "INSERT INTO feed (created_at, client_id, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setLong(1, event.getTimestamp());
            statement.setLong(2, event.getClientId());
            statement.setString(3, event.getEventType().toString());
            statement.setString(4, event.getOperation().toString());
            statement.setLong(5, event.getEntityId());
            return statement;
        }, keyHolder);
        event.setEventId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return event;
    }



    public List<Event> getByClientId(Long userId) {
        String sqlQuery = "SELECT f.id, " +
                "f.created_at, " +
                "f.client_id, " +
                "f.event_type, " +
                "f.operation, " +
                "f.entity_id " +
                "FROM feed AS f " +
                "WHERE f.client_id = ?;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeEvent(rs), userId);
    }

    public List<Event> getAllEvents() {
        String sqlQuery = "SELECT f.id, " +
                "f.created_at, " +
                "f.client_id, " +
                "f.event_type, " +
                "f.operation, " +
                "f.entity_id " +
                "FROM feed AS f ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeEvent(rs));
    }




    private Event makeEvent(ResultSet rs) throws SQLException {
        Long timestamp = rs.getLong("created_at");
        Long clientId = rs.getLong("client_id");
        EventTypes eventType = EventTypes.valueOf(rs.getString("event_type"));
        OperationTypes operation = OperationTypes.valueOf(rs.getString("operation"));
        Long eventId = rs.getLong("id");
        Long entityId = rs.getLong("entity_id");

        return new Event(timestamp, clientId, eventType, operation, eventId, Math.toIntExact(entityId));
    }




}
