package com.fantasy.service.match.api.request;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class GetTeamRequest {
    private Boolean winnersOnly;
}
