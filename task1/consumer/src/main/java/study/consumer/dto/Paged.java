package study.consumer.dto;

public record Paged<T>(T content, int totalPages) {
}