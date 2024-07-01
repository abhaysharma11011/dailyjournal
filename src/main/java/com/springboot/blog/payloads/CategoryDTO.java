package com.springboot.blog.payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Integer categoryId;

    @NotBlank
    @Size(min=4, message = "minimum size of title is 4")
    private String categoryTitle;

    @Size(min=10, message = "minimum size of description is 4")
    private String categoryDescription;
}
