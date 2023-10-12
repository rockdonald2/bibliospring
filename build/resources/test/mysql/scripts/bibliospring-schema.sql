CREATE TABLE `BS_USER`
(
    `username` varchar(64) COLLATE 'utf8_bin' NOT NULL UNIQUE,
    `password` varchar(64) COLLATE 'utf8_bin' NOT NULL,
    `id`       int unsigned                   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `uuid`     varchar(36) COLLATE 'utf8_bin' NOT NULL UNIQUE
) ENGINE = 'InnoDB'
  COLLATE 'utf8_bin';