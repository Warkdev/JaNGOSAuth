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
INSERT INTO `account` VALUES (1,'TEST','3D0D99423E31FCC67A6745EC89D70D700344BC76','C464DDF8BC3F7F0D9EF0BF76DB9C29DD474AA28F252EC5C750A695717C5D521DDFE135DC5238C1AB','C95E65CB5C73F1FD07D8930797367682D3B133CBF5EDA065540DEA346422F3C','2C59576CFA862C9457521753616E98103759B50B3DFCDE60C5E591B87A76BC6C','test@test.com','2015-03-07 14:57:11','127.0.0.1',0,0,'2016-02-14 20:00:45',NULL,2,0),(2,'ONLINE','81E65346AEEBC9CA395DBEE21D96FACA4CAFEE00',NULL,NULL,NULL,'online@online.com','2016-02-07 12:32:40','0.0.0.0',0,0,'2016-02-07 11:56:38',NULL,4,1),(5,'BANNED','418BF656C7A79EAC48B98C487D63723B3D4EC472','640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369','5302E5C33E857AC234B1F07373D6C8F70511E6AF14EAA2FEEE4FDBE01CCAAD7C','C03C8D8CD4EBA88AE6DA4CA2A037B8425E2EBC2E605C585F883AF08A264D53F8','banned@banned.com','2015-03-07 21:59:05','127.0.0.1',0,0,'2016-02-05 17:36:45',NULL,2,0),(6,'LOCK','CB5E339BBBB4D40D2B665DB7C83EC80E99109F5E','3545163EA7746B0E25951999A09392FF4CC5542DC655D1DA10649F104935B819C126B4473BD8FE29',NULL,NULL,'lock@lock.com','2015-03-07 20:59:16','127.0.0.1',0,1,'2015-03-07 21:01:07',NULL,1,0),(7,'FAILED','F03C62CAB86642D970987F254247E4189861A439',NULL,'454C357FC1AA25449FF5E9ED3DC9ABFE5C11D88DEDF5FB07A2854F0850409223','226C6E9AFA34C5F8BBF4CD3E775997E92CBA1D45E944DB85771F940285B45476','failed@failed.com','2015-03-08 08:45:57','0.0.0.0',0,0,'2015-03-08 08:45:57',NULL,7,0);

--
-- Dumping data for table `account_roles`
--

LOCK TABLES `account_roles` WRITE;
/*!40000 ALTER TABLE `account_roles` DISABLE KEYS */;
INSERT INTO `account_roles` VALUES (7,4);
/*!40000 ALTER TABLE `account_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bannedaccount`
--

LOCK TABLES `bannedaccount` WRITE;
/*!40000 ALTER TABLE `bannedaccount` DISABLE KEYS */;
INSERT INTO `bannedaccount` VALUES (1,'2015-03-07 21:20:05','2017-02-04 22:17:27',5,'my new test',1,5),(2,'2015-10-11 21:11:36',NULL,5,'testing api',0,1),(4,'2015-10-11 21:16:05','2016-01-09 22:15:32',5,'testing api',0,1),(5,'2015-10-11 21:24:06',NULL,5,'testing api',0,1),(6,'2015-10-24 17:57:03',NULL,1,'because I wanted to do so',0,1),(7,'2015-10-24 17:57:36',NULL,1,'And then do it again !',0,1),(9,'2016-02-11 20:37:29','2016-04-11 19:37:29',7,'test',0,7),(10,'2016-02-11 21:16:38','2016-04-11 20:16:38',7,'test',0,7),(11,'2016-02-13 10:04:10','2016-04-13 09:04:10',1,'test',0,1);
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
INSERT INTO `commands_roles` VALUES (2,1),(1,3),(4,3),(1,4),(4,4),(1,7),(1,11),(1,12),(4,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(4,30);
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
INSERT INTO `parameter1` VALUES (1,'defaultLocale','enGB'),(2,'authPort','3724'),(3,'authAddress','127.0.0.1'),(4,'authVersion','v1.0'),(5,'maxFailedAttempt','5'),(6,'minSupportedBuild','5875'),(7,'maxSupportedBuild','5875'),(8,'authTimeout','30'),(9,'defaultRole','Player');
/*!40000 ALTER TABLE `parameter1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realm`
--

LOCK TABLES `realm` WRITE;
/*!40000 ALTER TABLE `realm` DISABLE KEYS */;
INSERT INTO `realm` VALUES (9,'Aegwynn','127.0.0.1',3500,8,2,0.2,1000,200,0,0,0,0,0),(10,'Arathi','127.0.0.1',3501,6,3,0.35,1000,350,0,0,0,0,0),(11,'Garona','127.0.0.1',3502,1,5,0.4,1000,400,0,0,0,0,0),(12,'Shadowsong','127.0.0.1',3503,0,1,0.8,1000,800,0,0,0,0,0),(13,'Sporeggar','127.0.0.1',3504,0,3,0.075,1000,75,0,0,0,0,0),(14,'Magtheridon','127.0.0.1',3505,6,2,0.125,1000,125,0,0,0,0,0),(15,'Malfurion','127.0.0.1',3506,8,5,0.012,1000,12,0,0,1,0,0),(16,'Frostwolf','127.0.0.1',3507,0,2,0,1000,0,0,0,1,0,0);
/*!40000 ALTER TABLE `realm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realm_account`
--

LOCK TABLES `realm_account` WRITE;
/*!40000 ALTER TABLE `realm_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `realm_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realmtimezone`
--

LOCK TABLES `realmtimezone` WRITE;
/*!40000 ALTER TABLE `realmtimezone` DISABLE KEYS */;
INSERT INTO `realmtimezone` VALUES (1,'English'),(2,'German'),(3,'French'),(5,'Spanish');
/*!40000 ALTER TABLE `realmtimezone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `realmtype`
--

LOCK TABLES `realmtype` WRITE;
/*!40000 ALTER TABLE `realmtype` DISABLE KEYS */;
INSERT INTO `realmtype` VALUES (0,'Normal'),(1,'PvP'),(6,'RP'),(8,'RP PvP');
/*!40000 ALTER TABLE `realmtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Player','Player is the default security level'),(2,'Gamemaster','Gamemaster has more rights than the player'),(4,'Guest','Guest has almost no permission in-game');
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

-- Dump completed on 2016-02-19 17:37:35
