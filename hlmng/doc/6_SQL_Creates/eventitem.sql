CREATE DATABASE  IF NOT EXISTS `hlmng` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `hlmng`;
-- MySQL dump 10.13  Distrib 5.6.19, for debian-linux-gnu (x86_64)
--
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

--
-- Table structure for table `eventitem`
--

DROP TABLE IF EXISTS `eventitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eventitem` (
  `eventItemID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  `description` varchar(350) NOT NULL,
  `date` varchar(25) NOT NULL,
  `startTime` varchar(25) NOT NULL,
  `endTime` varchar(25) NOT NULL,
  `roomIDFK` int(11) NOT NULL,
  `eventIDFK` int(11) NOT NULL,
  `speakerIDFK` int(11) NOT NULL,
  PRIMARY KEY (`eventItemID`),
  KEY `eventIDFK_ei` (`eventIDFK`),
  KEY `roomIDFK_ei` (`roomIDFK`),
  KEY `speakerIDFK_ei` (`speakerIDFK`),
  CONSTRAINT `eventIDFK_ei` FOREIGN KEY (`eventIDFK`) REFERENCES `event` (`eventID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `roomIDFK_ei` FOREIGN KEY (`roomIDFK`) REFERENCES `eventroom` (`eventroomID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `speakerIDFK_ei` FOREIGN KEY (`speakerIDFK`) REFERENCES `speaker` (`speakerID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
