import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'https://whatsapp-clone-project-pa56.onrender.com/api/auth';
  private currentUserSubject = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  /** Register user */
  register(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, userData);
  }

  /** Login user */
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      tap((user: any) => {
        if (user) {
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }
      })
    );
  }

  /** Check if user is logged in */
  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }
  getCurrentUser(): any {
  const user = localStorage.getItem('currentUser');
  return user ? JSON.parse(user) : null;
}

logout(): void {
  localStorage.removeItem('token');
  localStorage.removeItem('currentUser');
}

getToken(): string | null {
  return localStorage.getItem('token');
}
}
