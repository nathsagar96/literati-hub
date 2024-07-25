import { Component, OnInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  username: string = '';
  isMobileMenuOpen = false;

  constructor() { }
  ngOnInit(): void {
    this.getUsernameFromToken();
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  getUsernameFromToken() {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken: any = jwtDecode(token);
      const fullName: string = decodedToken.fullName;
      this.username = fullName.split(' ')[0] || 'Guest';
    }
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }
}
