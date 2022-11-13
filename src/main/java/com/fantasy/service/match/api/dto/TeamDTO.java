package com.fantasy.service.match.api.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class TeamDTO {
    @NonNull
    private Long id;
    private String name;

    public TeamDTO(Long id, String name){
        this.id = id;
        this.name = name;
    }

}
