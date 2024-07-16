package dev.sagar.literatihub.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

  @NotEmpty(message = "Firstname is mandatory")
  @NotBlank(message = "Firstname is mandatory")
  private String firstname;

  @NotEmpty(message = "Lastname is mandatory")
  @NotBlank(message = "Lastname is mandatory")
  private String lastname;

  @NotEmpty(message = "Email is mandatory")
  @NotBlank(message = "Email is mandatory")
  @Email
  private String email;

  @NotEmpty(message = "Password is mandatory")
  @NotBlank(message = "Password is mandatory")
  @Size(min = 8, message = "Password should be at least 8 characters")
  private String password;
}
