-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: jangosauth
-- ------------------------------------------------------
-- Server version	5.6.26-log

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
-- Dumping data for table `commands`
--

LOCK TABLES `commands` WRITE;
/*!40000 ALTER TABLE `commands` DISABLE KEYS */;
INSERT INTO `commands` VALUES (1,'all','All commands'),(2,'account:find','Allow to find accounts'),(3,'realm:login','Allow to login into a realm'),(4,'account:login','Allow to login an account'),(5,'account:create','Allow to create an account'),(6,'account:delete','Allow to delete an account'),(7,'account:update','Allow to update an account'),(8,'account:ban','Allow to manage  the ban of account/IP'),(9,'account:impersonate','Allow to impersonate an account'),(10,'account:lock','Allow to lock an account'),(11,'commands:find','Allow to find commands'),(12,'parameters:find','Allow to find parameters'),(13,'parameters:delete','Allow to delete parameters'),(14,'parameters:create','Allow  to create parameters'),(15,'parameters:update','Allow to update parameters'),(16,'timezone:find','Allow to find timezones'),(17,'realmtype:find','Allow to find realm types'),(18,'roles:find','Allow to find roles'),(19,'roles:delete','Allow to delete roles'),(20,'roles:create','Allow to create roles'),(21,'roles:update','Allow to update roles'),(22,'servers:find','Allow to find servers'),(23,'servers:delete','Allow to delete servers'),(24,'servers:create','Allow to create servers'),(25,'servers:update','Allow to update servers'),(26,'realms:find','Allow to find realms'),(27,'realms:delete','Allow to delete realms'),(28,'realms:create','Allow to create realms'),(29,'realms:update','Allow to update realms'),(30,'locale:find','Allow to find locale');
/*!40000 ALTER TABLE `commands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `commands_roles`
--

LOCK TABLES `commands_roles` WRITE;
/*!40000 ALTER TABLE `commands_roles` DISABLE KEYS */;
INSERT INTO `commands_roles` VALUES (1,1),(2,1),(3,1),(4,1),(5,2),(5,4),(5,9),(5,10),(5,12),(5,26);
/*!40000 ALTER TABLE `commands_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `parameter1`
--

LOCK TABLES `parameter1` WRITE;
/*!40000 ALTER TABLE `parameter1` DISABLE KEYS */;
INSERT INTO `parameter1` VALUES (1,'defaultLocale','enGB'),(2,'authPort','3724'),(3,'authAddress','127.0.0.1'),(4,'authVersion','v1.0'),(5,'maxFailedAttempt','5'),(6,'minSupportedBuild','5875'),(7,'maxSupportedBuild','5875'),(8,'authTimeout','30');
/*!40000 ALTER TABLE `parameter1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realmtimezone`
--

LOCK TABLES `realmtimezone` WRITE;
/*!40000 ALTER TABLE `realmtimezone` DISABLE KEYS */;
INSERT INTO `realmtimezone` VALUES (1,'Development'),(2,'United States'),(3,'Oceanic'),(4,'Latin America'),(5,'Tournament'),(6,'Korea'),(7,'Tournament'),(8,'English'),(9,'German'),(10,'French'),(11,'Spanish'),(12,'Russian'),(13,'Tournament'),(14,'Taiwan'),(15,'Tournament'),(16,'China'),(17,'CN1'),(18,'CN2'),(19,'CN3'),(20,'CN4'),(21,'CN5'),(22,'CN6'),(23,'CN7'),(24,'CN8'),(25,'Tournament'),(26,'Test Server'),(27,'Tournament'),(28,'QA Server'),(29,'CN9');
/*!40000 ALTER TABLE `realmtimezone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realmtype`
--

LOCK TABLES `realmtype` WRITE;
/*!40000 ALTER TABLE `realmtype` DISABLE KEYS */;
INSERT INTO `realmtype` VALUES (0,'Normal'),(1,'PvP'),(4,'Normal'),(6,'RP'),(8,'RP PvP');
/*!40000 ALTER TABLE `realmtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Player','Player is the default security level'),(2,'Gamemaster','Gamemaster has more rights than the player'),(3,'Administrator','Administrator has full access on the game management'),(4,'Guest','Guest has almost no permission in-game'),(5,'Technical','This role is for technical access (servers)');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-07 13:52:37
