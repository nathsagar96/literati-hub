package dev.sagar.literatihub.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest registrationRequest)
      throws MessagingException {
    authenticationService.register(registrationRequest);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }

  @GetMapping("/activate-account")
  public void confirm(@RequestParam String token) throws MessagingException {
    authenticationService.activateAccount(token);
  }
}
