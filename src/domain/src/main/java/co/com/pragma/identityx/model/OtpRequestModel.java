package co.com.pragma.identityx.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequestModel {
    private String code;
    private String message;
    private AuthRequestOTPModel data;
}
