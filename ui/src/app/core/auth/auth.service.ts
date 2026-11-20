import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { API } from '../api/banque';

export interface Utilisateur {
  readonly login: string;
  readonly roles: readonly string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  readonly utilisateur = signal<Utilisateur | null>(null);


  identifiantsBase64: string | null = null;

  async connecter(login: string, motDePasse: string): Promise<void> {
    const base64 = btoa(login + ':' + motDePasse);
    const headers = new HttpHeaders({ Authorization: 'Basic ' + base64 });

    const reponse = await firstValueFrom(this.http.get<Utilisateur>(API + '/me', { headers }));

    this.identifiantsBase64 = base64;
    this.utilisateur.set(reponse);
  }

  deconnecter(): void {
    this.identifiantsBase64 = null;
    this.utilisateur.set(null);
  }
}