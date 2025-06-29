import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

interface User {
  username: string;
  role: string;
  id?: number;
}

interface LoginResponse {
  token: string;
  role?: string;
  username?: string;
  id?: number;
}

interface RegisterRequest {
  username: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/v1/authentication';
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  private getUserFromStorage(): User | null {
    const storedUser = localStorage.getItem('currentUser');
    return storedUser ? JSON.parse(storedUser) : null;
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Decodificar JWT para extraer información del usuario
  private decodeJWT(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (error) {
      console.error('Error decodificando JWT:', error);
      return null;
    }
  }

  login(username: string, password: string): Observable<boolean> {
    const url = `${this.apiUrl}/sign-in`;
    const body = { username, password };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    console.log('🔄 Enviando solicitud de login para Notes App...');

    return this.http.post<LoginResponse>(url, body, { headers }).pipe(
        map(response => {
          console.log('📥 Respuesta del backend Notes App:', response);

          if (response && response.token) {
            console.log('✅ Login exitoso en Notes App. Token JWT recibido.');

            // Guardar token
            localStorage.setItem('token', response.token);

            // Decodificar JWT para obtener información del usuario
            const decodedToken = this.decodeJWT(response.token);

            // Crear objeto usuario
            const user: User = {
              username: response.username || decodedToken?.sub || username,
              role: response.role || decodedToken?.role || 'ROLE_USER',
              id: response.id || decodedToken?.userId
            };

            console.log('👤 Usuario autenticado en Notes App:', user);

            // Guardar usuario
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);

            return true;
          }

          return false;
        }),
        catchError(error => {
          console.error('❌ Error durante el login en Notes App:', error);
          return of(false);
        })
    );
  }

  register(username: string, password: string, role: string = 'ROLE_USER'): Observable<any> {
    const url = `${this.apiUrl}/sign-up`;
    const body: RegisterRequest = {
      username,
      password,
      role: role || 'ROLE_USER'
    };
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    console.log('🔄 Registrando nuevo usuario en Notes App...');

    return this.http.post<any>(url, body, { headers }).pipe(
        tap(response => {
          console.log('✅ Registro exitoso en Notes App:', response);
        }),
        catchError(error => {
          console.error('❌ Error durante el registro en Notes App:', error);
          throw error;
        })
    );
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    const user = this.currentUserValue;

    if (!token || !user) {
      return false;
    }

    // Verificar expiración del token
    try {
      const decodedToken = this.decodeJWT(token);
      if (decodedToken && decodedToken.exp) {
        const currentTime = Math.floor(Date.now() / 1000);
        if (decodedToken.exp < currentTime) {
          console.log('⏰ Token JWT expirado en Notes App');
          this.logout();
          return false;
        }
      }
    } catch (error) {
      console.error('Error verificando token en Notes App:', error);
      return false;
    }

    return true;
  }

  logout(): void {
    console.log('🚪 Cerrando sesión en Notes App...');
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    console.log('✅ Sesión cerrada en Notes App.');
  }

  getLoggedInUser(): User | null {
    return this.currentUserValue;
  }

  hasRole(role: string): boolean {
    const user = this.currentUserValue;
    return user ? user.role === role : false;
  }

  getTokenInfo(): any {
    const token = this.getToken();
    return token ? this.decodeJWT(token) : null;
  }
}
