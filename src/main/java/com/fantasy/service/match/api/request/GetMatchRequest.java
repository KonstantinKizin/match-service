package com.fantasy.service.match.api.request;

import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class GetMatchRequest {
    private Long teamID;
    private Boolean upcomingOnly;

    public static GetMatchRequest defaultRequest() {
        return new GetMatchRequest();
    }
}
