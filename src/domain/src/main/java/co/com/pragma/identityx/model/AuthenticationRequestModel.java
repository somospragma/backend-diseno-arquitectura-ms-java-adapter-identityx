package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequestModel {
    private String code;
    private String message;
    private AuthenticationRequest data;
}
