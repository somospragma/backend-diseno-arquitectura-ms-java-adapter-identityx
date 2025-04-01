package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.Registration;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationModel {
    private String code;
    private String message;
    private Registration data;
}
