package com.example.gradleAdminApi.model.network;

import java.time.LocalDateTime;

import com.example.gradleAdminApi.model.Pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Header<T> {
	
	private LocalDateTime transactionTime;
	
	// api Response Code
	private String resultCode;
	
	// api Additional Description
	private String description;

    @Valid
    private T data;
	
    private Pagination pagination;
	
    // OK
    @SuppressWarnings("unchecked")
	public static <T> Header<T> OK() {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .build();
    }

    // DATA OK
    @SuppressWarnings("unchecked")
	public static <T> Header<T> OK(T data) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .build();
    }
    
    // DATA OK
    @SuppressWarnings("unchecked")
	public static <T> Header<T> OK(T data, Pagination pagination) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .pagination(pagination)
                .build();
    }

    // ERROR
    @SuppressWarnings("unchecked")
	public static <T> Header<T> ERROR(String description) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .description(description)
                .build();
    }
 
}
