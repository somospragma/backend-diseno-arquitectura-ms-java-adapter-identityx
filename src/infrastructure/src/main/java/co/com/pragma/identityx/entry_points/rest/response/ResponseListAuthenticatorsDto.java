package co.com.pragma.identityx.entry_points.rest.response;

import co.com.pragma.identityx.model.AuthenticatorModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListAuthenticatorsDto {
    List<AuthenticatorModel> data;
}
