import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Message } from '../models/message.model';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private apiUrl = environment.apiUrl; // e.g. https://whatsapp-clone-project-pa56.onrender.com

  constructor(private http: HttpClient) {}

  getConversation(receiverId: string): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/api/messages/${receiverId}`);
  }

  sendMessage(message: Message): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/api/messages/send`, message);
  }

  getAllUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/api/users`);
  }

  // 🎙️ Upload voice message API
  uploadVoiceMessage(formData: FormData): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/api/messages/voice`, formData);
  }
}
