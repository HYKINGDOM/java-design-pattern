package org.fkjava.gateway.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

public class ServerOAuth2AuthorizationCodeAuthenticationTokenConverter extends
        org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationCodeAuthenticationTokenConverter {

    public ServerOAuth2AuthorizationCodeAuthenticationTokenConverter(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        Mono<Authentication> mono = super.convert(serverWebExchange);
        return mono.map(authentication -> {
            OAuth2AuthorizationCodeAuthenticationToken token = (OAuth2AuthorizationCodeAuthenticationToken) authentication;

            OAuth2AuthorizationResponse response = convertResponse(serverWebExchange);

            return new OAuth2AuthorizationCodeAuthenticationToken(
                    token.getClientRegistration(),
                    new OAuth2AuthorizationExchange(
                            token.getAuthorizationExchange().getAuthorizationRequest(),
                            response)
            );
        });
    }

    private OAuth2AuthorizationResponse convertResponse(ServerWebExchange exchange) {
        MultiValueMap<String, String> queryParams = exchange.getRequest()
                .getQueryParams();
        String redirectUri = UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                .query(null)
                .build()
                .toUriString();
        List<String> headers = exchange.getRequest().getHeaders().get("X-Forwarded-Proto");
        if (headers != null && !headers.isEmpty()) {
            String proto = headers.get(0);
            if (redirectUri.startsWith("http://") && proto.equals("https")) {
                redirectUri = "https://" + redirectUri.substring(7);
            }
        }
        return convert(queryParams, redirectUri);
    }

    private OAuth2AuthorizationResponse convert(MultiValueMap<String, String> request, String redirectUri) {
        String code = request.getFirst(OAuth2ParameterNames.CODE);
        String errorCode = request.getFirst(OAuth2ParameterNames.ERROR);
        String state = request.getFirst(OAuth2ParameterNames.STATE);

        if (StringUtils.hasText(code)) {
            return OAuth2AuthorizationResponse.success(code)
                    .redirectUri(redirectUri)
                    .state(state)
                    .build();
        } else {
            String errorDescription = request.getFirst(OAuth2ParameterNames.ERROR_DESCRIPTION);
            String errorUri = request.getFirst(OAuth2ParameterNames.ERROR_URI);
            return OAuth2AuthorizationResponse.error(errorCode)
                    .redirectUri(redirectUri)
                    .errorDescription(errorDescription)
                    .errorUri(errorUri)
                    .state(state)
                    .build();
        }
    }
}
