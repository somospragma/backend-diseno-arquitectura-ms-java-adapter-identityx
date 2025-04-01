package co.com.pragma.identityx.driven_adapters.identityx.mapper;

import co.com.pragma.identityx.entry_points.rest.dto.AuthRequestOTPDto;
import co.com.pragma.identityx.model.AuthRequestOTPModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IdentityXMapper {
    AuthRequestOTPModel mapToModel(AuthRequestOTPDto authRequestOTPDto);
}
