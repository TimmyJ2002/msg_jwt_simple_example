package de.msg.training.jwtsimpleexample.controller.auth;


import de.msg.training.jwtsimpleexample.config.JwtUtils;
import de.msg.training.jwtsimpleexample.repository.RoleRepository;
import de.msg.training.jwtsimpleexample.repository.UserRepository;
import de.msg.training.jwtsimpleexample.service.RefreshTokenService;
import de.msg.training.jwtsimpleexample.service.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final String REFRESHTOKEN_COOKIE_NAME = "RefreshTokenCookie";
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  JwtUtils jwtUtils;


  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    String refreshToken = UUID.randomUUID().toString();
    refreshTokenService.deleteRefreshTokenForUser(userDetails.getId());
    refreshTokenService.createRefreshToken(refreshToken, userDetails.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, createCookie(refreshToken).toString());
    SignInResponse response = new SignInResponse(jwt, "", userDetails.getId(),
            userDetails.getUsername(), userDetails.getEmail(), roles);

    ResponseEntity<?> responseEntity = ResponseEntity.ok(response);

    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }

  @GetMapping("/refreshToken")
  public ResponseEntity<?> checkCookie(HttpServletRequest request) {
    Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(REFRESHTOKEN_COOKIE_NAME)).findFirst();
    if(cookie.isPresent()) {
      return ResponseEntity.ok(new RefreshTokenResponse(refreshTokenService.exchangeRefreshToken(cookie.get().getValue())));
    }
    throw new RuntimeException("Cookie was not set");
  }

  private ResponseCookie createCookie(String token) {
    return ResponseCookie.from(REFRESHTOKEN_COOKIE_NAME, token)
            .httpOnly(true)
            .maxAge(Duration.ofDays(1))
            .sameSite("None")
            .path("/auth/refreshToken")
            .build();
  }


}
