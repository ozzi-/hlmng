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
  DROP TABLE IF EXISTS `presentationpause`;
  /*!40101 SET @saved_cs_client     = @@character_set_client */;
  /*!40101 SET character_set_client = utf8 */;
  CREATE TABLE `presentationpause` (
    `presentationpauseID` int(11) NOT NULL AUTO_INCREMENT,
    `start` varchar(8) NOT NULL,
    `stop` varchar(8),
	`votingIDFK` int(11) NOT NULL,
    PRIMARY KEY (`presentationpauseID`),
    KEY `votingIDFK_vg` (`votingIDFK`),
    CONSTRAINT `votingIDFK_pp` FOREIGN KEY (`votingIDFK`) REFERENCES `voting` (`votingID`) ON DELETE CASCADE ON UPDATE CASCADE
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  /*!40101 SET character_set_client = @saved_cs_client */;
