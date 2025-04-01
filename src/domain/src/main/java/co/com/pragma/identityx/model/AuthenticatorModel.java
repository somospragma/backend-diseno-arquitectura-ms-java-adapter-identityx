package co.com.pragma.identityx.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatorModel {
    private String authenticatorId;
    private String deviceCorrelationId;
    private String regChallengeId;
    private String fidoDeregistrationRequest;
}
