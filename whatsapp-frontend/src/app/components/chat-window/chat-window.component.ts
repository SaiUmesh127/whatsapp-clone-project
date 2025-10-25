import { Component, OnInit, Input, OnChanges, SimpleChanges, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { MessageService } from '../../services/message.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { Message } from '../../models/message.model';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css']
})
export class ChatWindowComponent implements OnInit, OnChanges, AfterViewChecked {
  @Input() selectedUser: User | null = null;
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: Message[] = [];
  newMessage: string = '';
  currentUserId: number = 0;
  loading: boolean = false;
  sending: boolean = false;
  shouldScroll: boolean = false;

  // 🎙️ Voice recording
  mediaRecorder: any;
  audioChunks: any[] = [];
  isRecording: boolean = false;

  constructor(
    private messageService: MessageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) this.currentUserId = currentUser.userId;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedUser'] && this.selectedUser) this.loadMessages();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  loadMessages(): void {
    if (!this.selectedUser) return;
    this.loading = true;

    this.messageService.getConversation(this.selectedUser.id.toString()).subscribe({
      next: (messages: Message[]) => {
        this.messages = messages;
        this.loading = false;
        this.shouldScroll = true;
      },
      error: (error) => {
        console.error('Error loading messages:', error);
        this.loading = false;
      }
    });
  }

  sendMessage(): void {
    if (!this.newMessage.trim() || !this.selectedUser || this.sending) return;

    const message: Message = {
      receiverId: this.selectedUser.id,
      content: this.newMessage.trim(),
      messageType: 'TEXT',
      senderId: this.currentUserId,
      isRead: false
    };

    this.sending = true;
    this.messageService.sendMessage(message).subscribe({
      next: (sentMessage) => {
        this.messages.push(sentMessage);
        this.newMessage = '';
        this.sending = false;
        this.shouldScroll = true;
      },
      error: (error) => {
        console.error('Error sending message:', error);
        this.sending = false;
        alert('Failed to send message. Please try again.');
      }
    });
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  isMyMessage(message: Message): boolean {
    return message.senderId === this.currentUserId;
  }

  scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }

  getInitials(fullName: string): string {
    const names = fullName.split(' ');
    if (names.length >= 2) return names[0][0].toUpperCase() + names[1][0].toUpperCase();
    return fullName[0].toUpperCase();
  }

  // Voice recording
  recordAudio(): void {
    if (this.isRecording) return;

    navigator.mediaDevices.getUserMedia({ audio: true })
      .then((stream) => {
        this.mediaRecorder = new MediaRecorder(stream);
        this.audioChunks = [];
        this.mediaRecorder.ondataavailable = (e: any) => this.audioChunks.push(e.data);
        this.mediaRecorder.onstop = () => {
          const audioBlob = new Blob(this.audioChunks, { type: 'audio/webm' });
          this.uploadAudio(audioBlob);
        };
        this.mediaRecorder.start();
        this.isRecording = true;
        console.log('🎤 Recording started...');
      })
      .catch((err) => console.error('Microphone error:', err));
  }

  stopRecording(): void {
    if (this.mediaRecorder && this.isRecording) {
      this.mediaRecorder.stop();
      this.isRecording = false;
      console.log('🛑 Recording stopped.');
    }
  }

  uploadAudio(audioBlob: Blob): void {
    if (!this.selectedUser) return;

    const formData = new FormData();
    formData.append('audio', audioBlob, 'voiceMessage.webm');
    formData.append('receiverId', this.selectedUser.id.toString());
    formData.append('senderId', this.currentUserId.toString());

    this.messageService.uploadVoiceMessage(formData).subscribe({
      next: (message: Message) => {
        this.messages.push(message);
        this.shouldScroll = true;
      },
      error: (err) => console.error('Error uploading voice message:', err)
    });
  }
}
