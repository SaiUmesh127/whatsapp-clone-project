import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  @Output() userSelected = new EventEmitter<User>();

  users: User[] = [];
  currentUser: any;
  searchKeyword: string = '';
  selectedUserId: number | null = null;
  loading: boolean = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.loading = false;
      }
    });
  }

  searchUsers(): void {
    if (this.searchKeyword.trim() === '') {
      this.loadUsers();
      return;
    }

    this.userService.searchUsers(this.searchKeyword).subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Error searching users:', error);
      }
    });
  }

  selectUser(user: User): void {
    this.selectedUserId = user.id;
    this.userSelected.emit(user);
  }

  logout(): void {
    if (confirm('Are you sure you want to logout?')) {
      this.authService.logout().subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Logout error:', error);
          this.router.navigate(['/login']);
        }
      });
    }
  }

  getInitials(fullName: string): string {
    const names = fullName.split(' ');
    if (names.length >= 2) {
      return names[0].charAt(0).toUpperCase() + names[1].charAt(0).toUpperCase();
    }
    return fullName.charAt(0).toUpperCase();
  }
}