-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
-- 创建一个表，指定了4个属性：id、年龄、身高、体重。最后指定了id是唯一不能重复的键值
CREATE TABLE IF NOT EXISTS `users` (`id` integer NOT NULL, `name` varchar(20),  `age` integer, PRIMARY  KEY(`id`));

