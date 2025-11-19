import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registrazione',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterLink],
  templateUrl: './registrazione-utente.component.html',
  styleUrls: ['./registrazione-utente.component.css']
})
export class RegistrazioneComponent {
  nome: string = '';
  cognome: string = '';
  email: string = '';
  password: string = '';
  confermaPassword: string = '';
  messaggio: string = '';
  errore: string = '';

  private apiUrl = 'http://localhost:8080/api/auth/register';

  constructor(private http: HttpClient, private router: Router) {}

  registra() {
    this.errore = '';
    this.messaggio = '';

    if (this.password !== this.confermaPassword) {
      this.errore = 'Le password non coincidono';
      return;
    }

    const body = {
      nome: this.nome,
      cognome: this.cognome,
      email: this.email,
      password: this.password
    };

    this.http.post(this.apiUrl, body, { responseType: 'text' })
      .subscribe({
        next: (res) => {
          this.messaggio = 'Registrazione completata. Ora puoi accedere.';
          setTimeout(() => this.router.navigate(['/login']), 1500);
        },
        error: (err) => {
          if (err.status === 400) {
            this.errore = err.error || 'Utente già registrato';
          } else {
            this.errore = 'Errore durante la registrazione';
          }
        }
      });
  }
}
