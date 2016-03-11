/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.6.29 : Database - lucene
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`lucene` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `lucene`;

/*Table structure for table `t_item` */

DROP TABLE IF EXISTS `t_item`;

CREATE TABLE `t_item` (
  `id` varchar(32) NOT NULL COMMENT '主见id',
  `name` varchar(64) NOT NULL COMMENT '术语名称',
  `description` varchar(1024) NOT NULL COMMENT '术语解释',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_procedure` */

DROP TABLE IF EXISTS `t_procedure`;

CREATE TABLE `t_procedure` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `content` varchar(1024) NOT NULL COMMENT '内容',
  `path` varchar(512) NOT NULL COMMENT '路径',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_procedure_item` */

DROP TABLE IF EXISTS `t_procedure_item`;

CREATE TABLE `t_procedure_item` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `procedure_id` varchar(32) NOT NULL COMMENT '程序id',
  `item_id` varchar(32) NOT NULL COMMENT '术语id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
