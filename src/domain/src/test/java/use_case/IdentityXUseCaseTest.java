package use_case;

import co.com.pragma.identityx.driven_port.api.IdentityXApiPort;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IdentityxUseCaseTest {

    @Mock
    private IdentityXApiPort identityXApiPort;

    private IdentityxUseCase identityxUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        identityxUseCase = new IdentityxUseCase(identityXApiPort);
    }

    @Test
    void testGetOtp() {
        // Arrange
        String userId = "testUserId";
        Long otpTimeAlive = 90L;
        Application application = new Application();
        application.setApplicationId("testApplication");
        Policy policy = new Policy();
        policy.setPolicyId("policyId");
        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();

        when(identityXApiPort.createAuthRequestOTP(userId, application, policy,otpTimeAlive)).thenReturn(expectedOtpRequestModel);

        // Act
        OtpRequestModel result = identityxUseCase.getOtp(userId, application, policy,otpTimeAlive);

        // Assert
        assertEquals(expectedOtpRequestModel, result);
    }

    @Test
    void testValidateOtp() {
        // Arrange
        String authenticationRequestId = "testRequestId";
        String submittedAuthenticationCode = "123456";
        String userId = "45664654";
        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();

        when(identityXApiPort.validateAuthRequestOTP(authenticationRequestId, submittedAuthenticationCode,userId)).thenReturn(expectedOtpRequestModel);

        // Act
        OtpRequestModel result = identityxUseCase.validateOtp(authenticationRequestId, submittedAuthenticationCode,userId);

        // Assert
        assertEquals(expectedOtpRequestModel, result);
    }

    @Test
    void testGetUser() {
        // Arrange
        String userId = "testUserId";
        UserRequestModel expectedUserRequestModel = new UserRequestModel();

        when(identityXApiPort.getUser(userId)).thenReturn(expectedUserRequestModel);

        // Act
        UserRequestModel result = identityxUseCase.getUser(userId);

        // Assert
        assertEquals(expectedUserRequestModel, result);
    }

    @Test
    void testCreateUser() {
        // Arrange
        String userId = "testUserId";
        UserRequestModel expectedUserRequestModel = new UserRequestModel();

        when(identityXApiPort.createUser(userId)).thenReturn(expectedUserRequestModel);

        // Act
        UserRequestModel result = identityxUseCase.createUser(userId);

        // Assert
        assertEquals(expectedUserRequestModel, result);
    }

    @Test
    void testCreateRegistration()  {
        // Arrange
        String userId = "testUserId";
        Application application = new Application();
        application.setApplicationId("testApplication");
        RegistrationModel expectedRegistrationModel = new RegistrationModel();

        when(identityXApiPort.registration(userId,application)).thenReturn(expectedRegistrationModel);

        // Act
        RegistrationModel result = identityxUseCase.createRegistration(userId,application);

        // Assert
        assertEquals(expectedRegistrationModel, result);
    }

    @Test
    void testCreateRegistrationChallenge() {
        // Arrange
        String userId = "testUserId";
        RegistrationChallengeModel expectedRegistrationChallengeModel = new RegistrationChallengeModel();
        Application application = new Application();
        application.setApplicationId("testApplication");
        Policy policy = new Policy();
        policy.setPolicyId("policyId");

        when(identityXApiPort.registrationChallenge(userId,application,policy)).thenReturn(expectedRegistrationChallengeModel);

        // Act
        RegistrationChallengeModel result = identityxUseCase.createRegistrationChallenge(userId,application,policy);

        // Assert
        assertEquals(expectedRegistrationChallengeModel, result);
    }

    @Test
    void testGetApplication() {
        // Arrange
        String appId = "appId";
        ApplicationRequestModel expectedApplicationRequestModel = new ApplicationRequestModel();

        when(identityXApiPort.findApplication(appId)).thenReturn(expectedApplicationRequestModel);

        // Act
        ApplicationRequestModel result = identityxUseCase.getApplicationById(appId);

        // Assert
        assertEquals(expectedApplicationRequestModel, result);
    }

    @Test
    void testGetPolicy() {
        // Arrange
        String policyId = "policyId";
        PolicyRequestModel expectedPolicyRequestModel = new PolicyRequestModel();

        when(identityXApiPort.findPolicy(policyId)).thenReturn(expectedPolicyRequestModel);

        // Act
        PolicyRequestModel result = identityxUseCase.getPolicyById(policyId);

        // Assert
        assertEquals(expectedPolicyRequestModel, result);
    }

    @Test
    void createAuthRequest() {
        // Arrange
        String userId = "testUserId";
        AuthenticationRequestModel expectedAuthenticationRequestModel = new AuthenticationRequestModel();
        Application application = new Application();
        application.setApplicationId("testApplication");
        Policy policy = new Policy();
        policy.setPolicyId("policyId");
        when(identityXApiPort.createAuthRequest(userId,application,policy,"desc","type")).thenReturn(expectedAuthenticationRequestModel);

        // Act
        AuthenticationRequestModel result = identityxUseCase.createAuthRequest(userId,application,policy,"desc","type");

        // Assert
        assertEquals(expectedAuthenticationRequestModel, result);
    }

    @Test
    void updateAuthRequest() {
        // Arrange
        String authenticationRequestId = "authenticationRequestId";
        AuthenticationRequestModel expectedAuthenticationRequestModel = new AuthenticationRequestModel();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setAuthenticationRequestId(authenticationRequestId);
        authenticationRequest.setSubmittedAuthenticationCode("code");

        when(identityXApiPort.updateAuthRequest(authenticationRequestId,authenticationRequest)).thenReturn(expectedAuthenticationRequestModel);

        // Act
        AuthenticationRequestModel result = identityxUseCase.updateAuthRequest(authenticationRequestId,authenticationRequest);

        // Assert
        assertEquals(expectedAuthenticationRequestModel, result);
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

        when(identityXApiPort.getListAuthenticators(userId,applicationId)).thenReturn(List.of(authenticatorModel));

        // Act
        List<AuthenticatorModel> result = identityxUseCase.getListAuthenticators(userId,applicationId);

        // Assert
        assertEquals(List.of(authenticatorModel), result);
    }

    @Test
    void testDeleteAuthenticators() {

        // Arrange
        String authenticatorId = "authenticatorId";

        // Act
        identityxUseCase.deleteAuthenticator(authenticatorId);

        verify(identityXApiPort, times(1)).deleteAuthenticator(authenticatorId);

    }
}