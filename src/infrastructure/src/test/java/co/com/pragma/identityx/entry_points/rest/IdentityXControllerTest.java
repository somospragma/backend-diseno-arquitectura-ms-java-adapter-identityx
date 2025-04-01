package co.com.pragma.identityx.entry_points.rest;

import co.com.pragma.identityx.constants.ServiceConstants;
import co.com.pragma.identityx.entry_points.rest.controller.IdentityXController;
import co.com.pragma.identityx.entry_points.rest.dto.*;
import co.com.pragma.identityx.entry_points.rest.response.ResponseDeleteAuthenticatorDto;
import co.com.pragma.identityx.entry_points.rest.response.ResponseListAuthenticatorsDto;
import co.com.pragma.identityx.model.*;
import co.com.pragma.identityx.use_case.IdentityxUseCase;
import com.daon.identityx.rest.model.pojo.Application;
import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import com.daon.identityx.rest.model.pojo.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IdentityXControllerTest {

    @Mock
    private IdentityxUseCase identityXUseCase;

    private IdentityXController identityXController;

    private static final String CHANNEL_ID = "131";
    private static final String APPLICATION = "Portal PN";
    private static final String TIMESTAMP = "2019-07-04T11:24:10";
    private static final String TRANSACTION_ID = "a840be35-d11a-480f-8924-8c9ebd5a6e77";
    private static final String TERMINAL_ID = "127.0.0.1";
    private static final String TOKEN_CLIENT_ID = "41574603_1";
    private static final String TOKEN_USER_ID = "U_123_123";
    private static final String SESSION_ID = "12345";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        identityXController = new IdentityXController(identityXUseCase);
    }

    @Test
    void testGenerateOTP(){
        // Arrange
        String userId = "testUserId";
        Application application = new Application();
        application.setApplicationId("testApplicationId");
        Long otpTimeAlive = 90L;
        Policy policy = new Policy();
        policy.setPolicyId("id");

        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();
        GenerateOtpDto payload = new GenerateOtpDto();
        payload.setUserId(userId);
        payload.setApplication(application);
        payload.setPolicy(policy);
        payload.setOtpTimeAlive(otpTimeAlive);

        when(identityXUseCase.getOtp(userId, application, policy,otpTimeAlive)).thenReturn(expectedOtpRequestModel);

        // Act
        ResponseEntity<OtpRequestModel> response = identityXController.generateOTP(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOtpRequestModel, response.getBody());
    }

    @Test
    void testValidateOTP() {
        // Arrange
        String authenticationRequestId = "testRequestId";
        String submittedAuthenticationCode = "123456";
        String userId = "465454654";
        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();
        Map<String, Object> payload = new HashMap<>();
        payload.put(ServiceConstants.AUTH_REQUEST_ID, authenticationRequestId);
        payload.put(ServiceConstants.SUBMIT_AUTH_REQUEST_CODE, submittedAuthenticationCode);
        payload.put(ServiceConstants.USER_ID, userId);

        when(identityXUseCase.validateOtp(authenticationRequestId, submittedAuthenticationCode,userId)).thenReturn(expectedOtpRequestModel);

        // Act
        ResponseEntity<OtpRequestModel> response = identityXController.validateOTP(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOtpRequestModel, response.getBody());
    }

    @Test
    void testGetUser() {
        // Arrange
        String userId = "testUserId";
        UserRequestModel expectedUserRequestModel = new UserRequestModel();

        when(identityXUseCase.getUser(userId)).thenReturn(expectedUserRequestModel);

        // Act
        ResponseEntity<UserRequestModel> response = identityXController.getUser(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserRequestModel, response.getBody());
    }

    @Test
    void testCreateUser() {
        // Arrange
        String userId = "testUserId";
        UserRequestModel expectedUserRequestModel = new UserRequestModel();
        Map<String, Object> payload = new HashMap<>();
        payload.put(ServiceConstants.USER_ID, userId);

        when(identityXUseCase.createUser(userId)).thenReturn(expectedUserRequestModel);

        // Act
        ResponseEntity<UserRequestModel> response = identityXController.createUser(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserRequestModel, response.getBody());
    }

    @Test
    void testCreateRegistration(){
        // Arrange
        String userId = "testUserId";
        RegistrationModel expectedRegistrationRequestModel = new RegistrationModel();
        Application application = new Application();
        CreateRegistrationDto createRegistrationDto = new CreateRegistrationDto();
        createRegistrationDto.setUserId(userId);
        createRegistrationDto.setApplication(application);

        when(identityXUseCase.createRegistration(userId,application)).thenReturn(expectedRegistrationRequestModel);

        // Act
        ResponseEntity<RegistrationModel> response = identityXController.createRegistration(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,createRegistrationDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRegistrationRequestModel, response.getBody());
    }

    @Test
    void testCreateRegistrationChallenge() {
        // Arrange
        String userId = "testUserId";
        RegistrationChallengeModel expectedRegistrationChallengeRequestModel = new RegistrationChallengeModel();
        Application application = new Application();
        Policy policy = new Policy();
        CreateRegistrationChallengeDto createRegistrationChallengeDto = new CreateRegistrationChallengeDto();
        createRegistrationChallengeDto.setUserId(userId);
        createRegistrationChallengeDto.setApplication(application);
        createRegistrationChallengeDto.setPolicy(policy);

        when(identityXUseCase.createRegistrationChallenge(userId,application,policy)).thenReturn(expectedRegistrationChallengeRequestModel);

        // Act
        ResponseEntity<RegistrationChallengeModel> response = identityXController.createRegistrationChallenge(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,createRegistrationChallengeDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRegistrationChallengeRequestModel, response.getBody());
    }

    @Test
    void testUpdateRegistrationChallenge() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put(ServiceConstants.REG_CHALLENGE_ID, "testRegChallengeId");
        payload.put(ServiceConstants.FIDO_REGISTRATION_RESPONSE, "testFidoRegistrationResponse");

        RegistrationChallengeModel expectedModel = new RegistrationChallengeModel();
        when(identityXUseCase.updateRegistrationChallenge("testRegChallengeId", "testFidoRegistrationResponse"))
                .thenReturn(expectedModel);

        // Act
        ResponseEntity<RegistrationChallengeModel> response = identityXController.updateRegistrationChallenge(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedModel, response.getBody());

        verify(identityXUseCase, times(1))
                .updateRegistrationChallenge("testRegChallengeId", "testFidoRegistrationResponse");
    }

    @Test
    void testGetApplication(){
        // Arrange
        String appId = "appId";
        ApplicationRequestModel expectedApplicationRequestModel = new ApplicationRequestModel();

        when(identityXUseCase.getApplicationById(appId)).thenReturn(expectedApplicationRequestModel);

        // Act
        ResponseEntity<ApplicationRequestModel> response = identityXController.getApplicationById(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,appId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedApplicationRequestModel, response.getBody());
    }

    @Test
    void testGetPolicy(){
        // Arrange
        String policyId = "policyId";
        PolicyRequestModel expectedPolicyRequestModel = new PolicyRequestModel();

        when(identityXUseCase.getPolicyById(policyId)).thenReturn(expectedPolicyRequestModel);

        // Act
        ResponseEntity<PolicyRequestModel> response = identityXController.getPolicyById(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,policyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPolicyRequestModel, response.getBody());
    }

    @Test
    void testCreateAuthenticationRequest() {
        // Arrange
        String userId = "testUserId";
        Application application = new Application();
        application.setApplicationId("testApplicationId");
        Policy policy = new Policy();
        policy.setPolicyId("id");
        String description = "otp ";
        String type = "OP";

        AuthenticationRequestModel expectedAuthenticationRequestModel = new AuthenticationRequestModel();
        CreateAuthenticationRequestDto payload = new CreateAuthenticationRequestDto();
        payload.setUserId(userId);
        payload.setApplication(application);
        payload.setPolicy(policy);
        payload.setDescription(description);
        payload.setType(type);

        when(identityXUseCase.createAuthRequest(userId, application, policy,description,type)).thenReturn(expectedAuthenticationRequestModel);

        // Act
        ResponseEntity<AuthenticationRequestModel> response = identityXController.createAuthenticationRequest(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuthenticationRequestModel, response.getBody());
    }

    @Test
    void testUpdateAuthenticationRequest() {
        // Arrange
        String authenticationRequestId = "authenticationRequestId";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setSubmittedAuthenticationCode("123456");

        AuthenticationRequestModel expectedAuthenticationRequestModel = new AuthenticationRequestModel();
        UpdateAuthenticationRequestDto payload = new UpdateAuthenticationRequestDto();
        payload.setAuthenticationRequestId(authenticationRequestId);
        payload.setAuthenticationRequest(authenticationRequest);

        when(identityXUseCase.updateAuthRequest(authenticationRequestId, authenticationRequest)).thenReturn(expectedAuthenticationRequestModel);

        // Act
        ResponseEntity<AuthenticationRequestModel> response = identityXController.updateAuthenticationRequest(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuthenticationRequestModel, response.getBody());
    }

    @Test
    void testListAuthenticators() {
        // Arrange
        String userId = "testUserId";
        String applicationId = "testApplication";

        AuthenticatorModel authenticatorModel = new AuthenticatorModel();
        authenticatorModel.setAuthenticatorId("authenticatorId");
        authenticatorModel.setRegChallengeId("regChallengeId");
        authenticatorModel.setDeviceCorrelationId("deviceId");

        when(identityXUseCase.getListAuthenticators(userId,applicationId)).thenReturn(List.of(authenticatorModel));

        // Act
        ResponseEntity<ResponseListAuthenticatorsDto> response = identityXController.listAuthenticators(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,userId,applicationId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(authenticatorModel), Objects.requireNonNull(response.getBody()).getData());
    }

    @Test
    void testDeleteAuthenticators() {
        // Arrange
        String authenticatorId = "authenticatorId";

        // Act
        ResponseEntity<ResponseDeleteAuthenticatorDto> response = identityXController.deleteAuthenticator(CHANNEL_ID,APPLICATION,TIMESTAMP,TRANSACTION_ID,TERMINAL_ID,TOKEN_CLIENT_ID,TOKEN_USER_ID,SESSION_ID,authenticatorId);

        verify(identityXUseCase, times(1)).deleteAuthenticator(authenticatorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
