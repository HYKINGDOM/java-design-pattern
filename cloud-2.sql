-- MySQL dump 10.13  Distrib 8.0.13, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: cloud
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `content_article`
--

DROP TABLE IF EXISTS `content_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `content_article` (
  `id` varchar(36) NOT NULL,
  `content` longtext,
  `summary` varchar(300) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `effect_time` datetime(6) DEFAULT NULL,
  `publish_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_article`
--

LOCK TABLES `content_article` WRITE;
/*!40000 ALTER TABLE `content_article` DISABLE KEYS */;
INSERT INTO `content_article` VALUES ('08a706dd-c147-4625-bfdf-d33958eda657','<p>文章6</p><p>文章6</p><p>文章6</p>','文章6文章6文章6','文章6',_binary '\0','2019-09-06 10:55:36.198000','2019-09-06 10:55:36.198000'),('09faea99-8a89-45f0-8473-e1f172ac34bd','<p>文章11</p><p>文章11</p><p>文章11</p><p>文章11</p><p>文章11</p><p>文章11</p><p>文章11</p>','文章11文章11文章11文章11文章11文章11文章11','文章11',_binary '\0','2019-09-06 10:56:09.632000','2019-09-06 10:56:09.632000'),('0f0cc3b8-c71f-4a2d-b395-711825c4047d','<p>文章7</p><p>文章7</p><p>文章7</p>','文章7文章7文章7文章7文章7文章7文章7文章7文章7文章7','文章7',_binary '\0','2019-09-06 10:55:44.323000','2019-09-06 10:55:44.323000'),('0f2b33f0-6725-49f4-80f6-63645e4c2808','<p>文章9</p><p>文章9</p><p>文章9</p><p>文章9</p><p>文章9</p><p>文章9</p><p>文章9</p><p>文章9</p>','文章9文章9文章9文章9文章9文章9文章9文章9','文章9',_binary '\0','2019-09-06 10:55:58.386000','2019-09-06 10:55:58.386000'),('562b598b-95f7-4134-beff-d3065baa2759','<p>文章10</p><p>文章10</p><p>文章10</p><p>文章10</p><p>文章10</p><p>文章10</p><p>文章10</p>','文章10文章10文章10文章10文章10文章10文章10','文章10',_binary '\0','2019-09-06 10:56:04.394000','2019-09-06 10:56:04.394000'),('63ab0b4f-422a-42d6-a2ed-485d5c3788c2','<h2>更换主题</h2> <p>我们的样式使用的是less语言，我们定义了一些全局使用的变量</p> <p>您可以通过修改这些变量，达到变换样式的需求。</p> <h4>使用 <a href=\"https://github.com/heyui/hey-cli\" target=\"_blank\">hey-cli</a> 脚手架(推荐)</h4> <p>在自己定义的 var.less 文件中引用 heyui 的 var.less 文件，并按照自己的需求重新定义更换主题，然后在hey.js脚手架配置文件中设定全局引用文件。</p> <p>var.less，<a href=\"https://github.com/heyui/heyui-admin/blob/master/src/css/var.less\" target=\"_blank\">示例</a></p> <div><pre><code><span>@import</span> (less) <span>\"~/heyui/themes/var.less\"</span>;\n<span>//重新定义主题</span>\n<span>@primary-color:</span> <span>#FDA729</span>;\n<span>@red-color:</span> <span>#D64244</span>;\n<span>@green-color:</span> <span>#3cb357</span>;\n<span>@yellow-color:</span> <span>#EAC12C</span>;\n<span>@blue-color:</span> <span>#77A2DC</span>;</code></pre></div>  <p>hey-cli 配置文件 hey.js ，<a href=\"https://github.com/heyui/heyui-admin/blob/master/hey.conf.js\" target=\"_blank\">示例</a></p> <div><pre><code>globalVars: <span>\'./src/css/var.less\'</span>,</code></pre></div> <p>注意：使用这种引用方式，在vue单文件中也可以使用这些变量。</p> <h4>样式引用</h4> <p>您可以在入口js文件中import，也可以直接在app.less中引用。 <a href=\"https://github.com/heyui/heyui-admin/blob/master/src/css/app.less\" target=\"_blank\">示例</a></p> <div><pre><code><span>import</span> \"~<span>heyui</span>/<span>themes</span>/<span>common</span><span>.less</span>\";</code></pre></div> <h3>使用 vue-cli / 自己搭建webpack</h3> <h4>方式一</h4> <p>heyui定义了var.js文件，你可以通过lessLoader的globalVars参数定义全局变量</p> <p>你可以参考heyui-admin项目：<a href=\"https://github.com/heyui/heyui-admin\">链接</a></p> <p>一、项目中重定义参数</p> <div><pre><code><span>const</span> vars = <span>require</span>(<span>\'heyui/themes/var.js\'</span>);\n  <span>Object</span>.assign(vars, {\n    <span>\'primary-color\'</span>: <span>\'#3788ee\'</span>,\n    <span>\'link-color\'</span>: <span>\'#3788ee\'</span>,\n    <span>\'blue-color\'</span>: <span>\'#2d7bf4\'</span>,\n    <span>\'green-color\'</span>: <span>\'#0acf97\'</span>,\n    <span>\'yellow-color\'</span>: <span>\'#f9bc0b\'</span>,\n    <span>\'red-color\'</span>: <span>\'#f1556c\'</span>,\n  });\n  <span>module</span>.exports = vars;</code></pre></div>','更换主题 我们的样式使用的是less语言，我们定义了一些全局使用的变量 您可以通过修改这些变量，达到变换样式的需求。 使用 hey-cli 脚手架(推荐) 在自己定义的 var.less 文件中引用 ...','文章2',_binary '\0','2019-09-06 10:54:36.737000','2019-09-06 10:54:36.737000'),('70e78417-8d1b-4ad9-a192-b9b0ee60608f','<p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p><p>文章8</p>','文章8文章8文章8文章8文章8文章8文章8文章8文章8文章8','文章8',_binary '\0','2019-09-06 10:55:51.901000','2019-09-06 10:55:51.901000'),('752f9e6a-0b83-436b-bf2e-aa01aa322411','<p>文章3</p><p>文章3</p><p>文章3</p><p>文章3</p><p>文章3</p><p>文章3</p><p>文章3</p><p>文章3</p>','文章3文章3文章3文章3文章3文章3文章3文章3','文章3',_binary '\0','2019-09-06 10:55:12.776000','2019-09-06 10:55:12.776000'),('820a0f39-2462-48b8-ab58-af496ed9ecbb','<p>文章12</p><p>文章12</p><p>文章12</p><p>文章12</p><p>文章12</p><p>文章12</p><p>文章12</p>','文章12文章12文章12文章12文章12文章12文章12','文章12',_binary '\0','2019-09-06 10:56:32.467000','2019-09-06 10:56:32.467000'),('a8893494-e6da-4731-a2b0-0010390409f9','<p>文章4</p><p>文章4</p><p>文章4</p><p>文章4</p><p>文章4</p><p>文章4</p><p>文章4</p>','文章4文章4文章4文章4文章4文章4文章4','文章4',_binary '\0','2019-09-06 10:55:20.293000','2019-09-06 10:55:20.293000'),('dd5ce6c7-5eb3-47a6-949e-77e48366d0e5','<h2>修改一下关于HeyUI</h2> <p>HeyUI 是一套基于 Vue2.0 的开源 UI 组件库，主要服务于一些中后台产品。</p> <h3>特性</h3> <p>HeyUI提供的是一整套解决方案，所有的组件提供全局的可配置模式。</p> <ul><li>真正的数据驱动</li><li>全局的配置模式</li><li>数据字典化</li></ul> <h3>安装</h3> <p>推荐使用 npm 的方式安装。</p> <div><pre><code>npm install heyui</code></pre></div> <h3>示例</h3> <p>通过引用HeyUI，我们可以简单的写出一个应用界面。</p> <div> <div><span>复制</span><span>去Run执行</span></div> <pre><code><span>&lt;<span>template</span>&gt;</span>\n  <span>&lt;<span>div</span>&gt;</span>\n    <span>&lt;<span>Button</span> @<span>click.native</span>=<span>\"visible = true\"</span>&gt;</span>Hi!<span>&lt;/<span>Button</span>&gt;</span>\n    <span>&lt;<span>Modal</span> <span>v-model</span>=<span>\"visible\"</span>&gt;</span>\n      <span>&lt;<span>div</span>&gt;</span>Hello HeyUI!<span>&lt;/<span>div</span>&gt;</span>\n    <span>&lt;/<span>Modal</span>&gt;</span>\n  <span>&lt;/<span>div</span>&gt;</span>\n<span>&lt;/<span>template</span>&gt;</span>\n<span>&lt;<span>script</span>&gt;</span><span>\n<span>export</span> <span>default</span> {\n  data() {\n    <span>return</span> {\n      <span>visible</span>: <span>false</span>\n    };\n  }\n};\n</span><span>&lt;/<span>script</span>&gt;</span>\n</code></pre> <div>展开代码</div></div> <h3>支持环境</h3> <p>现代浏览器和 IE9 及以上。</p> <h3>兼容</h3> <p>HeyUI支持 Vue.js 2.x版本</p> <h3>相关链接</h3> <ul><li><a href=\"https://vuejs.org/\">Vue官方文档</a></li><li><a href=\"https://github.com/heyui/hey-cli\">Hey-Cli脚手架</a></li><li><a href=\"http://admin.heyui.top\">官方demo</a></li></ul>','关于HeyUI HeyUI 是一套基于 Vue2.0 的开源 UI 组件库，主要服务于一些中后台产品。 特性 HeyUI提供的是一整套解决方案，所有的组件提供全局的可配置模式。 真正的数据驱动全局的配...','修改第一个文章',_binary '\0','2019-09-06 09:58:20.000000','2019-09-06 09:58:20.000000'),('e7ee6608-924d-432e-be13-3d5cd142262d','<p>文章5</p><p>文章4</p><p>文章4</p><p>文章4</p><p>文章5</p>','文章5文章4文章4文章4文章5','文章5',_binary '\0','2019-09-06 10:55:30.077000','2019-09-06 10:55:30.077000');
/*!40000 ALTER TABLE `content_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_article_tags`
--

DROP TABLE IF EXISTS `content_article_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `content_article_tags` (
  `article_id` varchar(36) NOT NULL,
  `tags_id` varchar(36) NOT NULL,
  KEY `FKcmxasyynkhq670hbuv0wbu2jo` (`tags_id`),
  KEY `FKa8g85o94x66o43s8k3mh3euso` (`article_id`),
  CONSTRAINT `FKa8g85o94x66o43s8k3mh3euso` FOREIGN KEY (`article_id`) REFERENCES `content_article` (`id`),
  CONSTRAINT `FKcmxasyynkhq670hbuv0wbu2jo` FOREIGN KEY (`tags_id`) REFERENCES `content_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_article_tags`
--

LOCK TABLES `content_article_tags` WRITE;
/*!40000 ALTER TABLE `content_article_tags` DISABLE KEYS */;
INSERT INTO `content_article_tags` VALUES ('63ab0b4f-422a-42d6-a2ed-485d5c3788c2','d298a3df-07d1-45ac-8e0d-cb7eb1bd19fd'),('dd5ce6c7-5eb3-47a6-949e-77e48366d0e5','4b5bf0aa-4fdb-441c-a43d-49b4ca0f8c97'),('dd5ce6c7-5eb3-47a6-949e-77e48366d0e5','c3e30b2c-df5f-4376-bf31-7a589ac2c71a'),('dd5ce6c7-5eb3-47a6-949e-77e48366d0e5','40a62a56-e613-4d91-ac97-97e88e818e74');
/*!40000 ALTER TABLE `content_article_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content_tag`
--

DROP TABLE IF EXISTS `content_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `content_tag` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content_tag`
--

LOCK TABLES `content_tag` WRITE;
/*!40000 ALTER TABLE `content_tag` DISABLE KEYS */;
INSERT INTO `content_tag` VALUES ('1','测试1'),('2','测试2'),('40a62a56-e613-4d91-ac97-97e88e818e74','标签50'),('4b5bf0aa-4fdb-441c-a43d-49b4ca0f8c97','标签30'),('595d519b-66d1-4b58-a604-32bf08a47339','标签10'),('8ab05830-74dc-402d-ab9c-86b72f259297','标签1'),('bb924d26-0f02-48c4-bbea-70e59b71a5a5','标签2'),('c3e30b2c-df5f-4376-bf31-7a589ac2c71a','标签40'),('d298a3df-07d1-45ac-8e0d-cb7eb1bd19fd','新标签'),('d3aab8cc-5e92-4b36-9a6b-40e22031e879','标签3'),('e08011a4-af5a-44a5-9cdb-66d69d33b832','标签20'),('ef2e26ef-d86b-481c-a20a-e7143feba97f','标签20');
/*!40000 ALTER TABLE `content_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_item`
--

DROP TABLE IF EXISTS `file_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `file_item` (
  `id` varchar(36) NOT NULL,
  `content_type` varchar(255) DEFAULT NULL,
  `length` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `upload_time` datetime(6) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXnvn2p2f6uvaij55kp35pbsl2n` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_item`
--

LOCK TABLES `file_item` WRITE;
/*!40000 ALTER TABLE `file_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` varchar(36) NOT NULL,
  `built_in` bit(1) NOT NULL,
  `fixed` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role_key` varchar(255) DEFAULT NULL,
  `tenant_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('12e01580-aa91-440b-a516-eec8a52ee933',_binary '',_binary '','普通用户','USER',NULL),('4859f260-bced-46b0-bac6-9aab0b726c61',_binary '',_binary '\0','超级管理员','ADMIN',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tag` (
  `tag_id` varchar(36) NOT NULL,
  `account` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `temporary` bit(1) NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_user_infos`
--

DROP TABLE IF EXISTS `tag_user_infos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tag_user_infos` (
  `tag_tag_id` varchar(36) NOT NULL,
  `user_infos_id` varchar(36) NOT NULL,
  KEY `FKsbc2q6cudtsuxflh4s25bp1vs` (`user_infos_id`),
  KEY `FKikyipjx57mjbjx3125dku7u9y` (`tag_tag_id`),
  CONSTRAINT `FKikyipjx57mjbjx3125dku7u9y` FOREIGN KEY (`tag_tag_id`) REFERENCES `tag` (`tag_id`),
  CONSTRAINT `FKsbc2q6cudtsuxflh4s25bp1vs` FOREIGN KEY (`user_infos_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_user_infos`
--

LOCK TABLES `tag_user_infos` WRITE;
/*!40000 ALTER TABLE `tag_user_infos` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_user_infos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL,
  `account_expire_time` datetime(6) DEFAULT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_expire_time` datetime(6) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `registered_time` datetime(6) DEFAULT NULL,
  `tenant_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('0dc93490-0fab-49c7-9672-98cab04a4ae4','2020-03-04 16:11:36.198000',_binary '',_binary '','oVfatuEmUSWtzcAjeOlmQ-eFKMmU','富强民主文明和谐','oVfatuEmUSWtzcAjeOlmQ-eFKMmU','{bcrypt}$2a$10$TZpRpbtOKJOq9fDzZhArWeJMuI2Nmz/2X5iRTwwQK7YiqtsfSmE4q','2019-12-05 16:11:36.198000',NULL,'2019-09-06 16:11:36.195000',NULL),('17c96a05-56ce-4d5c-90e8-77f288c58939','2020-03-04 16:10:06.618000',_binary '',_binary '','oVfatuIOQWi6M8wu6WjmsZKP4WSw','2333','oVfatuIOQWi6M8wu6WjmsZKP4WSw','{bcrypt}$2a$10$lgKA6/9cqIBuma0LIWAHfuQbX.4GiFVi2cAO6UCudywi7o0/MsjJ2','2019-12-05 16:10:06.618000',NULL,'2019-09-06 16:10:06.615000',NULL),('1bfe9b3c-2341-4612-9473-45dd1bac914a','2020-03-04 16:09:53.984000',_binary '',_binary '','oVfatuAd7XlM2qzgXIjwj8zPPxPo','开心曾','oVfatuAd7XlM2qzgXIjwj8zPPxPo','{bcrypt}$2a$10$Mn4zQtTLoNBtUQJdJh76JuOOHPkfXEQYaEgnas6uvKiktY0TgzDqO','2019-12-05 16:09:53.984000',NULL,'2019-09-06 16:09:53.981000',NULL),('2b5ac2ce-cf40-4a22-b013-f8bae3a1dbaf','2020-03-04 16:12:00.620000',_binary '',_binary '','oVfatuGO67X-Ql0svO_J0CJOgSVc','末世岛屿','oVfatuGO67X-Ql0svO_J0CJOgSVc','{bcrypt}$2a$10$CdrCQRW7QBpdkRX5141duuk1ZmoXPpIhq6q7Pl3PPUKGmNxvdivBC','2019-12-05 16:12:00.620000',NULL,'2019-09-06 16:12:00.617000',NULL),('4099130e-9fa6-4085-8f14-ed85eeb20155','2020-03-04 16:11:12.556000',_binary '',_binary '','oVfatuJETYEPpXiaYZ1Ap_BZ8p-0','上司','oVfatuJETYEPpXiaYZ1Ap_BZ8p-0','{bcrypt}$2a$10$w1uGu5lqvF99SMEEpqtvauT0sVv13SW8OX.u207Bb3jQ/NwOInwH2','2019-12-05 16:11:12.556000',NULL,'2019-09-06 16:11:12.553000',NULL),('43d7c0ed-0a2e-428e-96d9-1dffb671ede9','2020-03-04 16:10:19.568000',_binary '',_binary '','oVfatuFxIck6IuqsABIfZPA38me0','一生梦一场','oVfatuFxIck6IuqsABIfZPA38me0','{bcrypt}$2a$10$Qf8wdIPohzaOvjlgrIs2EuYqbu.dlsGl/Sn.wCDcqDjnGxex/VFJu','2019-12-05 16:10:19.568000',NULL,'2019-09-06 16:10:19.564000',NULL),('4fcf660c-f1a5-459c-be9c-54be5e0d2676','2020-03-04 16:12:08.913000',_binary '',_binary '','oVfatuAUySmhL0WRDy7kETG8_a30','￡佐掱の恛憶','oVfatuAUySmhL0WRDy7kETG8_a30','{bcrypt}$2a$10$UdcOKjh/xmKzOCb5Ex1Zu.25OO23ZMBe6isvfw6ud5q/P034ik02K','2019-12-05 16:12:08.913000',NULL,'2019-09-06 16:12:08.910000',NULL),('53db1635-b10a-43f2-8aa5-12a229720023','2020-03-04 16:09:59.713000',_binary '',_binary '','oVfatuDwDidW7jAxkUWexQ09SgCE','倾听，红颜情','oVfatuDwDidW7jAxkUWexQ09SgCE','{bcrypt}$2a$10$YoUX3jORG.e2MZqulav/te1yRCZ0cd3LIvnXxwheJDHzZoFvCN5wO','2019-12-05 16:09:59.713000',NULL,'2019-09-06 16:09:59.709000',NULL),('5f18730d-7beb-4d8b-b9eb-34bf85ed4a97','2020-03-04 16:10:32.081000',_binary '',_binary '','oVfatuJfkM1GmZyjlfo6aWnFeodc','TKeo','oVfatuJfkM1GmZyjlfo6aWnFeodc','{bcrypt}$2a$10$mjVLAwLYDYqJnpN9fcijbe/21TldwlTnhWjKd/7xK7E2Lc3H69UaW','2019-12-05 16:10:32.081000',NULL,'2019-09-06 16:10:32.078000',NULL),('678bf851-786c-40e4-9f82-e7bb40e9002d','2020-02-29 14:01:24.704000',_binary '',_binary '','13800138000','疯狂软件','oVfatuAD7ifHGACn5ClJ-9bkFRbM','{bcrypt}$2a$10$t7bhcHuMss4D8DguTAMb7.WGywBn5eVV545EMgTrurkhSYl4atQli','2019-12-01 14:01:24.704000','13800138000','2019-09-02 14:01:24.694000',NULL),('7dcec901-bc58-4053-a00c-f8c6912a7ed8','2020-03-04 16:12:14.667000',_binary '',_binary '','oVfatuLOZCXYrRt8zkmtUOfMoB3w','仅此丶而已','oVfatuLOZCXYrRt8zkmtUOfMoB3w','{bcrypt}$2a$10$Y10suyJpEjvXFkXdI6Vht.soXYbXQbj6ktj9SADMZ3m0PfB0Tjffy','2019-12-05 16:12:14.667000',NULL,'2019-09-06 16:12:14.664000',NULL),('8e04b21f-67cd-42ca-aaf2-b5ffa65fa6fc','2020-03-04 16:10:58.124000',_binary '',_binary '','oVfatuDNSMhxMp63G65DbgQYwEFQ','杉','oVfatuDNSMhxMp63G65DbgQYwEFQ','{bcrypt}$2a$10$bpl682VTDnTJcO3/x2avwuRi3v53V9ExQht8fvkrLRjslGSyGAYzW','2019-12-05 16:10:58.124000',NULL,'2019-09-06 16:10:58.121000',NULL),('96695224-a67e-4e57-9ec4-b825c738b454','2020-03-04 16:11:04.713000',_binary '',_binary '','oVfatuC00FUish5b6bJJaGnLv0Co','L`YingYing','oVfatuC00FUish5b6bJJaGnLv0Co','{bcrypt}$2a$10$VuMObfvcrsuiSOnVx54TJubD.48EU.xBY9DCfBmM33H7X3dxblowK','2019-12-05 16:11:04.713000',NULL,'2019-09-06 16:11:04.709000',NULL),('9a70bd58-79bb-4e2b-9a1e-fdf89ffb00d7','2020-03-04 16:10:11.747000',_binary '',_binary '','oVfatuFpuxz1ttdPmh8mF0UalFSs',':(','oVfatuFpuxz1ttdPmh8mF0UalFSs','{bcrypt}$2a$10$H53qHBiOY.LUnKWV3jsacO3S.lvVn0rtkeEKPjn0jsnRmy987MaZq','2019-12-05 16:10:11.747000',NULL,'2019-09-06 16:10:11.743000',NULL),('c8a8b4c7-8946-4686-ae29-c1391cba3908','2020-03-04 16:12:27.482000',_binary '',_binary '','oVfatuABKgWSKjIqsuUGlHmt5WuI','旭佳','oVfatuABKgWSKjIqsuUGlHmt5WuI','{bcrypt}$2a$10$cLOoxJTTG47UtdXb47Fn1OMb2KG6Eu6Y4qwyRH5ymJzxvwlw7CafS','2019-12-05 16:12:27.482000',NULL,'2019-09-06 16:12:27.479000',NULL),('d31deceb-e38e-49de-8ba7-c3b0c90c3c87','2020-03-04 16:09:46.686000',_binary '',_binary '','oVfatuP8UZ6fP7lJEelbLH60MLWo','Charles','oVfatuP8UZ6fP7lJEelbLH60MLWo','{bcrypt}$2a$10$fg8u0Lfz3A3jqDjOFIwjIuZlFtwDsDmXySf.udZhqL/WobEOO1Zf6','2019-12-05 16:09:46.686000',NULL,'2019-09-06 16:09:46.682000',NULL),('d6d0a709-0945-4b98-acb4-ad323a2d54a3','2020-03-04 16:10:26.181000',_binary '',_binary '','oVfatuM91EsSu1xefaWa0xyNbyYI','seven','oVfatuM91EsSu1xefaWa0xyNbyYI','{bcrypt}$2a$10$d/JOdmveMku738ZgN3a96.scfM5h6PgtfyNaecSdGUUNyKn5gk8yi','2019-12-05 16:10:26.181000',NULL,'2019-09-06 16:10:26.178000',NULL),('db3d1599-29ad-4281-b78f-f7be8beaa0d1','2020-03-04 16:11:52.149000',_binary '',_binary '','oVfatuOmVZoBtnHrmcSgOj2WujNs','赠','oVfatuOmVZoBtnHrmcSgOj2WujNs','{bcrypt}$2a$10$2rlN3Kzz5fkE9JgJdsM/rur1ZxuyqaDcvLr4oSLc8xThhFNSL4qv2','2019-12-05 16:11:52.149000',NULL,'2019-09-06 16:11:52.146000',NULL),('dd9c0470-ec17-4db0-910f-e88841164a63','2020-03-04 16:12:21.272000',_binary '',_binary '','oVfatuI26xyPbT8tWREq1YBUq86Y','肥仔琳','oVfatuI26xyPbT8tWREq1YBUq86Y','{bcrypt}$2a$10$Rj33hgQg3e7rZdCSUYBvOu7yvmKvpwtKXd7xiH4He5R8ReneOZZJm','2019-12-05 16:12:21.272000',NULL,'2019-09-06 16:12:21.269000',NULL),('de001a1e-306f-4161-8e88-bb79b2928c65','2020-03-04 16:11:46.390000',_binary '',_binary '','oVfatuBUz3_cQEIjAjLTGOcmrFzc','不到130斤绝不改名','oVfatuBUz3_cQEIjAjLTGOcmrFzc','{bcrypt}$2a$10$2Dh2GdIj8whtoKCAgEK7Huq3vJ3UNL5MxQTtVVhYyWY6b39UzY4vq','2019-12-05 16:11:46.390000',NULL,'2019-09-06 16:11:46.387000',NULL),('e3258dea-4cf2-40a6-b1d3-307bcf09f9f3','2020-03-04 16:10:49.926000',_binary '',_binary '','oVfatuAcWPCsMOr-jl3cDXxGNSzY','More','oVfatuAcWPCsMOr-jl3cDXxGNSzY','{bcrypt}$2a$10$bVo6Am4c6syTYphmzVtteuyvPMKLbd9j2R9clTH259/srr0m.9kdK','2019-12-05 16:10:49.926000',NULL,'2019-09-06 16:10:49.923000',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_info` (
  `id` varchar(36) NOT NULL,
  `account` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `enable` bit(1) NOT NULL,
  `group_id` int(11) NOT NULL,
  `head_image_url` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `qr_scene` varchar(255) DEFAULT NULL,
  `qr_scene_value` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sex` tinyint(4) NOT NULL,
  `subscribe` tinyint(4) NOT NULL,
  `subscribe_date` datetime(6) DEFAULT NULL,
  `subscribe_scene` varchar(255) DEFAULT NULL,
  `subscribe_time` bigint(20) NOT NULL,
  `union_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES ('00629b83-e27a-4780-bc52-26d82ec4881f','gh_f21f1485101e','广州','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/qicBmYTfTYguqlibrV6eNwgVpG644pN7atssGRYatpM8XLKVNCPCErib4WAozayp2D7UianFa0ZK6ZT78nQOszh5diarzwZSSE6eM/132','zh_CN','仅此丶而已','oVfatuLOZCXYrRt8zkmtUOfMoB3w','广东','0','','',1,1,'2018-05-29 10:44:12.000000','ADD_SCENE_QR_CODE',1527561852,NULL,'7dcec901-bc58-4053-a00c-f8c6912a7ed8'),('010d9f86-ca27-448b-9218-50e5d2f37a95','gh_f21f1485101e','','',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/laQWKcFEnwLw0D2UynB2fcqYIpdzTLUeNpruTG8OPFzUY7kIoyJWeYZCLAFnxbRI8AibL1leXUialXNHcCVACJjiaoO3HUkyzEP/132','zh_CN','杉','oVfatuDNSMhxMp63G65DbgQYwEFQ','','0','','',0,1,'2019-05-17 14:23:47.000000','ADD_SCENE_QR_CODE',1558074227,NULL,'8e04b21f-67cd-42ca-aaf2-b5ffa65fa6fc'),('0531c9e1-9858-4653-9bc8-f91ff1ef00d7','gh_f21f1485101e','','俄罗斯',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/ajNVdqHZLLDbguia3xKSht3aJxIydTjpVWiaLTPv6jbdbGLe723iaHKQ98erv877d5TQxqRKBR89QfV8saRTmoCXEjBu5COTHU0L3rd8Nqo2XE/132','zh_CN','Charles','oVfatuP8UZ6fP7lJEelbLH60MLWo','伏尔加格勒','0','','',1,1,'2019-08-19 15:58:50.000000','ADD_SCENE_QR_CODE',1566201530,NULL,'d31deceb-e38e-49de-8ba7-c3b0c90c3c87'),('123b3052-6f8b-4f87-ad73-8660d5dc1022','gh_f21f1485101e','','刚果民主共和国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/UGrobmT8GcIkJ5P7Pgnga2IhYNPdrayNePG9KliblRDiayxFY3rtxXicgNFTjvNVcibmRBiaV5WwluWVXF8ApRYezjIvJ4caMBb4z/132','zh_CN','TKeo','oVfatuJfkM1GmZyjlfo6aWnFeodc','','0','','',1,1,'2018-05-23 15:52:29.000000','ADD_SCENE_QR_CODE',1527061949,NULL,'5f18730d-7beb-4d8b-b9eb-34bf85ed4a97'),('1ab04a2a-4923-4c6f-90a6-f694e9c94362','gh_f21f1485101e','广州','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN8FEHgWFtsH6kP1mNIFvSDUmDWuI38z99wgXZHNoz8GElPo9Wb4IBq16K6QvTfVLZ4y3e7KqicYgBle4tAMEKGUX/132','zh_CN',':(','oVfatuFpuxz1ttdPmh8mF0UalFSs','广东','0','','',1,1,'2019-08-24 20:17:48.000000','ADD_SCENE_QR_CODE',1566649068,NULL,'9a70bd58-79bb-4e2b-9a1e-fdf89ffb00d7'),('1d9f3c91-ca33-45a5-80b5-e25979521692','gh_f21f1485101e','汕头','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/UGrobmT8GcKflwoENS8hSn52bMU9PUqVO9bstU8MQ6XS84wicrTPeApz1LsRfNiaEWCf8GbFxnfBpDOZsvJ8I4FE7lujFyL4Sy/132','zh_CN','seven','oVfatuM91EsSu1xefaWa0xyNbyYI','广东','0','','',1,1,'2018-05-29 15:00:52.000000','ADD_SCENE_QR_CODE',1527577252,NULL,'d6d0a709-0945-4b98-acb4-ad323a2d54a3'),('21f1d928-f605-4220-a564-763d103d8dd8','gh_f21f1485101e','揭阳','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN9Lm4p2jvOlq1EXUSuEia4zkCg5V6RmQOXvTq2ffzTRIHl0GIbPZTuq1vShIBNTeIZgUOUwvGBic9pricwU4TajCZt/132','zh_CN','不到130斤绝不改名','oVfatuBUz3_cQEIjAjLTGOcmrFzc','广东','0','','',1,1,'2019-05-17 14:23:50.000000','ADD_SCENE_QR_CODE',1558074230,NULL,'de001a1e-306f-4161-8e88-bb79b2928c65'),('3105b720-fd12-45b6-ab53-6e1f79a2e974','gh_f21f1485101e','清远','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/qicBmYTfTYguViaET3rGk6jxMTWVaNgbejW61IF1PjSsQ40BwDEhhZ9Apn7xV8xJHT7wCHxn7lic5nq5WVLP376ia0t0IicXZ2t0f/132','zh_CN','L`YingYing','oVfatuC00FUish5b6bJJaGnLv0Co','广东','0','','',2,1,'2018-05-23 16:05:08.000000','ADD_SCENE_QR_CODE',1527062708,NULL,'96695224-a67e-4e57-9ec4-b825c738b454'),('409f1f2b-0d93-4866-844b-47137d318dfb','gh_f21f1485101e','广州','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/FxicvgOPjtrfHPDOhdw3ux1MQiaNXJwCG2Mibw8uqBIE12Wermh0Cjhuyk9DvCeDEECRbdw8aSwjIFZnJYu7ubxxCrRsGEaJxzZ/132','zh_CN','一生梦一场','oVfatuFxIck6IuqsABIfZPA38me0','广东','0','','',1,1,'2018-04-18 10:43:45.000000','ADD_SCENE_QR_CODE',1524019425,NULL,'43d7c0ed-0a2e-428e-96d9-1dffb671ede9'),('4293326b-c206-40b1-964b-c686eea18590','gh_f21f1485101e','广州','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/4ZmcR2uDic8yYFuFEKNlRP3e5MTrGOAxEzXP9OpcBT9mDvErxKnZr2Z3EUWnbdF8gXkx3EAyh0mFVTE0YeGWn5CEczrcNAtyL/132','zh_CN','富强民主文明和谐','oVfatuEmUSWtzcAjeOlmQ-eFKMmU','广东','0','','',1,1,'2018-05-29 15:01:01.000000','ADD_SCENE_QR_CODE',1527577261,NULL,'0dc93490-0fab-49c7-9672-98cab04a4ae4'),('48c3255a-7e6e-4740-9de6-4b5a0d0fa5f9','gh_f21f1485101e','广州','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/32Qibt73EATptYGYvnbYQOX4hfxnTmcX83iaXXwYB03w3cuX7nfYQhDOIDmgmmibqQLcbhQpITicPdVl0GKS77s45P2iavsUEOwy4/132','zh_CN','开心曾','oVfatuAd7XlM2qzgXIjwj8zPPxPo','广东','0','','',1,1,'2019-08-23 18:44:24.000000','ADD_SCENE_QR_CODE',1566557064,NULL,'1bfe9b3c-2341-4612-9473-45dd1bac914a'),('4901a5b0-8caf-47a0-9d29-481983074ee0','gh_f21f1485101e','广州','中国',_binary '',103,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN9APr4clu4j00USgtLh3vHX4c1pPfVElhgPpVagx6nWPLYXMticQicXUM8NuiaDhHSgWhg3Itv7ma9XHgRibtYtcMDa/132','zh_CN','罗文强-大数据和少儿编程培训','oVfatuAD7ifHGACn5ClJ-9bkFRbM','广东','0','','',1,1,'2019-08-26 11:50:41.000000','ADD_SCENE_QR_CODE',1566791441,NULL,'678bf851-786c-40e4-9f82-e7bb40e9002d'),('635b6858-8943-401e-8536-703d840dde18','gh_f21f1485101e','阳江','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN8ibu1qyNzVlFsSRicghoeteibpJRhGFcI2DV687txnicdrRz78kJia15UCQCNfRTXptb2NlbPicBLQF7KhvxQiaxQD5to/132','zh_CN','肥仔琳','oVfatuI26xyPbT8tWREq1YBUq86Y','广东','0','','',2,1,'2018-05-29 15:00:57.000000','ADD_SCENE_QR_CODE',1527577257,NULL,'dd9c0470-ec17-4db0-910f-e88841164a63'),('66d602ff-29b1-4ad1-9341-7cc290f50714','gh_f21f1485101e','','希腊',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/4ZmcR2uDic8zwh9y8V8Kr9djicrwRiaV51icBib1QibcdA0SzH1ia5jZLUHW2UCDwsKUujiblT5WZVIC1jELLAJKNje8dJWXakG33RHU/132','zh_CN','2333','oVfatuIOQWi6M8wu6WjmsZKP4WSw','','0','','',1,1,'2019-05-17 14:23:46.000000','ADD_SCENE_QR_CODE',1558074226,NULL,'17c96a05-56ce-4d5c-90e8-77f288c58939'),('7ed16e79-d55e-415b-b3e9-962b219ad4ae','gh_f21f1485101e','肇庆','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/maSTmtaArPgIx4VUC1ibrMf6OjA0YKSNNOTo5Lz3HZsGUtlMJpfyt5AD406cYGct49TtjJzkJJ40ISVupwkzaicscatiaGLJDial/132','zh_CN','倾听，红颜情','oVfatuDwDidW7jAxkUWexQ09SgCE','广东','0','','',1,1,'2019-05-17 14:23:59.000000','ADD_SCENE_QR_CODE',1558074239,NULL,'53db1635-b10a-43f2-8aa5-12a229720023'),('91f2ff48-4a67-451a-850f-1725d186dbd1','gh_f21f1485101e','汕头','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SN8FEHgWFtsH6mTRnLialeAhyQflLgObM6BLzoqYu7eAQP67ictsmMrByJltPC8QdWZAyJibVLJia6KmpKCnNRYvicAeic/132','zh_CN','旭佳','oVfatuABKgWSKjIqsuUGlHmt5WuI','广东','0','','',1,1,'2018-05-23 16:04:34.000000','ADD_SCENE_QR_CODE',1527062674,NULL,'c8a8b4c7-8946-4686-ae29-c1391cba3908'),('ccf4e5d7-68ff-46be-b82d-4d214d40611b','gh_f21f1485101e','','挪威',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/FxicvgOPjtrfmPRzDpc5RR0IBsYHaVwgKT7pK9gQFIlvNoHTn4qkibTPzqsvaNstk9ibutr4yFFdAw0IBpFzfoPmUABa2JR6wbB/132','zh_CN','￡佐掱の恛憶','oVfatuAUySmhL0WRDy7kETG8_a30','','0','','',1,1,'2018-03-28 10:02:17.000000','ADD_SCENE_QR_CODE',1522202537,NULL,'4fcf660c-f1a5-459c-be9c-54be5e0d2676'),('d996ccb7-61fc-4585-bf53-c9a4e32082ce','gh_f21f1485101e','深圳','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/Q3auHgzwzM63MaKy2hAsYFOibgq7fVRXEY2yt83iaRRVYWZkdKiatlWKqYyotsM7cs2qYPfsaFpOdibcaZrzkUiaZbhic2FEQAY5K481UpZ2WY1Eg/132','zh_CN','More','oVfatuAcWPCsMOr-jl3cDXxGNSzY','广东','0','','',1,1,'2018-07-03 11:20:06.000000','ADD_SCENE_SEARCH',1530588006,NULL,'e3258dea-4cf2-40a6-b1d3-307bcf09f9f3'),('dfb8c8a9-c697-405f-a0fc-9e15d02d82b2','gh_f21f1485101e','','芬兰',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/FxicvgOPjtrfIQ8r6cL1WSpoMdZsDIu0k0o9az87xicecOGx0aZgzvlmUUq3zMGArjXd8EficeLRtHuT7zDyak3tOsUxZLKaOfN/132','zh_CN','上司','oVfatuJETYEPpXiaYZ1Ap_BZ8p-0','','0','','',1,1,'2018-05-29 15:00:43.000000','ADD_SCENE_QR_CODE',1527577243,NULL,'4099130e-9fa6-4085-8f14-ed85eeb20155'),('f12dd910-7b4d-445a-8079-390474889a06','gh_f21f1485101e','','',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/qicBmYTfTYgvVTuibbibVsVuZqYYUJia5iaA1KI8RCqlibuiclT5FE7UOldatgl8uPbdQpWky0grN45iaHPDXtx3rzWicKgEIGfnIR9yj/132','zh_CN','末世岛屿','oVfatuGO67X-Ql0svO_J0CJOgSVc','','0','','',2,1,'2019-05-17 14:27:13.000000','ADD_SCENE_PROFILE_CARD',1558074433,NULL,'2b5ac2ce-cf40-4a22-b013-f8bae3a1dbaf'),('f6f456b7-48af-48bd-b9d8-d8974f6fb0b8','gh_f21f1485101e','汕头','中国',_binary '',0,'http://thirdwx.qlogo.cn/mmopen/MicSgeq41SNicVnJeHf49h6F44tTiaKkTCgiayCb1VcqCob0E9pzxWpHSUsSFLtQsANVU7Aa43PHQFjNPcI87EIXVefu94XgB0bj/132','zh_CN','赠','oVfatuOmVZoBtnHrmcSgOj2WujNs','广东','0','','',1,1,'2018-04-24 11:04:22.000000','ADD_SCENE_QR_CODE',1524539062,NULL,'db3d1599-29ad-4281-b78f-f7be8beaa0d1');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info_tag_id_list`
--

DROP TABLE IF EXISTS `user_info_tag_id_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_info_tag_id_list` (
  `user_info_id` varchar(36) NOT NULL,
  `tag_id_list` int(11) DEFAULT NULL,
  KEY `FKc6e00om4n7emgp3bnnu3micwi` (`user_info_id`),
  CONSTRAINT `FKc6e00om4n7emgp3bnnu3micwi` FOREIGN KEY (`user_info_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info_tag_id_list`
--

LOCK TABLES `user_info_tag_id_list` WRITE;
/*!40000 ALTER TABLE `user_info_tag_id_list` DISABLE KEYS */;
INSERT INTO `user_info_tag_id_list` VALUES ('4901a5b0-8caf-47a0-9d29-481983074ee0',103);
/*!40000 ALTER TABLE `user_info_tag_id_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_roles` (
  `user_id` varchar(36) NOT NULL,
  `roles_id` varchar(36) NOT NULL,
  KEY `FKj9553ass9uctjrmh0gkqsmv0d` (`roles_id`),
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKj9553ass9uctjrmh0gkqsmv0d` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('678bf851-786c-40e4-9f82-e7bb40e9002d','12e01580-aa91-440b-a516-eec8a52ee933');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-08 11:57:27
