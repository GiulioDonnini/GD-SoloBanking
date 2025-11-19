import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { BonificoComponent } from './pages/bonifico/bonifico.component';
import { RegistrazioneComponent } from './pages/registrazione-utente/registrazione-utente.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: DashboardComponent },
  { path: 'bonifico', component: BonificoComponent },
  { path: 'register', component: RegistrazioneComponent },
  { path: '**', redirectTo: 'login' }
];
