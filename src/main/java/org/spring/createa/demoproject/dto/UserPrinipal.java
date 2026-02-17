package org.spring.createa.demoproject.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.spring.createa.demoproject.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Builder
public class UserPrinipal implements UserDetails, OidcUser {

  private User user;
  private Map<String, Object> attributes;
  private Map<String, Object> claims;
  private OidcUserInfo oidcUserInfo;
  private OidcIdToken oidcIdToken;
  private String name;

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("USER"));
  }

  @Override
  public @Nullable String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "UserPrinipal{" +
        "user=" + user +
        '}';
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, Object> getClaims() {
    return claims;
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return oidcUserInfo;
  }

  @Override
  public OidcIdToken getIdToken() {
    return oidcIdToken;
  }
}
