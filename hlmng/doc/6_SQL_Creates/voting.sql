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
  DROP TABLE IF EXISTS `voting`;
  /*!40101 SET @saved_cs_client     = @@character_set_client */;
  /*!40101 SET character_set_client = utf8 */;
  CREATE TABLE `voting` (
    `votingID` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `juryCount` int(11) NOT NULL,
    `status` varchar(25) NOT NULL,
    `sliderMaxValue` int(11) NOT NULL,
    `votingStarted` varchar(8) ,
    `votingDuration` varchar(8) NOT NULL,
    `arithmeticMode` varchar(25) NOT NULL,
    `presentationMinTime` varchar(8) NOT NULL,
    `presentationMaxTime` varchar(8) NOT NULL,
    `presentationStarted` varchar(8),
    `presentationEnded` varchar(8),
    `inTimeScoreWeight` int(11) NOT NULL,
    `round` int(11) NOT NULL,
    `eventIDFK` int(11) NOT NULL,
    PRIMARY KEY (`votingID`),
    KEY `eventIDFK_vg` (`eventIDFK`),
    CONSTRAINT `eventIDFK_vg` FOREIGN KEY (`eventIDFK`) REFERENCES `event` (`eventID`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  /*!40101 SET character_set_client = @saved_cs_client */;

  LOCK TABLES `voting` WRITE;
  INSERT INTO `voting` VALUES (1,'Voting 1',15,'running',10,NULL,'00:00:50','median','00:05:00','00:07:00','14:30:30','14:35:50',2,1,1);
  INSERT INTO `voting` VALUES (2,'Voting 2',15,'voting',10,'14:39:57','00:00:50','median','00:05:00','00:07:00',NULL,NULL,1,1,1);
  UNLOCK TABLES;