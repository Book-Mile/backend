package com.bookmile.backend.domain.group.dto.req;

import com.bookmile.backend.domain.group.entity.GroupStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupSearchRequestDto {

    private String isbn13;
    private GroupStatus status;
}
