import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from 'src/app/services/models';
import { AuthenticationService } from 'src/app/services/services';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  constructor(private router: Router,
    private authenticationService: AuthenticationService,
    private tokenService: TokenService) { }

  login(): void {
    this.errorMsg = [];
    this.authenticationService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res): void => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['books']);
      },
      error: (err): void => {
        console.error(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      }
    })
  }
  register(): void {
    this.router.navigate(['register']);
  }
}
