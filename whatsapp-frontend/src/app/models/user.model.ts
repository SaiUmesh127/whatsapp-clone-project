export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  profilePicture?: string;
  about?: string;
  phoneNumber?: string;
  online: boolean;
  lastSeen?: Date;
  createdAt?: Date;
  active: boolean;
}