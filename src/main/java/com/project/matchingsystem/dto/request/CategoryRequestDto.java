package com.project.matchingsystem.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    @NotBlank
    private String categoryName;

}
