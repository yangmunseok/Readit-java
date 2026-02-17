package org.spring.createa.demoproject.service;

import java.util.Map;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.UserPrinipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

  @Autowired
  UserService userService;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser user = super.loadUser(userRequest);

    String provider = userRequest.getClientRegistration().getRegistrationId();
    Map<String, Object> attributes = user.getAttributes();

    //OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, attributes);

    Map<String, Object> claims = user.getClaims();
    System.out.println("attributes:" + attributes);
    System.out.println("claims:" + claims);
    OidcUserInfo userInfo = user.getUserInfo();
    OidcIdToken oidcIdToken = user.getIdToken();
    System.out.println("userinfo:" + userInfo);
    System.out.println("oidcIdToken:" + oidcIdToken);

    String name = user.getName();
    var builder = UserPrinipal.builder().attributes(attributes).claims(claims)
        .oidcUserInfo(userInfo).oidcIdToken(oidcIdToken).name(name);

    User exUser = userService.findByEmailAndProvider((String) claims.get("email"), provider);

    if (exUser == null) {
      User newUser = new User((String) claims.get("email"),
          (String) claims.getOrDefault("name", claims.get("nickname")), "USER",
          provider);
      userService.register(newUser);
      return builder.user(newUser).build();
    }
    return builder.user(exUser).build();
  }
}
