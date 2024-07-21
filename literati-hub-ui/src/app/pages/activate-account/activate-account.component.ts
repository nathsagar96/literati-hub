import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/services/authentication.service';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss']
})
export class ActivateAccountComponent {


  message: string = '';
  isOk: boolean = true;
  submitted: boolean = false;

  constructor(private router: Router,
    private authenticationService: AuthenticationService
  ) { }


  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  confirmAccount(token: string) {
    this.authenticationService.confirm({
      token
    }).subscribe({
      next: () => {
        this.message = 'Account activated successfully.\nNow you can proceed to login.';
        this.submitted = true;
        this.isOk = true;
      },
      error: (error: HttpErrorResponse) => {
        this.message = 'Token has been expired or invalid';
        this.submitted = true;
        this.isOk = false;
      }
    })
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

}
