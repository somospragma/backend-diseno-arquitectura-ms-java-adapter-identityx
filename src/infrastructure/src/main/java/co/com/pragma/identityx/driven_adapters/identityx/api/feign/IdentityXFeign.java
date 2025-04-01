package co.com.pragma.identityx.driven_adapters.identityx.api.feign;

import co.com.pragma.identityx.constants.ServiceConstants;
import com.daon.identityx.rest.model.pojo.*;
import com.identityx.auth.impl.MediaType;
import com.identityx.clientSDK.collections.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "IdentityXApi", url = ServiceConstants.IDENTITYX_HOST + ServiceConstants.IDENTITYX_TENANT,
configuration = FeignClientConfig.class)
public interface IdentityXFeign {

    @GetMapping(path = ServiceConstants.FIND_POLICY_PATH+"{policyId}")
    PolicyCollection findPolicy(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("policyId") String policyId
    );

    @GetMapping(path = ServiceConstants.FIND_APPLICATION_PATH+"{applicationId}")
    ApplicationCollection findApplication(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("applicationId") String applicationId
    );

    @GetMapping(path = ServiceConstants.FIND_USER_PATH+"{userId}")
    UserCollection getUser(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("userId") String userId
    );

    @PostMapping(path = ServiceConstants.USER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    User createUser(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody User user
    );

    @GetMapping(path = ServiceConstants.FIND_AUTH_REQUEST_ID_PATH+"{authenticationRequestId}")
    AuthenticationRequestCollection getAuthRequest(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("authenticationRequestId") String authenticationRequestId
    );

    @PostMapping(path = ServiceConstants.FIND_AUTH_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthenticationRequest createAuthenticationRequest(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AuthenticationRequest authenticationRequest
    );

    @PostMapping(path = ServiceConstants.FIND_AUTH_REQUEST_PATH+"{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthenticationRequest updateAuthenticationRequest(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") String id,
            @RequestBody AuthenticationRequest authenticationRequest
    );

    @PostMapping(path = ServiceConstants.ARCHIVE_AUTH_REQUEST_PATH+"{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthenticationRequest archiveAuthenticationRequest(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") String id,
            @PathVariable("authReqId") String authReqId
    );

    @GetMapping(path = ServiceConstants.USER_PATH+"{userId}/registrations")
    RegistrationCollection getRegistrationsByUser(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("userId") String userId
    );

    @PostMapping(path = ServiceConstants.FIND_REGISTRATION_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    Registration createRegistration(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Registration registration
    );

    @PostMapping(path = ServiceConstants.FIND_REGISTRATION_CHALLENGE_REQUEST_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    RegistrationChallenge createRegistrationChallenge(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody RegistrationChallenge registrationChallenge
    );

    @PostMapping(path = ServiceConstants.FIND_REGISTRATION_CHALLENGE_REQUEST_PATH+"{regChallengeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    RegistrationChallenge updateRegistrationChallenge(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("regChallengeId") String regChallengeId,
            @RequestBody RegistrationChallenge registrationChallenge
    );

    @GetMapping(path = ServiceConstants.FIND_REGISTRATION_CHALLENGE_REQUEST_PATH+"{regChallengeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    RegistrationChallenge getRegistrationChallenge(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("regChallengeId") String regChallengeId
    );

    @GetMapping(path = ServiceConstants.FIND_AUTHENTICATORS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    AuthenticatorCollection findAuthenticatorsByUserId(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("userId") String userId
    );

    @PostMapping(path = ServiceConstants.DELETE_AUTHENTICATORS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    Authenticator archiveAuthenticator(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("authenticatorId") String authenticatorId
    );

}
