package co.com.pragma.identityx.driven_adapters.identityx.mapper;

import co.com.pragma.identityx.entry_points.rest.dto.AuthRequestOTPDto;
import co.com.pragma.identityx.model.AuthRequestOTPModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class IdentityXMapperTest {

    private final IdentityXMapper identityXMapper = Mappers.getMapper(IdentityXMapper.class);


    @Test
    void testMapToModel_AuthRequestOTPDto() {
        AuthRequestOTPDto authRequestOTPDto = new AuthRequestOTPDto();
        authRequestOTPDto.setAuthenticationRequestId("1234562");
        authRequestOTPDto.setFidoAuthenticationRequest("1234562");
        authRequestOTPDto.setCreated(new Date());
        authRequestOTPDto.setStatus("1234562");
        authRequestOTPDto.setExpiration(new Date());
        authRequestOTPDto.setAuthenticationCode("1234562");
        authRequestOTPDto.setAvailableRetries(1234562L);
        authRequestOTPDto.setTotalRetriesAllowed(1L);
        authRequestOTPDto.setVerificationResult("1234562");

        //Act
        AuthRequestOTPModel result = identityXMapper.mapToModel(authRequestOTPDto);

        //Assert
        assertEquals(authRequestOTPDto.getAuthenticationRequestId(),result.getAuthenticationRequestId());
        assertEquals(authRequestOTPDto.getFidoAuthenticationRequest(),result.getFidoAuthenticationRequest());
        assertEquals(authRequestOTPDto.getCreated(),result.getCreated());
        assertEquals(authRequestOTPDto.getStatus(),result.getStatus());
        assertEquals(authRequestOTPDto.getExpiration(),result.getExpiration());
        assertEquals(authRequestOTPDto.getAuthenticationCode(),result.getAuthenticationCode());
        assertEquals(authRequestOTPDto.getAvailableRetries(),result.getAvailableRetries());
        assertEquals(authRequestOTPDto.getVerificationResult(),result.getVerificationResult());
        assertEquals(authRequestOTPDto.getTotalRetriesAllowed(),result.getTotalRetriesAllowed());
    }

}
