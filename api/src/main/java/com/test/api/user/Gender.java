package com.test.api.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
//    @NotBlank
//    @NotEmpty
//    @Max(value = 3, message = "Id must be in range 1-3")
//    @Min(value = 1, message = "Id must be in range 1-3")
    private Integer id;

//    @NotBlank
//    @NotEmpty
//    @Pattern(regexp = "^(male|female|none)$", message = "Gender may be male|female|none only")
    private String name;

}
