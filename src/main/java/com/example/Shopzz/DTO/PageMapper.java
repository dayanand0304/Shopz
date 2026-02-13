package com.example.Shopzz.DTO;

import com.example.Shopzz.DTO.Response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PageMapper {

    public static <T, R>PageResponse<R> toPageResponse(Page<T> page, Function<T,R> mapper){

        List<R> content=page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()

        );
    }
}
