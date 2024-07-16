package dev.sagar.literatihub.auth;

import dev.sagar.literatihub.email.EmailService;
import dev.sagar.literatihub.email.EmailTemplateName;
import dev.sagar.literatihub.role.RoleRepository;
import dev.sagar.literatihub.security.JwtService;
import dev.sagar.literatihub.user.Token;
import dev.sagar.literatihub.user.TokenRepository;
import dev.sagar.literatihub.user.User;
import dev.sagar.literatihub.user.UserRepository;
import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Value("${application.mailing.front-end.activation-url}")
  private String activationUrl;

  public void register(RegistrationRequest request) throws MessagingException {
    var userRole =
        roleRepository
            .findByName("USER")
            .orElseThrow(() -> new IllegalArgumentException("ROLE USER is not initialized"));

    var user =
        User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(userRole))
            .build();

    userRepository.save(user);
    sendVerificationEmail(user);
  }

  private void sendVerificationEmail(User user) throws MessagingException {
    var newToken = generateAndSaveActivationToken(user);
    emailService.sendEmail(
        user.getEmail(),
        user.getFullName(),
        EmailTemplateName.ACTIVATE_ACCOUNT,
        activationUrl,
        newToken,
        "Account activation");
  }

  private String generateAndSaveActivationToken(User user) {
    String generatedToken = generateActivationToken(6);

    var token =
        Token.builder()
            .token(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(15))
            .user(user)
            .build();

    tokenRepository.save(token);

    return generatedToken;
  }

  private String generateActivationToken(int length) {
    String characters = "0123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(characters.length());
      codeBuilder.append(characters.charAt(randomIndex));
    }

    return codeBuilder.toString();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
    var claims = new HashMap<String, Object>();
    var user = ((User) auth.getPrincipal());
    claims.put("fullName", user.getFullName());
    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  public void activateAccount(String token) throws MessagingException {
    Token savedToken =
        tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid Token"));

    if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
      sendVerificationEmail(savedToken.getUser());
      throw new RuntimeException(
          ("Activation token has expired. A new token has been sent to same email address"));
    }
    var user =
        userRepository
            .findById(savedToken.getUser().getId())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    user.setEnabled(true);
    userRepository.save(user);
    savedToken.setValidatedAt(LocalDateTime.now());
    tokenRepository.save(savedToken);
  }
}
