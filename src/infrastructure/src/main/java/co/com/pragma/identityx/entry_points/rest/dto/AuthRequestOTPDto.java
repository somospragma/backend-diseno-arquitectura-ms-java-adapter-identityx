package co.com.pragma.identityx.entry_points.rest.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestOTPDto {
    private String fidoAuthenticationRequest;
    private String authenticationRequestId;
    private Date expiration;
    private Long availableRetries;
    private Long totalRetriesAllowed;
    private String authenticationCode;
    private Date created;
    private String status;
    private String verificationResult;
}
