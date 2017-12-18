-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bdhorarios
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bdhorarios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bdhorarios` DEFAULT CHARACTER SET utf8 ;
USE `bdhorarios` ;

-- -----------------------------------------------------
-- Table `bdhorarios`.`aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aluno` (
  `numero` VARCHAR(12) NOT NULL,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `estatuto` TINYINT(4) NULL DEFAULT NULL,
  PRIMARY KEY (`numero`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`uc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`uc` (
  `idUC` VARCHAR(12) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `acron` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`idUC`),
  UNIQUE INDEX `idUC_UNIQUE` (`idUC` ASC),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  UNIQUE INDEX `acron_UNIQUE` (`acron` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`docente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`docente` (
  `id` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`turno` (
  `idTurno` INT(11) NOT NULL,
  `tipo` INT(11) NULL DEFAULT NULL,
  `dia` VARCHAR(45) NULL DEFAULT NULL,
  `inicio` VARCHAR(45) NULL DEFAULT NULL,
  `fim` VARCHAR(45) NULL DEFAULT NULL,
  `capacidade` VARCHAR(45) NULL DEFAULT NULL,
  `UC_idUC` VARCHAR(12) NOT NULL,
  `Docente_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idTurno`),
  INDEX `UC_idUC_idx` (`UC_idUC` ASC),
  INDEX `fk_Turno_Docente1_idx` (`Docente_id` ASC),
  CONSTRAINT `UC_idUC`
    FOREIGN KEY (`UC_idUC`)
    REFERENCES `bdhorarios`.`uc` (`idUC`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Turno_Docente1`
    FOREIGN KEY (`Docente_id`)
    REFERENCES `bdhorarios`.`docente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aula` (
  `id` INT(11) NOT NULL,
  `data` VARCHAR(45) NULL DEFAULT NULL,
  `Turno_idTurno` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Aula_Turno1_idx` (`Turno_idTurno` ASC),
  CONSTRAINT `fk_Aula_Turno1`
    FOREIGN KEY (`Turno_idTurno`)
    REFERENCES `bdhorarios`.`turno` (`idTurno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`aluno_has_aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aluno_has_aula` (
  `Aluno_numero` VARCHAR(12) NOT NULL,
  `Aula_id` INT(11) NOT NULL,
  PRIMARY KEY (`Aluno_numero`, `Aula_id`),
  INDEX `fk_Aluno_has_Aula_Aula1_idx` (`Aula_id` ASC),
  INDEX `fk_Aluno_has_Aula_Aluno1_idx` (`Aluno_numero` ASC),
  CONSTRAINT `fk_Aluno_has_Aula_Aluno1`
    FOREIGN KEY (`Aluno_numero`)
    REFERENCES `bdhorarios`.`aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Aluno_has_Aula_Aula1`
    FOREIGN KEY (`Aula_id`)
    REFERENCES `bdhorarios`.`aula` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`aluno_has_turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aluno_has_turno` (
  `Aluno_numero` VARCHAR(12) NOT NULL,
  `Turno_idTurno` INT(11) NOT NULL,
  PRIMARY KEY (`Aluno_numero`, `Turno_idTurno`),
  INDEX `fk_Aluno_has_Turno_Turno1_idx` (`Turno_idTurno` ASC),
  INDEX `fk_Aluno_has_Turno_Aluno1_idx` (`Aluno_numero` ASC),
  CONSTRAINT `fk_Aluno_has_Turno_Aluno1`
    FOREIGN KEY (`Aluno_numero`)
    REFERENCES `bdhorarios`.`aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Aluno_has_Turno_Turno1`
    FOREIGN KEY (`Turno_idTurno`)
    REFERENCES `bdhorarios`.`turno` (`idTurno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;