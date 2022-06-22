-- Ignora checagem de chaves estrangeiras
SET FOREIGN_KEY_CHECKS = 0;

-- --- --- --- --- --- --- --- --- --- ---
-- Tabelas
-- --- --- --- --- --- --- --- --- --- ---

DROP TABLE IF EXISTS `smartsale`.`user`;
CREATE TABLE IF NOT EXISTS `smartsale`.`user`
(
    `id`       INT         NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(60) NOT NULL,
    `username` VARCHAR(20) NOT NULL,
    `password` VARCHAR(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`supplier`;
CREATE TABLE IF NOT EXISTS `smartsale`.`supplier`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20)  NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`measure_type`;
CREATE TABLE IF NOT EXISTS `smartsale`.`measure_type`
(
    `id`              INT         NOT NULL AUTO_INCREMENT,
    `name`            VARCHAR(20) NOT NULL,
    `acronym_minimum` VARCHAR(2)  NOT NULL,
    `acronym_maximum` VARCHAR(2)  NULL,

    PRIMARY KEY (`id`),
    UNIQUE (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`product`;
CREATE TABLE IF NOT EXISTS `smartsale`.`product`
(
    `measure_type_id` INT           NOT NULL,

    `id`              INT           NOT NULL AUTO_INCREMENT,
    `name`            VARCHAR(60)   NOT NULL,
    `price`           DECIMAL(6, 2) NOT NULL DEFAULT 0,
    `stock`           INT           NOT NULL DEFAULT 0,
    `alert_ending`    INT           NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`),
    UNIQUE (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`product_has_supplier`;
CREATE TABLE IF NOT EXISTS `smartsale`.`product_has_supplier`
(
    `product_id`  INT NOT NULL,
    `supplier_id` INT NOT NULL,

    `id`          INT NOT NULL AUTO_INCREMENT,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`payment_method`;
CREATE TABLE IF NOT EXISTS `smartsale`.`payment_method`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`customer`;
CREATE TABLE IF NOT EXISTS `smartsale`.`customer`
(
    `id`      INT          NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(60)  NOT NULL,
    `phone`   VARCHAR(20)  NOT NULL,
    `zipcode` CHAR(9)      NOT NULL,
    `address` VARCHAR(255) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`sale`;
CREATE TABLE IF NOT EXISTS `smartsale`.`sale`
(
    `customer_id`       INT           NOT NULL,
    `payment_method_id` INT           NOT NULL,

    `id`                INT           NOT NULL AUTO_INCREMENT,
    `price`             DECIMAL(6, 2) NULL DEFAULT 0,
    `date`              TIMESTAMP     NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

DROP TABLE IF EXISTS `smartsale`.`sale_item`;
CREATE TABLE IF NOT EXISTS `smartsale`.`sale_item`
(
    `sale_id` INT           NOT NULL,

    `id`      INT           NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(60)   NOT NULL,
    `price`   DECIMAL(6, 2) NOT NULL,
    `amount`  INT           NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET utf8;

-- --- --- --- --- --- --- --- --- --- ---
-- Adiciona chaves estrangeiras
-- --- --- --- --- --- --- --- --- --- ---

ALTER TABLE `product`
    ADD FOREIGN KEY (`measure_type_id`) REFERENCES `measure_type` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE;

ALTER TABLE `product_has_supplier`
    ADD FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    ADD FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE;

ALTER TABLE `sale`
    ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    ADD FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE;

ALTER TABLE `sale_item`
    ADD FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`)
        ON UPDATE NO ACTION
        ON DELETE CASCADE;

-- --- --- --- --- --- --- --- --- --- ---
-- Insere dados
-- --- --- --- --- --- --- --- --- --- ---

INSERT INTO `user` (`name`, `username`, `password`)
VALUES ('Giovanni Oliveira', 'giovanni', 'giovanni');

INSERT INTO `measure_type` (`name`, `acronym_minimum`, `acronym_maximum`)
VALUES ('Grama', 'g', 'kg'),
       ('Mililitro', 'ml', 'l'),
       ('Unitário', 'u', NULL);
