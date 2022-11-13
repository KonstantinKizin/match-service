package com.fantasy.service.match.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchSearchObject {
    private Long teamID;
    private LocalDateTime startAfter;

    @JsonIgnore
    public boolean hasFilter() {
        return teamID != null || startAfter != null;
    }

    @JsonIgnore
    public boolean isUpcomingOnly() {
        return startAfter != null;
    }


}
