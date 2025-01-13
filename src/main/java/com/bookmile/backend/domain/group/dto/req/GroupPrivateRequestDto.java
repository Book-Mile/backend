package com.bookmile.backend.domain.group.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupPrivateRequestDto {
    private Long userId;
    private Boolean isOpen;
}