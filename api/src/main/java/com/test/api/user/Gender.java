package com.test.api.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "gender")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gender {

    @Id
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[1-3]{1}$", message = "Id must be in range 1-3")
    private Integer id;

    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^male|female|none$", message = "Gender name must be male, female or none")
    private String name;

}
