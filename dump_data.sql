-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: ecommercev5
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  KEY `FK709eickf3kc0dujx3ub9i7btf` (`user_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK709eickf3kc0dujx3ub9i7btf` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (3,49.99,2,4,1),(4,59.99,13,8,1),(5,89.99,1,11,1);
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,89.99,1,1,2),(2,29.99,1,1,3);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `shipping_address` varchar(255) DEFAULT NULL,
  `status` enum('CANCELLED','CONFIRMED','DELIVERED','PENDING','SHIPPED') NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2025-04-21 09:33:39.133720','PENDING','LOL, LOL, POP','PENDING',119.98,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `average_rating` double DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `review_count` int DEFAULT NULL,
  `stock` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,4.5,'Men','Tailored for modern sophistication—ideal for formal evenings and sharp statements.','https://images.unsplash.com/photo-1630173250799-2813d34ed14b?q=80&w=1965&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Slim Fit Blazer',129.99,12,25),(2,4.7,'Men','Relaxed silhouette with timeless denim texture—effortlessly cool.','https://plus.unsplash.com/premium_photo-1708274146193-1064e177ae39?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Casual Denim Jacket',89.99,18,31),(3,4.3,'Men','Crisp, breathable cotton with an elevated casual appeal.','https://images.unsplash.com/photo-1625910513520-bed0389ce32f?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Cotton Polo Shirt',29.99,10,49),(4,4.6,'Men','Smart tailoring meets everyday ease in this modern classic.','https://images.unsplash.com/photo-1584865288642-42078afe6942?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Chinos Pants',49.99,14,40),(5,4.2,'Men','Statement prints meet effortless minimalism.','https://images.unsplash.com/photo-1627225793904-a2f900a6e4cf?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Graphic T-Shirt',19.99,25,75),(6,4.8,'Men','Supple leather cut for bold simplicity.','https://images.unsplash.com/photo-1521223890158-f9f7c3d5d504?q=80&w=1492&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Leather Jacket',199.99,30,15),(7,4.5,'Men','Clean lines and refined details for elevated daily wear.','https://plus.unsplash.com/premium_photo-1674777843130-40233fe45804?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Formal Shirt',39.99,22,45),(8,4.4,'Men','Streamlined silhouette with subtle stretch for modern comfort.','https://images.unsplash.com/photo-1475178626620-a4d074967452?q=80&w=1972&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Skinny Jeans',59.99,16,30),(9,4.7,'Men','Soft, structured, and made for understated warmth.','https://plus.unsplash.com/premium_photo-1671135590215-ded219822a44?q=80&w=1973&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Wool Sweater',69.99,11,22),(10,4.3,'Men','Sleek functionality with a minimalist edge.','https://images.unsplash.com/photo-1542406775-ade58c52d2e4?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Sports Hoodie',49.99,15,27),(11,4.6,'Women','Soft drape and floral expression—crafted for elegant movement.','https://images.unsplash.com/photo-1692220438343-d054c8b7d7c0?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Floral Maxi Dress',89.99,24,18),(12,4.5,'Women','A modern flow in structured pleats—effortlessly refined.','https://images.unsplash.com/photo-1631978278971-9afda1670882?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Pleated Midi Skirt',49.99,17,24),(13,4.7,'Women','Flattering elevation meets clean denim lines.','https://images.unsplash.com/photo-1678219376035-90da4bda4926?q=80&w=1946&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','High-Waist Jeans',59.99,20,35),(14,4.5,'Women','Fluid form with a defined waist—timeless and versatile.','https://images.unsplash.com/photo-1508829298730-713792c22189?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Wrap Dress',69.99,25,20),(15,4.6,'Women','Sculptural volume in a clean, winter-ready form.','https://images.unsplash.com/photo-1739620620741-f7b23ede3149?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Oversized Coat',149.00,19,12),(16,4.4,'Accessories','Polished simplicity—crafted in genuine leather.','https://images.unsplash.com/photo-1664285612706-b32633c95820?q=80&w=2158&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Classic Leather Belt',29.99,11,50),(17,4.3,'Men','Minimalist design meets all-day comfort.','https://images.unsplash.com/photo-1736555142217-916540c7f1b7?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Casual Sneakers',59.99,23,40),(18,4.7,'Accessories','Silk refinement for an elevated occasion.','https://plus.unsplash.com/premium_photo-1668618919861-9e85e97657e0?q=80&w=2072&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Elegant Silk Tie',19.99,15,60),(19,4.5,'Accessories','Retro geometry meets modern UV clarity.','https://plus.unsplash.com/premium_photo-1692340973681-e96b10bda346?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Vintage Sunglasses',39.99,28,35),(20,4.6,'Accessories','Textural softness wrapped in minimal luxury.','https://images.unsplash.com/photo-1738774106908-04ea849dec8e?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Designer Scarf',34.99,18,45),(21,4.8,'Women','Sleek contours crafted for seasonal transitions.','https://images.unsplash.com/photo-1574413230119-f302e1c9035d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Ankle Boots',89.99,21,30),(22,4.5,'Men','Classic meets contemporary in smooth, sculpted leather.','https://plus.unsplash.com/premium_photo-1674770380314-d1639a2d4b77?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Lace-up Oxford Shoes',109.99,19,25),(23,4.4,'Women','Bohemian lines in relaxed, airy movement.','https://images.unsplash.com/photo-1660997351262-6c31d8a35b6c?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Boho Maxi Skirt',44.99,12,40),(24,4.3,'Men','Functional and fresh—designed for sunlit adventures.','https://images.unsplash.com/photo-1695918428860-a3bc6a2f1d4b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Cargo Shorts',34.99,11,40),(25,4.2,'Women','Streamlined and airy with subtle structure.','https://images.unsplash.com/photo-1571666274590-f8cc87006500?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Fitted Tank Top',24.99,14,70),(26,4.6,'Accessories','Refined sun protection with vintage-inspired charm.','https://images.unsplash.com/photo-1719664026208-34ad5c25d0a9?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Summer Hat',19.99,10,50),(27,4.5,'Women','Effortless layering piece in contemporary denim.','https://plus.unsplash.com/premium_photo-1661286730661-3f12452bb601?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Denim Overalls',59.99,16,42),(28,4.4,'Men','Light structure with a clean finish—layering made simple.','https://plus.unsplash.com/premium_photo-1708274927606-0f186d55a7aa?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Puffer Vest',69.99,19,33),(29,4.3,'Men','Crisp construction designed for fluid transition.','https://images.unsplash.com/photo-1713816302861-1ff8d4b09fad?q=80&w=2026&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Button-Down Casual Shirt',44.99,21,38),(30,4.5,'Women','Contoured movement in sleek activewear.','https://images.unsplash.com/photo-1600348077475-d4db860d06f7?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Athletic Leggings',39.99,26,55),(31,4.7,'Men','Lightweight performance for optimal speed and comfort.','https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Running Shoes',79.99,35,60),(32,4.9,'Accessories','Non-slip surface for stability and support during practice.','https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=2120&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','Yoga Mat',24.99,40,80);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(1000) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `last_modified` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7qatq4kob2sr6rlp44khhj53g` (`user_id`),
  CONSTRAINT `FKdlvw23ak3u9v9bomm8g12rtc0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_permissions`
--

DROP TABLE IF EXISTS `staff_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_permissions` (
  `staff_id` bigint NOT NULL,
  `permission` enum('ADD_PRODUCTS','MANAGE_STOCK','UPDATE_ORDER_STATUS','UPDATE_PRODUCTS','VIEW_CUSTOMERS','VIEW_ORDERS','VIEW_PRODUCTS','VIEW_STOCK_REPORTS') DEFAULT NULL,
  KEY `FK91vshorw20h7tyrjkybyunff3` (`staff_id`),
  CONSTRAINT `FK91vshorw20h7tyrjkybyunff3` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_permissions`
