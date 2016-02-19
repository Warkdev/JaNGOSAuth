CREATE DATABASE  IF NOT EXISTS `jangosauth` /*!40100 DEFAULT CHARACTER SET utf8 */;    
USE `jangosauth`;  

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the account',
  `name` varchar(30) NOT NULL COMMENT 'Name of the account, used within the WoW client to logon.',
  `hash_pass` varchar(40) NOT NULL DEFAULT '' COMMENT 'Hash of the account, according to SRP6 spec: H(I | ":" | P) where H is SHA1 algorithm. Generated at account creation time.',
  `sessionkey` longtext COMMENT 'Storing the session key for reconnect operations.',
  `verifier` longtext COMMENT 'According to the SRP6 specs, this is the client''s password verifier. It is calculated at the first authentication time (avoiding to recalculate hash each time).',
  `salt` longtext COMMENT 'According to the SRP6 specs, this is the server salt. It is calculated at the first authentication time (avoiding to recalculate hash each time).',
  `email` varchar(50) DEFAULT NULL COMMENT 'Account Owner''s email.',
  `creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation time (for records).',
  `lastIP` varchar(30) NOT NULL DEFAULT '0.0.0.0' COMMENT 'Last IP used for this account.',
  `failedattempt` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of failed attempt made for this account.',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Indicates whether this account is locked or not.',
  `lastlogin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of the last login performed by this account.',
  `fk_prefrealm` int(11) DEFAULT NULL COMMENT 'ID of the preferred realm for this account.',
  `fk_locale` int(11) DEFAULT NULL COMMENT 'The locale used by this client',
  `online` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Flag value indicating whether this account is already online or not.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_account_locale_idx` (`fk_locale`),
  KEY `fk_account_realm_idx` (`fk_prefrealm`),
  CONSTRAINT `fk_account_locale` FOREIGN KEY (`fk_locale`) REFERENCES `locale` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_account_realm` FOREIGN KEY (`fk_prefrealm`) REFERENCES `realm` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1 COMMENT='Account table represents an jE4W account compatible with WoW client v1.12.1.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_roles`
--

