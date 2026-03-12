package org.nazarius.dto;

public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
}