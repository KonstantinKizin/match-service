package com.fantasy.service.match.dao.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long entityID, Class<?> clazz) {
        super(String.format("Entity %s by id %s not found", clazz, entityID));
    }
}
