-- MySQL dump 10.13  Distrib 8.0.41, for macos15 (x86_64)
--
-- Host: 127.0.0.1    Database: inventory
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1043 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1001,'MacBook Pro 2023','Electronics',8799,13),(1002,'Office Chair','Furniture',299.9,25),(1003,'Printer Paper (A4)','Office Supply',19.9,50),(1004,'Jabra Wireless Headphone','Electronics',699,30),(1005,'Full Cream Milk 1L','Groceries',7.5,40),(1006,'King Size Bed','Furniture',1999.9,3),(1007,'Smart LED TV 55\"','Electronics',2599,12),(1008,'Ergonomic Mesh Chair','Furniture',749,20),(1009,'Wireless Mechanical Keyboard','Accessories',499,18),(1010,'Whiteboard with Stand','Office Supply',159,30),(1011,'Dumbbell Set (20kg)','Fitness',299,50),(1012,'Electric Kettle','Appliances',119,35),(1013,'Bluetooth Speaker JBL','Electronics',349,30),(1014,'Adjustable Bed Frame','Furniture',1999,7),(1015,'Gaming Chair','Gaming',899,11),(1016,'Leather Wallet','Fashion',159,40),(1017,'Organic Honey 1L','Groceries',79,58),(1018,'Electric Toothbrush','Health & Wellness',259,22),(1019,'Nike Sports T-shirt','Sportswear',149,55),(1020,'Digital Drawing Tablet','Computers',1099,12),(1021,'Smart Air Purifier','Appliances',1499,15),(1022,'4K Action Camera','Photography',999,9),(1023,'Running Treadmill','Fitness',2999,5),(1024,'Designer Handbag','Fashion',2299,20),(1025,'Electric Rice Cooker','Appliances',249,45),(1026,'Wireless Earbuds','Electronics',199,18),(1027,'Gaming Monitor 27\"','Computers',1699,19),(1028,'Men’s Leather Shoes','Fashion',399,25),(1029,'Camping Tent 4-Person','Outdoor',699,14),(1030,'Car Vacuum Cleaner','Automotive',199,35),(1031,'Portable Power Bank 20000mAh','Accessories',129,60),(1033,'Stainless Steel Water Bottle','Outdoor',89,50),(1034,'Mechanical Wristwatch','Fashion',799,16),(1035,'Cordless Drill Set','Tools',599,20),(1036,'Noise Cancelling Headphones','Electronics',1299,86),(1037,'Smartphone Tripod','Photography',199,29),(1038,'Foldable Study Table','Furniture',349,20),(1039,'Massage Gun','Health & Wellness',499,11),(1040,'Leather Office Bag','Accessories',599,12),(1041,'back to my old days','uncle lim\'s mantra',0,83),(1042,'Iphone 20 Pro Max','Electronics',7999.99,8);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-14  8:55:14
