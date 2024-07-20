import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationRequest } from 'src/app/services/models';
import { AuthenticationService } from 'src/app/services/services/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  constructor(private router: Router,
    private authenticationService: AuthenticationService) {
  }

  registrationRequest: RegistrationRequest = {
    email: '', firstname: '', lastname: '', password: ''
  }
  errorMsg: Array<string> = [];

  login() {
    this.router.navigate(['login']);
  }
  register() {
    this.errorMsg = [];
    this.authenticationService.register({
      body: this.registrationRequest
    }).subscribe({
      next: (res): void => {
        this.router.navigate(['activate-account']);
      },
      error: (err): void => {
        this.errorMsg = err.error.validationErrors;
      }
    });
  }
}

