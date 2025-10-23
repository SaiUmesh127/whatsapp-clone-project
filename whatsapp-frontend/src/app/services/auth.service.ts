import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth-response.model';
import { environment } from '../../environments/environment'; // ✅ Import environment

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private apiUrl = `${environment.apiUrl}/api/auth`; // ✅ Uses dynamic base URL
  
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request)
      .pipe(
        tap(response => this.saveUserData(response))
      );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request)
      .pipe(
        tap(response => this.saveUserData(response))
      );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {})
      .pipe(
        tap(() => this.clearUserData())
      );
  }

  getCurrentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    const user = this.getCurrentUser();
    return user ? user.token : null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  private saveUserData(response: AuthResponse): void {
    localStorage.setItem('currentUser', JSON.stringify(response));
    localStorage.setItem('token', response.token);
    this.currentUserSubject.next(response);
  }

  private clearUserData(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }
}
