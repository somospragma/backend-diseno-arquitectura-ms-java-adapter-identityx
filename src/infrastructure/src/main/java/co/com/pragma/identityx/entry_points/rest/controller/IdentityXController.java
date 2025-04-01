package co.com.pragma.identityx.entry_points.rest.controller;

import co.com.pragma.identityx.entry_points.rest.dto.*;
import co.com.pragma.identityx.entry_points.rest.response.ResponseDeleteAuthenticatorDto;
import co.com.pragma.identityx.entry_points.rest.response.ResponseListAuthenticatorsDto;
import co.com.pragma.identityx.exceptionshandler.dto.ResponseErrorBodyDto;
import co.com.pragma.identityx.helpers.ResponseHelper;
import co.com.pragma.identityx.use_case.IdentityxUseCase;
import com.daon.identityx.rest.model.pojo.Application;
import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import com.daon.identityx.rest.model.pojo.Policy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import co.com.pragma.identityx.model.*;
import co.com.pragma.identityx.constants.ServiceConstants;

import java.util.Map;

import static co.com.pragma.identityx.constants.ServiceConstants.*;

@RestController
@RequiredArgsConstructor
public class IdentityXController {
    private final IdentityxUseCase identityXUseCase;

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.otp.generate}")
    @Operation(summary = OPERATION_GENERATE_OTP)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = OtpRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<OtpRequestModel> generateOTP(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody @Validated GenerateOtpDto payload)
    {
        String userId = payload.getUserId();
        Application applicationData = payload.getApplication();
        Policy policy = payload.getPolicy();
        Long otpTimeAlive= payload.getOtpTimeAlive();
        return ResponseHelper.successResponse(identityXUseCase.getOtp(userId,applicationData,policy,otpTimeAlive));

    }

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.otp.validate}")
    @Operation(summary = OPERATION_VALIDATE_OTP)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = OtpRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<OtpRequestModel> validateOTP(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody Map<String, Object> payload) {

        String authenticationRequestId = (String)payload.get(ServiceConstants.AUTH_REQUEST_ID);
        String submittedAuthenticationCode = (String)payload.get(ServiceConstants.SUBMIT_AUTH_REQUEST_CODE);
        String userId = (String)payload.get(USER_ID);

        return ResponseHelper.successResponse(identityXUseCase.validateOtp(authenticationRequestId,submittedAuthenticationCode,userId));

    }

    @GetMapping(path = "${co.itau.route.api.adapter.identityx.get.user}")
    @Operation(summary = OPERATION_GET_USER)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = UserRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<UserRequestModel> getUser(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestHeader(name = USER_ID) String userId) {
        return ResponseHelper.successResponse(identityXUseCase.getUser(userId));
    }

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.create.user}")
    @Operation(summary = OPERATION_CREATE_USER)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = UserRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<UserRequestModel> createUser(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody Map<String, Object> payload) {

        String userId = (String)payload.get(ServiceConstants.USER_ID);
        return ResponseHelper.successResponse(identityXUseCase.createUser(userId));
    }

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.create.registration}")
    @Operation(summary = OPERATION_CREATE_REGISTRATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = RegistrationModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<RegistrationModel> createRegistration(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody CreateRegistrationDto payload) {

        String userId = payload.getUserId();
        Application applicationData = payload.getApplication();
        return ResponseHelper.successResponse(identityXUseCase.createRegistration(userId,applicationData));
    }

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.registration-challenge.create}")
    @Operation(summary = OPERATION_CREATE_REGISTRATION_CHALLENGE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = RegistrationChallengeModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<RegistrationChallengeModel> createRegistrationChallenge(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody CreateRegistrationChallengeDto payload) {

        String userId = payload.getUserId();
        Application applicationData = payload.getApplication();
        Policy policy = payload.getPolicy();

        return ResponseHelper.successResponse(identityXUseCase.createRegistrationChallenge(userId,applicationData,policy));

    }

    @PutMapping(path = "${co.itau.route.api.adapter.identityx.registration.challenge.update}")
    @Operation(summary = OPERATION_UPDATE_REGISTRATION_CHALLENGE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = RegistrationChallengeModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<RegistrationChallengeModel> updateRegistrationChallenge(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody Map<String, Object> payload) {

        String regChallengeId = (String)payload.get(ServiceConstants.REG_CHALLENGE_ID);
        String fidoRegistrationResponse = (String)payload.get(ServiceConstants.FIDO_REGISTRATION_RESPONSE);

        return ResponseHelper.successResponse(identityXUseCase.updateRegistrationChallenge(regChallengeId,fidoRegistrationResponse));

    }

    @GetMapping(path = "${co.itau.route.api.adapter.identityx.get.application}")
    @Operation(summary = OPERATION_GET_APPLICATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = ApplicationRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<ApplicationRequestModel> getApplicationById(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestHeader(name = APPLICATION_ID) String appId) {
        return ResponseHelper.successResponse(identityXUseCase.getApplicationById(appId));
    }

    @GetMapping(path = "${co.itau.route.api.adapter.identityx.get.policy}")
    @Operation(summary = OPERATION_GET_POLICY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = PolicyRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<PolicyRequestModel> getPolicyById(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestHeader(name = POLICY_ID) String policyId) {
        return ResponseHelper.successResponse(identityXUseCase.getPolicyById(policyId));
    }

    @PostMapping(path = "${co.itau.route.api.adapter.identityx.auth-request.generate}")
    @Operation(summary = OPERATION_CREATE_AUTHENTICATION_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = AuthenticationRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<AuthenticationRequestModel> createAuthenticationRequest(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody CreateAuthenticationRequestDto payload)
    {
        String userId = payload.getUserId();
        Application applicationData = payload.getApplication();
        Policy policy = payload.getPolicy();
        String description = payload.getDescription();
        String type = payload.getType();
        return ResponseHelper.successResponse(identityXUseCase.createAuthRequest(userId,applicationData,policy,description,type));

    }
    @PutMapping(path = "${co.itau.route.api.adapter.identityx.auth-request.update}")
    @Operation(summary = OPERATION_UPDATE_AUTHENTICATION_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = AuthenticationRequestModel.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<AuthenticationRequestModel> updateAuthenticationRequest(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestBody UpdateAuthenticationRequestDto payload) {

        String authenticationRequestId = payload.getAuthenticationRequestId();
        AuthenticationRequest authenticationRequest = payload.getAuthenticationRequest();

        return ResponseHelper.successResponse(identityXUseCase.updateAuthRequest(authenticationRequestId,authenticationRequest));

    }

    @GetMapping(path = "${co.itau.route.api.adapter.identityx.list.authenticators}")
    @Operation(summary = OPERATION_LIST_AUTHENTICATORS_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = ResponseListAuthenticatorsDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<ResponseListAuthenticatorsDto> listAuthenticators(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestHeader(name = USER_ID) String userId,
            @RequestHeader(name = APPLICATION_ID) String applicationId)
    {

        return ResponseHelper.successResponse(
                new ResponseListAuthenticatorsDto(identityXUseCase.getListAuthenticators(userId,applicationId)));
    }

    @DeleteMapping(path = "${co.itau.route.api.adapter.identityx.delete.authenticator}")
    @Operation(summary = OPERATION_DELETE_AUTHENTICATORS_REQUEST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_OK, description = ServiceConstants.RESPONSE_STATUS_DESC_OK,content = @Content(schema = @Schema(implementation = ResponseDeleteAuthenticatorDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BAD_REQUEST, description = RESPONSE_STATUS_DESC_BAD_REQUEST,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_BUSINESS, description = RESPONSE_STATUS_DESC_BUSINESS,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class))),
            @ApiResponse(responseCode = RESPONSE_STATUS_CODE_SERVER, description = RESPONSE_STATUS_DESC_SERVER,content = @Content(schema = @Schema(implementation = ResponseErrorBodyDto.class)))
    })
    public ResponseEntity<ResponseDeleteAuthenticatorDto> deleteAuthenticator(
            @RequestHeader(name = HEADER_CHANNEL_ID_REGIONAL) String channelId,
            @RequestHeader(name = HEADER_APPLICATION_REGIONAL) String application,
            @RequestHeader(name = HEADER_TIMESTAMP_REGIONAL) String timestamp,
            @RequestHeader(name = HEADER_TRANSACTION_ID_REGIONAL) String transactionId,
            @RequestHeader(name = HEADER_TERMINAL_ID_REGIONAL) String terminalId,
            @RequestHeader(name = HEADER_TOKEN_CLIENT_ID_REGIONAL, required = false) String tokenClientId,
            @RequestHeader(name = HEADER_TOKEN_USER_ID_REGIONAL, required = false) String tokenUserId,
            @RequestHeader(name = HEADER_SESSION_ID_REGIONAL) String sessionId,
            @RequestHeader(name = AUTHENTICATOR_ID) String authenticatorId)
    {

        AuthenticatorModel authenticatorDeleted = identityXUseCase.deleteAuthenticator(authenticatorId);
        return ResponseHelper.successResponse(new ResponseDeleteAuthenticatorDto(authenticatorDeleted));
    }
}
