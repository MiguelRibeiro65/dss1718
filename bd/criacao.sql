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
  `nome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `estatuto` TINYINT(4) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`numero`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`docente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`docente` (
  `numero` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`numero`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`uc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`uc` (
  `acron` VARCHAR(15) NOT NULL,
  `nome` VARCHAR(70) NOT NULL,
  PRIMARY KEY (`acron`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  UNIQUE INDEX `acron_UNIQUE` (`acron` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`turno` (
  `idTurno` VARCHAR(45) NOT NULL,
  `dia` VARCHAR(45) NOT NULL,
  `inicio` VARCHAR(45) NOT NULL,
  `fim` VARCHAR(45) NOT NULL,
  `capacidade` INT(11) NOT NULL,
  `Docente_id` VARCHAR(45) NOT NULL,
  `uc_acron` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`idTurno`),
  INDEX `fk_Turno_Docente1_idx` (`Docente_id` ASC),
  INDEX `fk_turno_uc1_idx` (`uc_acron` ASC),
  CONSTRAINT `fk_Turno_Docente1`
    FOREIGN KEY (`Docente_id`)
    REFERENCES `bdhorarios`.`docente` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_turno_uc1`
    FOREIGN KEY (`uc_acron`)
    REFERENCES `bdhorarios`.`uc` (`acron`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aula` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `data` VARCHAR(45) NOT NULL,
  `Turno_idTurno` VARCHAR(45) NOT NULL,
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
  `aula_id` INT(11) NOT NULL,
  PRIMARY KEY (`Aluno_numero`, `aula_id`),
  INDEX `fk_Aluno_has_Aula_Aluno1_idx` (`Aluno_numero` ASC),
  INDEX `fk_aluno_has_aula_aula1_idx` (`aula_id` ASC),
  CONSTRAINT `fk_Aluno_has_Aula_Aluno1`
    FOREIGN KEY (`Aluno_numero`)
    REFERENCES `bdhorarios`.`aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_aluno_has_aula_aula1`
    FOREIGN KEY (`aula_id`)
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
  `Turno_idTurno` VARCHAR(45) NOT NULL,
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


-- -----------------------------------------------------
-- Table `bdhorarios`.`aluno_has_uc`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`aluno_has_uc` (
  `aluno_numero` VARCHAR(12) NOT NULL,
  `uc_acron` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`aluno_numero`, `uc_acron`),
  INDEX `fk_aluno_has_uc_uc1_idx` (`uc_acron` ASC),
  INDEX `fk_aluno_has_uc_aluno1_idx` (`aluno_numero` ASC),
  CONSTRAINT `fk_aluno_has_uc_aluno1`
    FOREIGN KEY (`aluno_numero`)
    REFERENCES `bdhorarios`.`aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_aluno_has_uc_uc1`
    FOREIGN KEY (`uc_acron`)
    REFERENCES `bdhorarios`.`uc` (`acron`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`direcaoCurso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`direcaoCurso` (
  `email` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdhorarios`.`troca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdhorarios`.`troca` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `aluno_numero` VARCHAR(12) NOT NULL,
  `turno_idTurno` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_aluno_has_turno1_turno1_idx` (`turno_idTurno` ASC),
  INDEX `fk_aluno_has_turno1_aluno1_idx` (`aluno_numero` ASC),
  CONSTRAINT `fk_aluno_has_turno1_aluno1`
    FOREIGN KEY (`aluno_numero`)
    REFERENCES `bdhorarios`.`aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_aluno_has_turno1_turno1`
    FOREIGN KEY (`turno_idTurno`)
    REFERENCES `bdhorarios`.`turno` (`idTurno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

