import { Component, OnInit } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  username: string = '';

  ngOnInit(): void {
    this.setActiveLink();
    this.getUsernameFromToken();
  }

  setActiveLink() {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(link => link.classList.remove('active'));
        link.classList.add('active');
      });
    });
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