--

LOCK TABLES `staff_permissions` WRITE;
/*!40000 ALTER TABLE `staff_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_locked` bit(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `oauth_provider` varchar(255) DEFAULT NULL,
  `oauth_provider_id` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_expiry` datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expiry` datetime(6) DEFAULT NULL,
  `role` enum('ADMIN','STAFF','USER') DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'1007 Mountain Drive, Gotham City',NULL,NULL,NULL,'bruce.wayne@wayne.enterprises',NULL,'Bruce Wayne',NULL,NULL,NULL,NULL,'$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y','+1555BATMAN0',NULL,NULL,NULL,'USER',NULL,_binary ''),(2,NULL,'Kent Farm, Smallville',NULL,NULL,NULL,'clark.kent@dailyplanet.com',NULL,'Clark Kent',NULL,NULL,NULL,NULL,'$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y','+1555SUPERMN',NULL,NULL,NULL,'USER',NULL,_binary ''),(3,NULL,'1 Stark Tower, New York',NULL,NULL,NULL,'tony.stark@starkindustries.com',NULL,'Tony Stark',NULL,NULL,NULL,NULL,'$2a$12$SATxIE9c94XvyC9hT7nHPOlyURChKKiqYPfV.G8gBesbQnYcgz/1.','+1555IRONMAN',NULL,NULL,NULL,'ADMIN',NULL,_binary ''),(4,NULL,'Avengers Compound, Upstate NY',NULL,NULL,NULL,'steve.rogers@avengers.com',NULL,'Steve Rogers',NULL,NULL,NULL,NULL,'$2a$12$UC2VFiYEZWkJUE8xdgKiQO5KuWB62X4kNIoPti8K63i5Pz3B5T5fi','+1555CAPTAIN',NULL,NULL,NULL,'STAFF',NULL,_binary ''),(5,NULL,'SHIELD HQ, Classified',NULL,NULL,NULL,'natasha.romanoff@shield.gov',NULL,'Natasha Romanoff',NULL,NULL,NULL,NULL,'$2a$12$UC2VFiYEZWkJUE8xdgKiQO5KuWB62X4kNIoPti8K63i5Pz3B5T5fi','+1555WIDOW00',NULL,NULL,NULL,'STAFF',NULL,_binary ''),(6,NULL,'Vought Tower, New York',NULL,NULL,NULL,'homelander@vought.com',NULL,'Homelander',NULL,NULL,NULL,NULL,'$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y','+1555VOUGHT1',NULL,NULL,NULL,'USER',NULL,_binary ''),(7,NULL,'Flatiron Building, New York',NULL,NULL,NULL,'billy.butcher@theboys.com',NULL,'Billy Butcher',NULL,NULL,NULL,NULL,'$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y','+1555BUTCHER',NULL,NULL,NULL,'USER',NULL,_binary '\0'),(8,NULL,'Vought Tower, New York',NULL,NULL,NULL,'the.deep@vought.com',NULL,'The Deep',NULL,NULL,NULL,NULL,'$2a$12$hefiufHSdplugrw6jcKFEOGFT31yRkw0nhkt/e676gYc97bV5eh8y','+1555DEEPSEA',NULL,NULL,NULL,'USER',NULL,_binary '');
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

-- Dump completed on 2025-04-21  9:35:22

-- Add staff entries
INSERT INTO staff (id, user_id, created_by, created_at) VALUES
(1, 4, 3, NOW()), -- Steve Rogers created by Tony Stark
(2, 5, 3, NOW()); -- Natasha Romanoff created by Tony Stark

-- Add staff permissions
-- Steve Rogers - Can view and update products, view orders
INSERT INTO staff_permissions (staff_id, permission) VALUES
(1, 'VIEW_PRODUCTS'),
(1, 'UPDATE_PRODUCTS'),
(1, 'ADD_PRODUCTS'),
(1, 'VIEW_ORDERS'),
(1, 'MANAGE_STOCK'),
(1, 'VIEW_STOCK_REPORTS');

-- Natasha Romanoff - Can view orders, update order status, view customers
INSERT INTO staff_permissions (staff_id, permission) VALUES
(2, 'VIEW_ORDERS'),
(2, 'UPDATE_ORDER_STATUS'),
(2, 'VIEW_CUSTOMERS'),
(2, 'MANAGE_STOCK');

-- Update users with city, state, postal code, and country information
UPDATE users SET 
    city = 'Gotham City', 
    state = 'New Jersey', 
    postal_code = '12345', 
    country = 'United States' 
WHERE id = 1; -- Bruce Wayne

UPDATE users SET 
    city = 'Smallville', 
    state = 'Kansas', 
    postal_code = '66605', 
    country = 'United States' 
WHERE id = 2; -- Clark Kent

UPDATE users SET 
    city = 'New York', 
    state = 'New York', 
    postal_code = '10021', 
    country = 'United States' 
WHERE id = 3; -- Tony Stark

UPDATE users SET 
    city = 'New York', 
    state = 'New York', 
    postal_code = '12550', 
    country = 'United States' 
WHERE id = 4; -- Steve Rogers

UPDATE users SET 
    city = 'Washington', 
    state = 'D.C.', 
    postal_code = '20500', 
    country = 'United States' 
WHERE id = 5; -- Natasha Romanoff

UPDATE users SET 
    city = 'New York', 
    state = 'New York', 
    postal_code = '10007', 
    country = 'United States' 
WHERE id = 6; -- Homelander

UPDATE users SET 
    city = 'New York', 
    state = 'New York', 
    postal_code = '10010', 
    country = 'United States' 
WHERE id = 7; -- Billy Butcher

UPDATE users SET 
    city = 'New York', 
    state = 'New York', 
    postal_code = '10007', 
    country = 'United States' 
WHERE id = 8; -- The Deep

-- Add reviews
INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES
(1, 1, 5, 'Great blazer! Perfect fit and excellent quality.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY)),
(1, 2, 4, 'Good blazer but slightly longer arms than expected', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY)),
(1, 6, 5, 'Very stylish, fits perfectly.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY)),
(2, 1, 5, 'This denim jacket is amazing - goes with everything', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY)),
(2, 8, 4, 'Comfortable and looks good, but the buttons feel a bit loose.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY)),
(3, 2, 5, 'The polo is very comfortable and breathable', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY)),
(3, 6, 4, 'Nice color, good fit.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY)),
(4, 1, 4, 'Nice chinos, comfortable for office wear', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY)),
(4, 8, 5, 'Best chinos I have owned. Great material.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
(5, 2, 3, 'The print faded after a few washes.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
(6, 1, 5, 'Expensive but worth it. Fantastic quality leather.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY)),
(7, 6, 4, 'Good formal shirt for the price.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY)),
(11, 2, 5, 'Beautiful dress, flows nicely.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY)),
(12, 8, 3, 'Skirt material is thinner than I expected.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY)),
(17, 1, 5, 'Very comfy sneakers for walking.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY)),
(21, 2, 4, 'Good boots, keep my feet warm.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));

-- Clear existing order data and add more comprehensive orders
TRUNCATE TABLE orders;
TRUNCATE TABLE order_items;

-- Add extensive orders with multiple orders per day (varying from 3-15 daily)
-- Only customers (user_ids 1, 2, 6, 7, 8) will place orders - not admins/staff
INSERT INTO orders (id, user_id, total_amount, shipping_address, status, payment_status, created_at) VALUES
-- Previous Month Orders
(1, 1, 189.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 8 HOUR),
(2, 2, 139.98, 'Kent Farm, Smallville', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 9 HOUR + INTERVAL 30 MINUTE),
(3, 6, 119.98, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 12 HOUR + INTERVAL 15 MINUTE),
(4, 7, 84.98, 'Flatiron Building, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 15 HOUR + INTERVAL 20 MINUTE),
(5, 8, 124.98, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 18 HOUR + INTERVAL 45 MINUTE),

-- Day 2 - 8 orders
(6, 1, 49.99, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY) + INTERVAL 8 HOUR + INTERVAL 5 MINUTE),
(7, 2, 19.99, 'Kent Farm, Smallville', 'CANCELLED', 'REFUNDED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY) + INTERVAL 13 HOUR + INTERVAL 45 MINUTE),
(8, 6, 139.98, 'Vought Tower, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY) + INTERVAL 15 HOUR + INTERVAL 20 MINUTE),
(9, 8, 44.99, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY) + INTERVAL 17 HOUR + INTERVAL 10 MINUTE),
(10, 1, 259.98, '1007 Mountain Drive, Gotham City', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 6 HOUR + INTERVAL 5 MINUTE),

-- Previous 3 weeks orders (selected)
(11, 2, 89.99, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 10 HOUR + INTERVAL 10 MINUTE),
(12, 6, 119.98, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 11 HOUR + INTERVAL 25 MINUTE),
(13, 8, 109.98, 'Vought Tower, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 12 HOUR + INTERVAL 40 MINUTE),
(14, 1, 179.98, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 13 HOUR + INTERVAL 15 MINUTE),
(15, 7, 74.97, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY) + INTERVAL 20 HOUR + INTERVAL 5 MINUTE),
(16, 2, 129.98, 'Kent Farm, Smallville', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY) + INTERVAL 9 HOUR + INTERVAL 30 MINUTE),
(17, 6, 119.98, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY) + INTERVAL 14 HOUR + INTERVAL 15 MINUTE),
(18, 1, 24.99, '1007 Mountain Drive, Gotham City', 'CANCELLED', 'REFUNDED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY) + INTERVAL 17 HOUR + INTERVAL 45 MINUTE),

-- 26-15 days ago (selected)
(19, 1, 159.98, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 6 HOUR + INTERVAL 10 MINUTE),
(20, 2, 119.98, 'Kent Farm, Smallville', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 6 HOUR + INTERVAL 45 MINUTE),
(21, 6, 69.98, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 9 HOUR + INTERVAL 5 MINUTE),
(22, 7, 84.98, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 9 HOUR + INTERVAL 40 MINUTE),
(23, 8, 64.98, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 10 HOUR + INTERVAL 15 MINUTE),
(24, 1, 89.99, '1007 Mountain Drive, Gotham City', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 11 HOUR + INTERVAL 30 MINUTE),
(25, 2, 59.99, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 12 HOUR + INTERVAL 45 MINUTE),
(26, 6, 199.99, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 17 HOUR + INTERVAL 15 MINUTE),
(27, 7, 19.99, 'Flatiron Building, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY) + INTERVAL 19 HOUR + INTERVAL 45 MINUTE),
(28, 8, 89.99, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY) + INTERVAL 8 HOUR + INTERVAL 30 MINUTE),
(29, 1, 49.99, '1007 Mountain Drive, Gotham City', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY) + INTERVAL 10 HOUR + INTERVAL 15 MINUTE),
(30, 2, 39.99, 'Kent Farm, Smallville', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY) + INTERVAL 12 HOUR + INTERVAL 45 MINUTE),

-- Last 14 days (selected orders)
(31, 6, 29.99, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY) + INTERVAL 10 HOUR + INTERVAL 45 MINUTE),
(32, 7, 149.00, 'Flatiron Building, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY) + INTERVAL 9 HOUR + INTERVAL 30 MINUTE),
(33, 8, 39.99, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY) + INTERVAL 14 HOUR + INTERVAL 15 MINUTE),
(34, 1, 59.99, '1007 Mountain Drive, Gotham City', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY) + INTERVAL 11 HOUR + INTERVAL 45 MINUTE),
(35, 2, 99.98, 'Kent Farm, Smallville', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY) + INTERVAL 16 HOUR + INTERVAL 30 MINUTE),

-- This week (past 7 days)
(36, 6, 109.99, 'Vought Tower, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY) + INTERVAL 8 HOUR + INTERVAL 15 MINUTE),
(37, 7, 49.99, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY) + INTERVAL 10 HOUR + INTERVAL 30 MINUTE),
(38, 8, 129.99, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY) + INTERVAL 9 HOUR + INTERVAL 45 MINUTE),
(39, 1, 69.99, '1007 Mountain Drive, Gotham City', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY) + INTERVAL 14 HOUR + INTERVAL 15 MINUTE),
(40, 2, 199.99, 'Kent Farm, Smallville', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 11 HOUR + INTERVAL 30 MINUTE),
(41, 6, 59.99, 'Vought Tower, New York', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY) + INTERVAL 16 HOUR + INTERVAL 45 MINUTE),
(42, 7, 89.99, 'Flatiron Building, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY) + INTERVAL 13 HOUR + INTERVAL 15 MINUTE),

-- Today's orders (varying times)
(43, 8, 44.99, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 8 HOUR + INTERVAL 15 MINUTE),
(44, 1, 119.98, '1007 Mountain Drive, Gotham City', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 6 HOUR + INTERVAL 30 MINUTE),
(45, 2, 69.99, 'Kent Farm, Smallville', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 4 HOUR + INTERVAL 45 MINUTE),
(46, 6, 79.99, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 2 HOUR + INTERVAL 10 MINUTE),
(47, 7, 24.99, 'Flatiron Building, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 1 HOUR + INTERVAL 5 MINUTE),
(48, 8, 59.99, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 30 MINUTE),
(49, 1, 34.99, '1007 Mountain Drive, Gotham City', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 15 MINUTE),
(50, 2, 19.99, 'Kent Farm, Smallville', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 5 MINUTE);

-- Add detailed order items
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
-- Order 1: Bruce Wayne - Blazer + 2x Polo
(1, 1, 1, 129.99),
(1, 3, 2, 29.99),

-- Order 2: Clark Kent - Denim Jacket + Chinos
(2, 2, 1, 89.99),
(2, 4, 1, 49.99),

-- Order 3: Homelander - Sweater + Hoodie
(3, 9, 1, 69.99),
(3, 10, 1, 49.99),

-- Order 4: Billy Butcher - Jeans + Tank
(4, 13, 1, 59.99),
(4, 25, 1, 24.99),

-- Order 5: The Deep - Ankle Boots + Scarf
(5, 21, 1, 89.99),
(5, 20, 1, 34.99),

-- Order 6: Bruce Wayne - Chinos
(6, 4, 1, 49.99),

-- Order 7: Clark Kent - Hat (Cancelled)
(7, 26, 1, 19.99),

-- Order 8: Homelander - Oxford Shoes + Belt
(8, 22, 1, 109.99),
(8, 16, 1, 29.99),

-- Order 9: The Deep - Boho Skirt
(9, 23, 1, 44.99),

-- Order 10: Bruce Wayne - Leather Jacket + Sneakers
(10, 6, 1, 199.99),
(10, 17, 1, 59.99),

-- Orders 11-30: Selected previous orders
(11, 2, 1, 89.99),
(12, 28, 1, 69.99),
(12, 10, 1, 49.99),
(13, 29, 1, 44.99),
(13, 16, 1, 29.99),
(13, 19, 1, 34.99),
(14, 1, 1, 129.99),
(14, 4, 1, 49.99),
(15, 24, 1, 34.99),
(15, 5, 1, 19.99),
(15, 26, 1, 19.99),
(16, 14, 1, 69.99),
(16, 17, 1, 59.99),
(17, 9, 1, 69.99),
(17, 10, 1, 49.99),
(18, 32, 1, 24.99),
(19, 2, 1, 89.99),
(19, 17, 1, 69.99),
(20, 10, 1, 49.99),
(20, 25, 1, 19.99),
(20, 16, 1, 29.99),
(21, 3, 1, 39.99),
(21, 16, 1, 29.99),
(22, 8, 1, 59.99),
(22, 16, 1, 24.99),
(23, 25, 1, 24.99),
(23, 24, 1, 39.99),
(24, 2, 1, 89.99),
(25, 31, 1, 59.99),
(26, 6, 1, 199.99),
(27, 26, 1, 19.99),
(28, 21, 1, 89.99),
(29, 4, 1, 49.99),
(30, 19, 1, 39.99),

-- Orders 31-35: Last two weeks
(31, 16, 1, 29.99),
(32, 15, 1, 149.00),
(33, 19, 1, 39.99),
(34, 8, 1, 59.99),
(35, 2, 1, 49.99),
(35, 16, 1, 49.99),

-- Orders 36-42: This week
(36, 22, 1, 109.99),
(37, 4, 1, 49.99),
(38, 1, 1, 129.99),
(39, 14, 1, 69.99),
(40, 6, 1, 199.99),
(41, 8, 1, 59.99),
(42, 21, 1, 89.99),

-- Orders 43-50: Today
(43, 23, 1, 44.99),
(44, 9, 1, 69.99),
(44, 10, 1, 49.99),
(45, 14, 1, 69.99),
(46, 31, 1, 79.99),
(47, 32, 1, 24.99),
(48, 17, 1, 59.99),
(49, 24, 1, 34.99),
(50, 5, 1, 19.99);

-- Update orders table with correct total amounts calculated from order_items
UPDATE orders o 
SET total_amount = (
    SELECT SUM(oi.price * oi.quantity) 
    FROM order_items oi 
    WHERE oi.order_id = o.id
);

-- Add more customer orders with greater variety
INSERT INTO orders (id, user_id, total_amount, shipping_address, status, payment_status, created_at) VALUES
-- 6 months ago - heavy shopping period (holiday season)
(51, 1, 429.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 185 DAY) + INTERVAL 14 HOUR),
(52, 2, 319.96, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 184 DAY) + INTERVAL 9 HOUR),
(53, 6, 559.95, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 183 DAY) + INTERVAL 17 HOUR),
(54, 7, 179.97, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 182 DAY) + INTERVAL 10 HOUR),
(55, 8, 339.97, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 181 DAY) + INTERVAL 12 HOUR),
(56, 1, 259.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 180 DAY) + INTERVAL 15 HOUR),
(57, 2, 399.95, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 179 DAY) + INTERVAL 11 HOUR),

-- 5 months ago
(58, 6, 79.99, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 150 DAY) + INTERVAL 9 HOUR),
(59, 7, 124.98, 'Flatiron Building, New York', 'CANCELLED', 'REFUNDED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 145 DAY) + INTERVAL 14 HOUR),
(60, 8, 249.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 140 DAY) + INTERVAL 16 HOUR),
(61, 1, 189.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 136 DAY) + INTERVAL 10 HOUR),
(62, 2, 44.98, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 132 DAY) + INTERVAL 13 HOUR),

-- 4 months ago
(63, 6, 279.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 120 DAY) + INTERVAL 11 HOUR),
(64, 8, 95.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 115 DAY) + INTERVAL 15 HOUR),
(65, 1, 159.97, '1007 Mountain Drive, Gotham City', 'CANCELLED', 'REFUNDED', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 110 DAY) + INTERVAL 9 HOUR),

-- 3 months ago (spring shopping)
(66, 2, 224.97, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 95 DAY) + INTERVAL 12 HOUR),
(67, 6, 79.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 90 DAY) + INTERVAL 10 HOUR),
(68, 7, 349.97, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY) + INTERVAL 16 HOUR),
(69, 8, 149.97, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 80 DAY) + INTERVAL 13 HOUR),

-- 2 months ago
(70, 1, 69.98, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY) + INTERVAL 14 HOUR),
(71, 2, 199.97, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY) + INTERVAL 9 HOUR),
(72, 6, 289.97, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY) + INTERVAL 11 HOUR),
(73, 7, 174.97, 'Flatiron Building, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY) + INTERVAL 15 HOUR),

-- Recent recurring purchases (customer patterns)
(74, 1, 99.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY) + INTERVAL 10 HOUR),
(75, 1, 99.97, '1007 Mountain Drive, Gotham City', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY) + INTERVAL 14 HOUR),
(76, 1, 99.97, '1007 Mountain Drive, Gotham City', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY) + INTERVAL 13 HOUR),
(77, 2, 149.97, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY) + INTERVAL 9 HOUR),
(78, 2, 149.97, 'Kent Farm, Smallville', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY) + INTERVAL 11 HOUR),
(79, 6, 219.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY) + INTERVAL 15 HOUR),
(80, 6, 219.98, 'Vought Tower, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY) + INTERVAL 10 HOUR),

-- Additional recent orders with different patterns
(81, 7, 299.97, 'Flatiron Building, New York', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY) + INTERVAL 14 HOUR),
(82, 8, 74.98, 'Vought Tower, New York', 'DELIVERED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY) + INTERVAL 16 HOUR),
(83, 1, 249.97, '1007 Mountain Drive, Gotham City', 'CONFIRMED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY) + INTERVAL 11 HOUR),
(84, 2, 359.96, 'Kent Farm, Smallville', 'SHIPPED', 'PAID', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY) + INTERVAL 13 HOUR),
(85, 6, 84.98, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY) + INTERVAL 15 HOUR),
(86, 7, 169.98, 'Flatiron Building, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 9 HOUR),
(87, 8, 239.97, 'Vought Tower, New York', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY) + INTERVAL 14 HOUR),
(88, 1, 144.97, '1007 Mountain Drive, Gotham City', 'PENDING', 'PENDING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY) + INTERVAL 10 HOUR),
(89, 2, 59.98, 'Kent Farm, Smallville', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 12 HOUR),
(90, 6, 389.97, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 8 HOUR),
(91, 7, 199.97, 'Flatiron Building, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 5 HOUR),
(92, 8, 99.98, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 3 HOUR),
(93, 1, 154.97, '1007 Mountain Drive, Gotham City', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 1 HOUR),
(94, 2, 279.96, 'Kent Farm, Smallville', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 30 MINUTE),
(95, 6, 119.98, 'Vought Tower, New York', 'PENDING', 'PENDING', CURRENT_TIMESTAMP - INTERVAL 10 MINUTE);

-- Add order items for new orders
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
-- Order 51: Bruce Wayne - Multiple premium items
(51, 1, 1, 129.99),
(51, 6, 1, 199.99),
(51, 16, 1, 29.99),
(51, 20, 1, 34.99),
(51, 19, 1, 39.99),

-- Order 52: Clark Kent - Clothing haul
(52, 3, 2, 29.99),
(52, 4, 1, 49.99),
(52, 5, 1, 19.99),
(52, 7, 2, 39.99),
(52, 9, 1, 69.99),
(52, 10, 1, 49.99),

-- Order 53: Homelander - Premium shopping spree
(53, 1, 1, 129.99),
(53, 6, 1, 199.99),
(53, 15, 1, 149.00),
(53, 21, 1, 89.99),
(53, 22, 1, 109.99),

-- Order 54: Billy Butcher - Mixed items
(54, 5, 1, 19.99),
(54, 10, 1, 49.99),
(54, 16, 1, 29.99),
(54, 24, 1, 34.99),
(54, 18, 2, 19.99),

-- Order 55: The Deep - Men's collection
(55, 1, 1, 129.99),
(55, 4, 1, 49.99),
(55, 7, 1, 39.99),
(55, 8, 1, 59.99),
(55, 17, 1, 59.99),

-- Order 56: Bruce Wayne - Basic essentials
(56, 3, 1, 29.99),
(56, 5, 1, 19.99),
(56, 8, 1, 59.99),
(56, 17, 1, 59.99),
(56, 24, 1, 34.99),
(56, 26, 1, 19.99),
(56, 32, 1, 24.99),

-- Order 57: Clark Kent - High-end purchase
(57, 6, 1, 199.99),
(57, 15, 1, 149.00),
(57, 20, 1, 34.99),
(57, 19, 1, 39.99),

-- Orders 58-62: Single or dual item purchases
(58, 31, 1, 79.99),
(59, 4, 1, 49.99),
(59, 10, 1, 49.99),
(59, 16, 1, 24.99),
(60, 6, 1, 199.99),
(60, 20, 1, 34.99),
(60, 32, 1, 24.99),
(61, 2, 1, 89.99),
(61, 3, 2, 29.99),
(61, 26, 2, 19.99),
(62, 5, 1, 19.99),
(62, 26, 1, 24.99),

-- Orders 63-65: Mixed products
(63, 6, 1, 199.99),
(63, 16, 1, 29.99),
(63, 32, 1, 24.99),
(63, 18, 1, 19.99),
(64, 10, 1, 49.99),
(64, 20, 1, 34.99),
(64, 26, 1, 19.99),
(65, 4, 1, 49.99),
(65, 5, 1, 19.99),
(65, 16, 1, 29.99),
(65, 19, 1, 39.99),
(65, 26, 1, 19.99),

-- Orders 66-69: Seasonal shopping
(66, 7, 1, 39.99),
(66, 9, 1, 69.99),
(66, 14, 1, 69.99),
(66, 20, 1, 34.99),
(66, 26, 1, 19.99),
(67, 5, 2, 19.99),
(67, 24, 1, 34.99),
(67, 26, 1, 19.99),
(68, 1, 1, 129.99),
(68, 6, 1, 199.99),
(68, 26, 1, 19.99),
(69, 5, 1, 19.99),
(69, 10, 1, 49.99),
(69, 17, 1, 59.99),
(69, 26, 1, 19.99),

-- Orders 70-73: Regular shopping
(70, 5, 1, 19.99),
(70, 32, 1, 24.99),
(70, 26, 1, 19.99),
(71, 6, 1, 199.99),
(71, 5, 1, 19.99),
(71, 26, 1, 19.99),
(72, 1, 1, 129.99),
(72, 7, 1, 39.99),
(72, 8, 1, 59.99),
(72, 16, 1, 29.99),
(72, 19, 1, 39.99),
(73, 10, 1, 49.99),
(73, 19, 1, 39.99),
(73, 20, 1, 34.99),
(73, 32, 1, 24.99),
(73, 26, 1, 19.99),

-- Orders 74-80: Recurring purchases (same items)
(74, 3, 1, 29.99),
(74, 5, 1, 19.99),
(74, 32, 1, 24.99),
(74, 26, 1, 19.99),
(75, 3, 1, 29.99),
(75, 5, 1, 19.99),
(75, 32, 1, 24.99),
(75, 26, 1, 19.99),
(76, 3, 1, 29.99),
(76, 5, 1, 19.99),
(76, 32, 1, 24.99),
(76, 26, 1, 19.99),
(77, 9, 1, 69.99),
(77, 10, 1, 49.99),
(77, 18, 1, 19.99),
(77, 26, 1, 19.99),
(78, 9, 1, 69.99),
(78, 10, 1, 49.99),
(78, 18, 1, 19.99),
(78, 26, 1, 19.99),
(79, 6, 1, 199.99),
(79, 26, 1, 19.99),
(80, 6, 1, 199.99),
(80, 26, 1, 19.99),

-- Orders 81-95: Recent varied purchases
(81, 1, 1, 129.99),
(81, 6, 1, 199.99),
(81, 26, 1, 19.99),
(82, 3, 1, 29.99),
(82, 20, 1, 34.99),
(82, 26, 1, 19.99),
(83, 2, 1, 89.99),
(83, 4, 1, 49.99),
(83, 9, 1, 69.99),
(83, 19, 1, 39.99),
(84, 1, 1, 129.99),
(84, 6, 1, 199.99),
(84, 18, 1, 19.99),
(84, 26, 1, 19.99),
(85, 10, 1, 49.99),
(85, 32, 1, 24.99),
(85, 26, 1, 19.99),
(86, 9, 1, 69.99),
(86, 14, 1, 69.99),
(86, 18, 1, 19.99),
(86, 26, 1, 19.99),
(87, 2, 1, 89.99),
(87, 17, 1, 59.99),
(87, 22, 1, 109.99),
(88, 4, 1, 49.99),
(88, 5, 1, 19.99),
(88, 10, 1, 49.99),
(88, 26, 1, 19.99),
(89, 18, 1, 19.99),
(89, 24, 1, 34.99),
(89, 26, 1, 19.99),
(90, 1, 1, 129.99),
(90, 6, 1, 199.99),
(90, 16, 1, 29.99),
(90, 26, 1, 19.99),
(91, 2, 1, 89.99),
(91, 9, 1, 69.99),
(91, 19, 1, 39.99),
(92, 17, 1, 59.99),
(92, 19, 1, 39.99),
(93, 3, 1, 29.99),
(93, 10, 1, 49.99),
(93, 16, 1, 29.99),
(93, 20, 1, 34.99),
(93, 26, 1, 19.99),
(94, 1, 1, 129.99),
(94, 7, 2, 39.99),
(94, 17, 1, 59.99),
(94, 26, 1, 19.99),
(95, 9, 1, 69.99),
(95, 19, 1, 39.99),
(95, 26, 1, 19.99);

-- Update new orders with correct total amounts calculated from order_items
UPDATE orders o 
SET total_amount = (
    SELECT SUM(oi.price * oi.quantity) 
    FROM order_items oi 
    WHERE oi.order_id = o.id
)
WHERE id >= 51;

-- Add review data for older orders
INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES
(1, 6, 5, 'Perfect fit and exceptional quality. Worth every penny!', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 170 DAY)),
(2, 7, 4, 'Great casual jacket, but runs slightly large.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 165 DAY)),
(3, 8, 5, 'This shirt is amazingly comfortable and well-constructed.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 160 DAY)),
(4, 1, 4, 'Comfortable pants for daily wear. Good value.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 155 DAY)),
(6, 2, 5, 'This leather jacket is an absolute masterpiece. Best purchase ever!', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 150 DAY)),
(9, 6, 5, 'Warm, stylish and perfect for winter days.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 145 DAY)),
(10, 7, 3, 'Decent hoodie but the material is thinner than expected.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 140 DAY)),
(14, 8, 4, 'Beautiful dress that fits perfectly. Very versatile.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 135 DAY)),
(15, 1, 5, 'This coat is exceptional for cold weather. Many compliments!', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 130 DAY)),
(17, 2, 4, 'Comfortable sneakers but took a while to break in.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 125 DAY)),
(19, 6, 5, 'These sunglasses are both stylish and functional. Great UV protection.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 120 DAY)),
(22, 7, 5, 'These oxfords are elegant and surprisingly comfortable.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 115 DAY)),
(24, 8, 4, 'Good quality shorts. Perfect for summer.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 110 DAY)),
(31, 1, 5, 'Best running shoes I've owned. Great support and comfort.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 105 DAY)),
(32, 2, 5, 'High quality yoga mat with excellent grip. Worth the price.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 100 DAY));

-- Update product ratings based on all reviews
UPDATE products p 
SET average_rating = (
    SELECT AVG(rating) 
    FROM reviews r 
    WHERE r.product_id = p.id
),
review_count = (
    SELECT COUNT(*) 
    FROM reviews r 
    WHERE r.product_id = p.id
)
WHERE EXISTS (
    SELECT 1 
    FROM reviews r 
    WHERE r.product_id = p.id
);

-- Reset ratings for products without actual customer reviews
UPDATE products p
SET average_rating = NULL, 
    review_count = NULL
WHERE NOT EXISTS (
    SELECT 1 
    FROM reviews r 
    WHERE r.product_id = p.id
);

-- Update phone numbers to standard numeric format
UPDATE users SET phone_number = '1555123001' WHERE id = 1; -- Bruce Wayne
UPDATE users SET phone_number = '1555123002' WHERE id = 2; -- Clark Kent
UPDATE users SET phone_number = '1555123003' WHERE id = 3; -- Tony Stark
UPDATE users SET phone_number = '1555123004' WHERE id = 4; -- Steve Rogers
UPDATE users SET phone_number = '1555123005' WHERE id = 5; -- Natasha Romanoff
UPDATE users SET phone_number = '1555123006' WHERE id = 6; -- Homelander
UPDATE users SET phone_number = '1555123007' WHERE id = 7; -- Billy Butcher
UPDATE users SET phone_number = '1555123008' WHERE id = 8; -- The Deep