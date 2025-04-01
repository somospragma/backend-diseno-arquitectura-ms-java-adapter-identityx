package co.com.pragma.identityx.entry_points.rest.dto;

import com.daon.identityx.rest.model.pojo.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRegistrationDto {
    private String userId;
    private Application application;
}
