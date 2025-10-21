import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message, Conversation } from '../models/message.model';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  
  private apiUrl = 'http://localhost:8080/api/messages';

  constructor(private http: HttpClient) { }

  sendMessage(message: Message): Observable<Message> {
    return this.http.post<Message>(this.apiUrl, message);
  }

  getConversation(userId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/conversation/${userId}`);
  }

  getRecentConversations(): Observable<Conversation[]> {
    return this.http.get<Conversation[]>(`${this.apiUrl}/recent`);
  }

  markAsRead(messageId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${messageId}/read`, {});
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/unread-count`);
  }
  uploadVoiceMessage(formData: FormData) {
  return this.http.post<Message>(`${this.apiUrl}/messages/audio`, formData);
}
}