package com.test.api.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private Integer id;

    @NotBlank
    @NotEmpty
    private String name;

}
