import { Component } from '@angular/core';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-bonifico',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './bonifico.component.html',
  styleUrls: ['./bonifico.component.css']
})
export class BonificoComponent {
  ibanMittente: string = '';
  ibanDestinatario: string = '';
  importo: number | null = null;
  descrizione: string = '';
  messaggio: string = '';
  errore: string = '';

  private apiUrl = 'http://localhost:8080/api/conto/bonifico';

  constructor(private http: HttpClient, private router: Router) {}

  vaiAlBonifico() {
    this.router.navigate(['/bonifico']);
  }

  inviaBonifico() {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      ibanMittente: this.ibanMittente,
      ibanDestinatario: this.ibanDestinatario,
      importo: this.importo,
      descrizione: this.descrizione
    };

    this.http.post(this.apiUrl, body, { headers, responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.messaggio = response;
          this.errore = '';
          this.pulisciForm();
        },
        error: (err) => {
          if (err.status === 400) {
            this.errore = err.error || 'Saldo insufficiente';
          } else {
            this.errore = 'Errore durante l’esecuzione del bonifico';
          }
          this.messaggio = '';
        }
      });
  }

  pulisciForm() {
    this.ibanDestinatario = '';
    this.importo = null;
    this.descrizione = '';
  }

  tornaHome() {
    this.router.navigate(['/home']);
  }

  logout() {
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }
}
