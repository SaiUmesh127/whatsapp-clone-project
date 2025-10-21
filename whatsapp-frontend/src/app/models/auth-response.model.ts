export interface AuthResponse {
  token: string;
  type: string;
  userId: number;
  username: string;
  email: string;
  fullName: string;
  message: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  phoneNumber?: string;
}