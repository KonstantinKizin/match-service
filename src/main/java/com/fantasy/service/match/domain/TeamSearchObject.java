package com.fantasy.service.match.domain;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class TeamSearchObject {
    private Boolean winnersOnly;

    public boolean isWinnersOnly(){
        return Boolean.TRUE.equals(winnersOnly);
    }
}
