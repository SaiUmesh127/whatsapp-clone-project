-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: whatsapp_db
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `login_attempts`
--

DROP TABLE IF EXISTS `login_attempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_attempts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attempted_at` datetime(6) NOT NULL,
  `attempted_username` varchar(255) NOT NULL,
  `failure_reason` varchar(200) DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `successful` bit(1) NOT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtg9vhke4mlf5vij2rcvfk2dg2` (`user_id`),
  CONSTRAINT `FKtg9vhke4mlf5vij2rcvfk2dg2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_attempts`
--

LOCK TABLES `login_attempts` WRITE;
/*!40000 ALTER TABLE `login_attempts` DISABLE KEYS */;
INSERT INTO `login_attempts` VALUES (1,'2025-10-17 01:53:37.985023','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(3,'2025-10-17 02:30:50.706007','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(4,'2025-10-17 02:33:20.046410','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(5,'2025-10-17 02:35:40.999559','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(6,'2025-10-17 02:38:59.465553','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(7,'2025-10-17 02:45:32.150938','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(8,'2025-10-17 04:20:45.273559','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(9,'2025-10-17 04:22:01.704827','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',3),(10,'2025-10-17 04:23:36.480495','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(11,'2025-10-17 04:23:47.381114','Umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',2),(12,'2025-10-17 04:24:41.504283','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',3),(13,'2025-10-18 03:55:58.992517','Umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',2),(14,'2025-10-18 03:56:11.535077','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',3),(15,'2025-10-18 04:04:40.682358','sai_umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','PostmanRuntime/7.36.3',1),(16,'2025-10-20 08:02:41.334263','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36 Edg/141.0.0.0',3),(17,'2025-10-20 08:04:00.455845','Umesh',NULL,'0:0:0:0:0:0:0:1',_binary '','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36 Edg/141.0.0.0',2),(18,'2025-10-20 08:09:22.161553','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36 Edg/141.0.0.0',3),(19,'2025-10-20 08:12:18.221269','Dheeraj',NULL,'0:0:0:0:0:0:0:1',_binary '','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36 Edg/141.0.0.0',3);
/*!40000 ALTER TABLE `login_attempts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(5000) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `message_type` enum('AUDIO','FILE','IMAGE','TEXT','VIDEO') NOT NULL,
  `read_at` datetime(6) DEFAULT NULL,
  `sent_at` datetime(6) NOT NULL,
  `receiver_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `reaction` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt05r0b6n0iis8u7dfna4xdh73` (`receiver_id`),
  KEY `FK4ui4nnwntodh6wjvck53dbk9m` (`sender_id`),
  CONSTRAINT `FK4ui4nnwntodh6wjvck53dbk9m` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt05r0b6n0iis8u7dfna4xdh73` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'Hello umesh how are you',_binary '\0','TEXT',NULL,'2025-10-18 04:01:16.699839',2,3,NULL),(2,'Hi dude I am good how are you',_binary '\0','TEXT',NULL,'2025-10-20 08:04:32.442010',3,2,NULL),(3,'Hi',_binary '\0','TEXT',NULL,'2025-10-21 03:56:27.433358',2,3,NULL);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `about` varchar(500) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `last_seen` datetime(6) DEFAULT NULL,
  `online` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `profile_picture` varchar(500) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Backend Developer | Java | Spring Boot',_binary '','2025-10-17 01:52:12.691528','sai@example.com','Sai Umesh N','2025-10-18 04:04:53.237877',_binary '\0','$2a$10$uwqcYRyPQYJMJKV/gpyquOmILxd6DBJoyCHljZwB5j18ycMRzRLOW','+91 9999999999',NULL,'2025-10-18 04:04:53.256299','sai_umesh'),(2,'Hey there! I\'m using WhatsApp Clone',_binary '','2025-10-17 04:18:31.903298','saiumesh127@gmail.com','Narahari','2025-10-20 08:09:09.951479',_binary '\0','$2a$10$zwpOLNsBQY81.p3OnWgpS.ZQcc5UkjPYtdhjWn8ejiId6D7dT/AYK','+91 95426755',NULL,'2025-10-20 08:09:09.953535','Umesh'),(3,'Hey there! I\'m using WhatsApp Clone',_binary '','2025-10-17 04:18:32.228272','dheerajpola5757@gmail.com','Bob Johnson','2025-10-20 08:12:18.221269',_binary '','$2a$10$kuAnTXWnmRHOwcBETnYCPuAEoDIMPDp.QoiD1xqXjI1rdBPef.CWy','+91 80081003',NULL,'2025-10-20 08:12:18.234894','Dheeraj'),(4,'Hey there! I\'m using WhatsApp Clone',_binary '','2025-10-17 04:18:32.397249','charlie@example.com','Charlie Brown',NULL,_binary '\0','$2a$10$H6p37u5DtWdPgcZTHdTcIuA6/TPihzpQH.I9FZiIJVRALuZrvMkJ2','+91 3333333333',NULL,'2025-10-17 04:18:32.397249','charlie'),(5,'Hey there! I\'m using WhatsApp Clone',_binary '','2025-10-17 04:18:32.547297','diana@example.com','Diana Prince',NULL,_binary '\0','$2a$10$KMKr7GgWdvDbK3OiFPMeruKuIKHHUeHdAMeXJklWTjd8H4kcXfc.y','+91 4444444444',NULL,'2025-10-17 04:18:32.547297','diana'),(6,'Hey there! I\'m using WhatsApp Clone',_binary '','2025-10-17 04:18:32.717406','eve@example.com','Eve Davis',NULL,_binary '\0','$2a$10$GUkRDn8YQGdCCYCpT0H7LeYNrYoKrhBTRU3B5UrK.xlaMMDT27Cl2','+91 5555555555',NULL,'2025-10-17 04:18:32.717406','eve');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-22 11:52:18
