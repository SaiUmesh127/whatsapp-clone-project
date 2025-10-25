import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  selectedUser: User | null = null;

  constructor() { }

  ngOnInit(): void { }

  onUserSelected(user: User): void {
    this.selectedUser = user;
  }
}
