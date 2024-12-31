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
    `description` LONGTEXT NOT NULL,
    `date` DATETIME NOT NULL,
    `location_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `event_name` ON `event` (`name`);
CREATE INDEX `event_date` ON `event` (`date`);

CREATE TABLE `mail_template` (
    `id` VARCHAR(255) NOT NULL,
    `subject` VARCHAR(255) NOT NULL,
    `content` LONGTEXT NOT NULL,
    PRIMARY KEY (`id`)
);

-- [jooq ignore start]
INSERT INTO mail_template (id, subject, content)
VALUES ('TEST','This is a test','The template variable "foobar" was set to "${foobar}".');
-- [jooq ignore stop]

CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `user_email` ON `user` (`email`);
