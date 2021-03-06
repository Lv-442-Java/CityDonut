package com.softserve.ita.java442.cityDonut.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {

    private long id;
    private String firstName;
    private String lastName;
    private String role;

}
