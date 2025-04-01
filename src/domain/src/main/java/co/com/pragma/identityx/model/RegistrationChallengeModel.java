package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.RegistrationChallenge;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationChallengeModel {
    private String code;
    private String message;
    private RegistrationChallenge data;
}
