CREATE DATABASE  IF NOT EXISTS `hlmng` /*!40100 DEFAULT CHARACTER SET latin1 */;

-- Host: localhost    Database: hlmng
-- ------------------------------------------------------
-- Server version	5.6.19-1~exp1ubuntu2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE `hlmng`;
DROP TABLE IF EXISTS `qrcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrcode` (
  `qrCodeID` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` varchar(19) NOT NULL,
  `claimedAt` varchar(19),
  `payload` varchar(100) NOT NULL UNIQUE,
  `role` varchar(25) NOT NULL,
  `userIDFK` int(11) ,
  `eventIDFK` int(11) DEFAULT NULL,
  PRIMARY KEY (`qrcodeID`),
  KEY `userIDFK_qr` (`userIDFK`),
  CONSTRAINT `userIDFK_qr` FOREIGN KEY (`userIDFK`) REFERENCES `user` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  KEY `eventIDFK_qr` (`eventIDFK`),
  CONSTRAINT `eventIDFK_qr` FOREIGN KEY (`eventIDFK`) REFERENCES `event` (`eventID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `qrcode` WRITE;
INSERT INTO `qrcode` VALUES (1,'2015-06-21 22:59:59','2015-06-21 23:09:13','SECRET PAYLOAD111','jury',1,1);
INSERT INTO `qrcode` VALUES (2,'2015-06-22 12:20:23',NULL,'SECRET PAYLOAD222','author',2,1);
UNLOCK TABLES;