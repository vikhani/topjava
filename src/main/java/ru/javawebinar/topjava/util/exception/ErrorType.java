package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("app error"),
    DATA_NOT_FOUND("data not found"),
    DATA_ERROR("data error"),
    VALIDATION_ERROR("Ошибка проверки данных"),
    INVALID_DUPLICATE("Такое значение уже есть");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
