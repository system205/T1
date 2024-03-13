package study.supplier.dto;

public record Paged<T>(T content, int totalPages) { }
