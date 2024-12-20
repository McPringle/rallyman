CREATE TABLE `location` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `latitude` DECIMAL(9,6) NOT NULL,
    `longitude` DECIMAL(9,6) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `location_name` ON `location` (`name`);

CREATE TABLE `event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` MEDIUMTEXT NOT NULL,
    `date` DATETIME NOT NULL,
    `location` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `event_name` ON `event` (`name`);
CREATE INDEX `event_date` ON `event` (`date`);
