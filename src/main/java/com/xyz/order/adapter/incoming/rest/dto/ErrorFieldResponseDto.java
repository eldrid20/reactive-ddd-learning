package com.xyz.order.adapter.incoming.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorFieldResponseDto extends ErrorResponseDto {
    private List<ErrorField> errorFields;
}
