package study.metricsproducer.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Value("${app.security.api-key-header-name:X-API-KEY}")
    private String authTokenHeaderName;
    @Value("${app.security.api-key:ProducerApiKey}")
    private String authToken;

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(authTokenHeaderName);
        if (apiKey == null || !apiKey.equals(authToken)) {
            return Optional.empty();
        }

        return Optional.of(new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES));
    }
}