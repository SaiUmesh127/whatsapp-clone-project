import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/all`);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  getCurrentUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/profile`);
  }

  searchUsers(keyword: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }

  getOnlineUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/online`);
  }

  updateProfile(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, user);
  }
}