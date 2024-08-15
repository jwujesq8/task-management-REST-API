package com.test.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private String description;
    private String httpStatusDesc;
}
