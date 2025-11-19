import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email: string = '';
  password: string = '';
  errorMessage: string = '';

  private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient, private router: Router) {}

  onLogin() {
    const body = { email: this.email, password: this.password };

    this.http.post(this.apiUrl, body, { responseType: 'text' })
      .subscribe({
        next: (token) => {
          localStorage.setItem('jwt', token);
          this.errorMessage = '';
          this.router.navigate(['/home']);
        },
        error: (err) => {
          if (err.status === 401) {
            this.errorMessage = 'Credenziali non valide';
          } else {
            this.errorMessage = 'Errore di connessione al server';
          }
        }
      });
  }
}
