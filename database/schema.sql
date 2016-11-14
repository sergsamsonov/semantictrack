-- MySQL dump 10.13  Distrib 5.7.13, for Win64 (x86_64)
--
-- Host: localhost    Database: bugtracking
-- ------------------------------------------------------
-- Server version	5.7.13

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
-- Table structure for table `acronyms`
--

DROP TABLE IF EXISTS `acronyms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acronyms` (
  `id` int(11) NOT NULL COMMENT 'Identifier of acronym',
  `acronym` varchar(10) NOT NULL COMMENT 'Acronym',
  `interpretation` varchar(50) DEFAULT NULL COMMENT 'Interpretation of acronym',
  PRIMARY KEY (`id`),
  UNIQUE KEY `acronym_UNIQUE` (`acronym`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User domain acronyms';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_permission`
--

DROP TABLE IF EXISTS `group_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_permission` (
  `groupid` int(11) NOT NULL COMMENT 'Group identifier',
  `permissionid` int(11) NOT NULL COMMENT 'Permission identifier',
  PRIMARY KEY (`groupid`,`permissionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `groupid` int(11) NOT NULL COMMENT 'Group identifier',
  `groupname` varchar(10) NOT NULL COMMENT 'Group name',
  `description` varchar(45) DEFAULT NULL COMMENT 'Group description',
  PRIMARY KEY (`groupid`),
  UNIQUE KEY `groupname_UNIQUE` (`groupname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Security Groups';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lemmas`
--

DROP TABLE IF EXISTS `lemmas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lemmas` (
  `lemma` varchar(20) NOT NULL COMMENT 'Lemma',
  `ticknum` int(11) DEFAULT '0' COMMENT 'Number of tickets with lemma''s derivation',
  PRIMARY KEY (`lemma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Lemmas extracted from tickets';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `permissionid` int(11) NOT NULL COMMENT 'Permission identifier',
  `name` varchar(10) NOT NULL COMMENT 'Permission name',
  `description` varchar(45) DEFAULT NULL COMMENT 'Permission description',
  PRIMARY KEY (`permissionid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Permissions to access resources';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `systvariables`
--

DROP TABLE IF EXISTS `systvariables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `systvariables` (
  `code` varchar(10) NOT NULL COMMENT 'System variable code',
  `description` varchar(45) DEFAULT NULL COMMENT 'System variable description',
  `value` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='System variables';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL COMMENT 'Task identifier',
  `name` varchar(15) NOT NULL COMMENT 'Task name',
  `description` varchar(45) DEFAULT NULL COMMENT 'Task description',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tasks';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickets` (
  `number` int(11) NOT NULL COMMENT 'Ticket number',
  `issue` varchar(70) DEFAULT NULL COMMENT 'Short description of issue',
  `issuedescr` text COMMENT 'Detailed description of issue',
  `solution` varchar(70) DEFAULT NULL COMMENT 'Short description of solution',
  `solutiondet` text COMMENT 'Detailed description of solution',
  `sourcefiles` text COMMENT 'List of source files',
  `lemmas` text COMMENT 'Lemmas extracted from ticket''s fields: issue, issuedescr, solution, solutiondet',
  `issuelem` text COMMENT 'Lemmas extracted from field issue',
  `issdesclem` text COMMENT 'Lemmas extracted from field issuedescr',
  `solutionlem` text COMMENT 'Lemmas extracted from field solution',
  `soldetlem` text COMMENT 'Lemmas extracted from field solutiondet',
  `lemmasnum` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of tokens from field lemmas',
  `issuenum` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of tokens from field issuelem',
  `issdescnum` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of tokens from field issdesclem',
  `solutnum` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of tokens from field solutionlem',
  `soldetnum` int(11) NOT NULL DEFAULT '0' COMMENT 'Number of tokens from field soldetlem',
  `user` int(11) DEFAULT NULL COMMENT 'Identifiier of user who entered a ticket',
  `responsible` int(11) DEFAULT NULL COMMENT 'Identifier of user assigned to an active ticket task',
  `status` int(11) DEFAULT NULL COMMENT 'Current ticket status identifier',
  `ticktype` int(11) DEFAULT NULL COMMENT 'Ticket type identifier',
  `task` int(11) DEFAULT NULL COMMENT 'Active ticket task identifier',
  PRIMARY KEY (`number`),
  FULLTEXT KEY `ISSDESCIDX` (`issdesclem`),
  FULLTEXT KEY `ISSUEIDX` (`issuelem`),
  FULLTEXT KEY `SOLUTIDX` (`solutionlem`),
  FULLTEXT KEY `SOLDETIDX` (`soldetlem`),
  FULLTEXT KEY `LEMMASIDX` (`lemmas`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Tickets';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticketstat`
--

DROP TABLE IF EXISTS `ticketstat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticketstat` (
  `id` int(11) NOT NULL COMMENT 'Ticket status identifier',
  `name` varchar(15) NOT NULL COMMENT 'Ticket status name',
  `description` varchar(45) DEFAULT NULL COMMENT 'Ticket status description',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tickets status';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticktypes`
--

DROP TABLE IF EXISTS `ticktypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticktypes` (
  `id` int(11) NOT NULL COMMENT 'Ticket type identifier',
  `name` varchar(15) NOT NULL COMMENT 'Ticket type name',
  `description` varchar(45) DEFAULT NULL COMMENT 'Ticket type description',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tickets types';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `userid` int(11) NOT NULL COMMENT 'User identifier',
  `groupid` int(11) NOT NULL COMMENT 'Group identifier',
  PRIMARY KEY (`userid`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL COMMENT 'User''s identifier',
  `login` varchar(10) NOT NULL COMMENT 'User''s login',
  `password` varchar(70) DEFAULT NULL COMMENT 'User''s password',
  `email` varchar(45) DEFAULT NULL COMMENT 'User''s email address',
  `firstname` varchar(45) DEFAULT NULL COMMENT 'User''s first name',
  `middlename` varchar(45) DEFAULT NULL COMMENT 'User''s middle name',
  `lastname` varchar(45) DEFAULT NULL COMMENT 'User''s last name',
  `enabled` tinyint(1) DEFAULT '0' COMMENT 'Flag is set to enable/disable access to company resources',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Users';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-31 12:25:11
