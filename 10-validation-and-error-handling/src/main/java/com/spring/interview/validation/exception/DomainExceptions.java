package com.spring.interview.validation.exception;

public final class DomainExceptions {

    private DomainExceptions() {}

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class BusinessConflictException extends RuntimeException {
        public BusinessConflictException(String message) {
            super(message);
        }
    }
}
