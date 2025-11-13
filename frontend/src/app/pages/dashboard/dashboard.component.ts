import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  iban: string = '';
  saldo: number | null = null;
  movimenti: any[] = [];
  errore: string = '';

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private router: Router) {}


  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });

    this.http.get(`${this.apiUrl}/conto/personale`, { headers })
      .subscribe({
        next: (conto: any) => {
          this.iban = conto.iban;
          this.saldo = conto.saldo;
          this.caricaMovimenti(conto.id, headers);
        },
        error: () => this.errore = 'Errore nel caricamento del conto'
      });
  }

  caricaMovimenti(contoId: number, headers: HttpHeaders) {
    this.http.get(`${this.apiUrl}/transazioni/${contoId}`, { headers })
      .subscribe({
        next: (movimenti: any) => this.movimenti = movimenti,
        error: () => this.errore = 'Errore nel caricamento movimenti'
      });
  }

  vaiABonifico() {
    this.router.navigate(['/bonifico']);
  }

  logout() {
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }
}
