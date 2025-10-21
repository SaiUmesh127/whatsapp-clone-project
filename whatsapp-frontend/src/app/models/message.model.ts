export interface Message {
  id?: number;
  senderId: number;
  senderName?: string;
  receiverId: number;
  receiverName?: string;
  content: string;
  messageType: string;
  isRead: boolean;
  sentAt?: string;
  readAt?: string;
}

export interface Conversation {
  userId: number;
  username: string;
  fullName: string;
  profilePicture?: string;
  lastMessage: string;
  lastMessageTime: string;
  unreadCount: number;
  online: boolean;
}