DROP TABLE IF EXISTS `account_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_roles` (
  `fk_account` int(11) NOT NULL,
  `fk_roles` int(11) NOT NULL,
  PRIMARY KEY (`fk_roles`,`fk_account`),
  KEY `fk_account_idx` (`fk_account`),
  CONSTRAINT `fk_account` FOREIGN KEY (`fk_account`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles` FOREIGN KEY (`fk_roles`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This table holds the relationships between an account and his assigned roles.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bannedaccount`
--

DROP TABLE IF EXISTS `bannedaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bannedaccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the record.',
  `bandate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Date of the ban.',
  `unban` timestamp NULL DEFAULT NULL COMMENT 'Date of the unban (may be null for unlimited ban).',
  `fk_bannedby` int(11) NOT NULL COMMENT 'Foreign Key to account indicating who is the requestor of the ban.',
  `reason` varchar(255) NOT NULL COMMENT 'Short message indicating why this account has been banned.',
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Indicate whether this ban record is active or not.',
  `fk_bannedaccount` int(11) NOT NULL COMMENT 'ID of the banned account.',
  PRIMARY KEY (`id`),
  KEY `fk_ban_account_idx` (`fk_bannedaccount`),
  KEY `fk_banisher_account_idx` (`fk_bannedby`),
  CONSTRAINT `fk_ban_account` FOREIGN KEY (`fk_bannedaccount`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_banisher_account` FOREIGN KEY (`fk_bannedby`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='Banned account represents all the account banned records with the reasons given.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bannedip`
--

DROP TABLE IF EXISTS `bannedip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bannedip` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'The id of the record.',
  `ip` varchar(30) NOT NULL COMMENT 'The banned IP.',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Date of the ban.',
  `unban` timestamp NULL DEFAULT NULL COMMENT 'Date of the unban (may be null for unlimited ban).',
  `fk_bannedby` int(11) NOT NULL COMMENT 'Foreign Key to account indicating who is the requestor of the ban.',
  `reason` varchar(255) NOT NULL COMMENT 'Short message indicating why this account has been banned.',
  `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Indicate whether this ban record is active or not.',
  PRIMARY KEY (`id`),
  KEY `fk_bannedip_bannedby_idx` (`fk_bannedby`),
  CONSTRAINT `fk_bannedip_bannedby` FOREIGN KEY (`fk_bannedby`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Table reporting all the IP banned with their reasons.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commands`
--

DROP TABLE IF EXISTS `commands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(35) NOT NULL COMMENT 'Name of the command',
  `description` varchar(60) NOT NULL COMMENT 'A short description of the command.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='This table holds all the commands available in game or through the gui interface.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commands_roles`
--

DROP TABLE IF EXISTS `commands_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commands_roles` (
  `fk_roles` int(11) NOT NULL,
  `fk_commands` int(11) NOT NULL,
  PRIMARY KEY (`fk_roles`,`fk_commands`),
  KEY `fk_commands_idx` (`fk_commands`),
  CONSTRAINT `fk_commands_roles` FOREIGN KEY (`fk_commands`) REFERENCES `commands` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_commands` FOREIGN KEY (`fk_roles`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This table holds the relationship between the commands and the roles.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `locale`
--

DROP TABLE IF EXISTS `locale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locale` (
  `id` int(11) NOT NULL COMMENT 'Locale ID.',
  `locale` varchar(30) NOT NULL COMMENT 'Locale common name.',
  `localeString` varchar(45) NOT NULL COMMENT 'String like the WoW client is sending it to the server.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Locale represents all locale supported by the WoW client.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parameter1`
--

DROP TABLE IF EXISTS `parameter1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameter1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the parameter, no meaning.',
  `param` varchar(50) NOT NULL COMMENT 'key of the parameter',
  `val` longtext NOT NULL COMMENT 'Value of the parameter.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='Table parameters contains a range of parameters ordered following key/value';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realm`
--

DROP TABLE IF EXISTS `realm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `realm` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the realm.',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT 'Name of the realm.',
  `address` varchar(32) NOT NULL DEFAULT '127.0.0.1' COMMENT 'Address of the realm, may be a name of an ip.',
  `port` int(11) NOT NULL DEFAULT '8085' COMMENT 'Listen port of the realm.',
  `fk_realmtype` int(11) NOT NULL COMMENT 'Foreign key to the Realm Type.',
  `fk_timezone` int(11) NOT NULL COMMENT 'Foreign key to the timezone.',
  `population` float NOT NULL DEFAULT '0' COMMENT 'Population calculated from (playerCount / maxPlayerCount * 2)',
  `maxPlayers` int(11) NOT NULL DEFAULT '1000' COMMENT 'The maximum number of players allowed on this realm.',
  `countPlayers` int(11) NOT NULL DEFAULT '0' COMMENT 'The number of players actually created on this realm.',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Means that this realm is invalid and must not be shown to the client.',
  `offline` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Means that this realm is offline.',
  `showversion` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Means that the version of this realm must be shown to the client.',
  `newplayers` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Means that only the new players may join this realm.',
  `recommended` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Display the recommended option in the client.',
  PRIMARY KEY (`id`),
  KEY `fk_realm_type_idx` (`fk_realmtype`),
  KEY `fk_realm_timezone_idx` (`fk_timezone`),
  CONSTRAINT `fk_realm_timezone` FOREIGN KEY (`fk_timezone`) REFERENCES `realmtimezone` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_realm_type` FOREIGN KEY (`fk_realmtype`) REFERENCES `realmtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1 COMMENT='Representation of all the realms known by this authentication server.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realm_account`
--

DROP TABLE IF EXISTS `realm_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `realm_account` (
  `fk_account` int(11) NOT NULL COMMENT 'Foreign key to the account.',
  `fk_realm` int(11) NOT NULL COMMENT 'Foreign key to the realm.',
  `numChars` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Provides the number of characters for this realm.',
  PRIMARY KEY (`fk_account`,`fk_realm`),
  KEY `fk_ar_realm_idx` (`fk_realm`),
  CONSTRAINT `fk_ar_account` FOREIGN KEY (`fk_account`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ar_realm` FOREIGN KEY (`fk_realm`) REFERENCES `realm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Realm - Account represents the relationship between an account and a realm. ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realmtimezone`
--

DROP TABLE IF EXISTS `realmtimezone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `realmtimezone` (
  `id` int(11) NOT NULL COMMENT 'id of the timezone like sent by the client.',
  `name` varchar(25) DEFAULT NULL COMMENT 'Meaning of  the id.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Represents all the time zones supported by the WoW client.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `realmtype`
--

DROP TABLE IF EXISTS `realmtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `realmtype` (
  `id` int(11) NOT NULL COMMENT 'Id of  the Realm Type, correspond to the byte sent to the client.',
  `type` varchar(30) DEFAULT NULL COMMENT 'The string representing the type of realm for an easy understanding.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='RealmType is an enumeration of all possible type of realm for WoW classic v1.12.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the role.',
  `name` varchar(20) NOT NULL COMMENT 'Name of the role',
  `description` varchar(200) NOT NULL COMMENT 'A short description about the role.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='This table holds all the roles defined for security purposes.';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-19 17:38:02
