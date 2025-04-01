package co.com.pragma.identityx.entry_points.rest.dto;

import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAuthenticationRequestDto {
    private String authenticationRequestId;
    private AuthenticationRequest authenticationRequest;
}
