import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { MessageService } from 'src/app/services/message.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  @Output() userSelected = new EventEmitter<any>();

  users: any[] = [];
  currentUser: any;
  searchKeyword: string = '';
  loading: boolean = false;
  selectedUserId: number | null = null;

  constructor(
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCurrentUser();
    this.loadAllUsers();
  }

  loadCurrentUser(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.currentUser = user;
    }
  }

  loadAllUsers(): void {
    this.loading = true;
    this.messageService.getAllUsers().subscribe({
      next: (response: any) => {
        this.users = response.filter(
          (u: any) => u.id !== this.currentUser?.userId
        );
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        this.loading = false;
      }
    });
  }

  selectUser(user: any): void {
    this.selectedUserId = user.id;
    this.userSelected.emit(user);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  searchUsers(): void {
    if (!this.searchKeyword.trim()) {
      this.loadAllUsers();
      return;
    }

    const keyword = this.searchKeyword.toLowerCase();
    this.users = this.users.filter((u) =>
      u.fullName.toLowerCase().includes(keyword)
    );
  }

  getInitials(fullName: string): string {
    const names = fullName.split(' ');
    if (names.length >= 2) {
      return (
        names[0].charAt(0).toUpperCase() + names[1].charAt(0).toUpperCase()
      );
    }
    return fullName.charAt(0).toUpperCase();
  }
}
