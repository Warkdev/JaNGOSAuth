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
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'TEST','3D0D99423E31FCC67A6745EC89D70D700344BC76','640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369','39F419EC214004274C90118C432208D5F8357401D3F45D18CED70BE1F0577CED','8A35F4454D75197D09195EF686C5C94B57361CD8F0143AE24D733BE76F5BB0D2','test@test.com','2015-03-07 14:57:11','127.0.0.1',0,0,'2016-02-03 22:03:21',3,2),(5,'BANNED','5A31EA4791DCB33648008D0C5C260BAAA37E2A9A','9452722072081247AB2A26FC453ABE2BF7D756E7FAEF54D7F5121AFE38A125F1E9C5072661934A52','5302E5C33E857AC234B1F07373D6C8F70511E6AF14EAA2FEEE4FDBE01CCAAD7C','C03C8D8CD4EBA88AE6DA4CA2A037B8425E2EBC2E605C585F883AF08A264D53F8','banned@banned.com','2015-03-07 21:59:05','127.0.0.1',0,0,'2015-03-07 22:02:19',1,2),(6,'LOCK','55D367D2B75917788824A32EC4DF154E7E73A5D7','3545163EA7746B0E25951999A09392FF4CC5542DC655D1DA10649F104935B819C126B4473BD8FE29','33D1A68208E962CC5A25D6B81CF0B71D6C6CEC15A0EB5E3F38BF5BB5FD7345F6','677C7399A31D41EF5AFB72AB0263815A55846E664E11A4C5A5C5F7F18B0AE25','lock@lock.com','2015-03-07 21:59:16','127.0.0.1',0,1,'2015-03-07 22:01:07',1,2),(7,'FAILED','F03C62CAB86642D970987F254247E4189861A439',NULL,'454C357FC1AA25449FF5E9ED3DC9ABFE5C11D88DEDF5FB07A2854F0850409223','226C6E9AFA34C5F8BBF4CD3E775997E92CBA1D45E944DB85771F940285B45476','failed@failed.com','2015-03-08 08:45:57','0.0.0.0',5,1,'2015-03-08 08:45:57',1,2);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `account_roles`
--

LOCK TABLES `account_roles` WRITE;
/*!40000 ALTER TABLE `account_roles` DISABLE KEYS */;
INSERT INTO `account_roles` VALUES (1,3),(5,2),(6,1),(7,4);
/*!40000 ALTER TABLE `account_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bannedaccount`
--

LOCK TABLES `bannedaccount` WRITE;
/*!40000 ALTER TABLE `bannedaccount` DISABLE KEYS */;
INSERT INTO `bannedaccount` VALUES (1,'2015-03-07 21:20:05',NULL,1,'testing',1,5),(2,'2015-10-11 21:11:36',NULL,5,'testing api',0,1),(4,'2015-10-11 21:16:05','2016-01-09 22:15:32',5,'testing api',0,1),(5,'2015-10-11 21:24:06',NULL,5,'testing api',0,1),(6,'2015-10-24 17:57:03',NULL,1,'because I wanted to do so',0,1),(7,'2015-10-24 17:57:36',NULL,1,'And then do it again !',0,1);
/*!40000 ALTER TABLE `bannedaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bannedip`
--

LOCK TABLES `bannedip` WRITE;
/*!40000 ALTER TABLE `bannedip` DISABLE KEYS */;
INSERT INTO `bannedip` VALUES (2,'127.0.0.1','2015-10-13 19:45:09','2016-02-10 20:44:59',1,'Test my energy',0),(3,'127.0.0.1','2016-02-01 21:16:03','2016-02-10 20:45:11',1,'Test my energy',0),(4,'153.124.12.1','2016-02-01 21:16:03','2016-02-10 20:45:11',1,'Test',1);
/*!40000 ALTER TABLE `bannedip` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `locale`
--

LOCK TABLES `locale` WRITE;
/*!40000 ALTER TABLE `locale` DISABLE KEYS */;
INSERT INTO `locale` VALUES (0,'English','enGB'),(1,'Korean','koKR'),(2,'French','frFR'),(3,'German','deDE'),(4,'Chinese','zhCN'),(5,'Taiwanese','zhTW'),(6,'Spanish Spain','esES'),(7,'Spanish Latin America','esMX'),(8,'Russian','ruRU'),(9,'English US','enUS');
/*!40000 ALTER TABLE `locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `parameter1`
--

LOCK TABLES `parameter1` WRITE;
/*!40000 ALTER TABLE `parameter1` DISABLE KEYS */;
INSERT INTO `parameter1` VALUES (1,'defaultLocale','enGB'),(2,'authPort','3724'),(3,'authAddress','127.0.0.1'),(4,'authVersion','v0.1 BETA'),(5,'maxFailedAttempt','5'),(6,'minSupportedBuild','5875'),(7,'maxSupportedBuild','5875'),(8,'generateNewKeyOnStartup','true'),(9,'keyLength','128'),(10,'serverKey','atX1ohgGZj05fKvR6eSqTQ==');
/*!40000 ALTER TABLE `parameter1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realm`
--

LOCK TABLES `realm` WRITE;
/*!40000 ALTER TABLE `realm` DISABLE KEYS */;
INSERT INTO `realm` VALUES (1,'je4W PvE','127.0.0.1',3050,0,1,0,1000,0,0,0,0,0,0),(2,'je4W PvP','127.0.0.1',3081,1,2,0,1000,0,0,0,0,0,0),(3,'je4W RP PvE','127.0.0.1',3082,6,3,0,1000,0,0,0,0,0,0),(4,'je4W RP PvP','127.0.0.1',3083,8,3,0,1000,0,0,0,0,0,0),(5,'je4W Testing','127.0.0.1',3100,8,3,0,1000,0,0,0,0,0,0),(6,'je4W PvP2','127.0.0.1',3012,1,2,0,1000,0,0,0,0,0,0),(7,'je4W PvE2','127.0.0.1',3042,0,1,0,1000,0,0,0,0,0,0);
/*!40000 ALTER TABLE `realm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realm_account`
--

LOCK TABLES `realm_account` WRITE;
/*!40000 ALTER TABLE `realm_account` DISABLE KEYS */;
INSERT INTO `realm_account` VALUES (1,1,2),(1,2,4);
/*!40000 ALTER TABLE `realm_account` ENABLE KEYS */;
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

-- Dump completed on 2016-02-04 16:52:24
