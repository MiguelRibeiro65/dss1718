-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bdHorarios
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bdHorarios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bdHorarios` DEFAULT CHARACTER SET utf8 ;
USE `bdHorarios` ;

-- -----------------------------------------------------
-- Table `bdHorarios`.`Aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Aluno` (
  `numero` VARCHAR(12) NOT NULL,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `estatuto` TINYINT(4) NULL DEFAULT NULL,
  PRIMARY KEY (`numero`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdHorarios`.`UC`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`UC` (
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
-- Table `bdHorarios`.`Docente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Docente` (
  `id` INT(11) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdHorarios`.`Turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Turno` (
  `idTurno` INT(11) NOT NULL,
  `tipo` INT(11) NULL DEFAULT NULL,
  `dia` VARCHAR(45) NULL,
  `inicio` VARCHAR(45) NULL,
  `fim` VARCHAR(45) NULL,
  `capacidade` VARCHAR(45) NULL,
  `UC_idUC` VARCHAR(12) NOT NULL,
  `Docente_id` INT(11) NOT NULL,
  PRIMARY KEY (`idTurno`),
  INDEX `UC_idUC_idx` (`UC_idUC` ASC),
  INDEX `fk_Turno_Docente1_idx` (`Docente_id` ASC),
  CONSTRAINT `UC_idUC`
    FOREIGN KEY (`UC_idUC`)
    REFERENCES `bdHorarios`.`UC` (`idUC`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Turno_Docente1`
    FOREIGN KEY (`Docente_id`)
    REFERENCES `bdHorarios`.`Docente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdHorarios`.`Aluno_has_Turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Aluno_has_Turno` (
  `Aluno_numero` VARCHAR(12) NOT NULL,
  `Turno_idTurno` INT(11) NOT NULL,
  PRIMARY KEY (`Aluno_numero`, `Turno_idTurno`),
  INDEX `fk_Aluno_has_Turno_Turno1_idx` (`Turno_idTurno` ASC),
  INDEX `fk_Aluno_has_Turno_Aluno1_idx` (`Aluno_numero` ASC),
  CONSTRAINT `fk_Aluno_has_Turno_Aluno1`
    FOREIGN KEY (`Aluno_numero`)
    REFERENCES `bdHorarios`.`Aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Aluno_has_Turno_Turno1`
    FOREIGN KEY (`Turno_idTurno`)
    REFERENCES `bdHorarios`.`Turno` (`idTurno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdHorarios`.`Aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Aula` (
  `id` INT NOT NULL,
  `data` VARCHAR(45) NULL,
  `Turno_idTurno` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Aula_Turno1_idx` (`Turno_idTurno` ASC),
  CONSTRAINT `fk_Aula_Turno1`
    FOREIGN KEY (`Turno_idTurno`)
    REFERENCES `bdHorarios`.`Turno` (`idTurno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bdHorarios`.`Aluno_has_Aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdHorarios`.`Aluno_has_Aula` (
  `Aluno_numero` VARCHAR(12) NOT NULL,
  `Aula_id` INT NOT NULL,
  PRIMARY KEY (`Aluno_numero`, `Aula_id`),
  INDEX `fk_Aluno_has_Aula_Aula1_idx` (`Aula_id` ASC),
  INDEX `fk_Aluno_has_Aula_Aluno1_idx` (`Aluno_numero` ASC),
  CONSTRAINT `fk_Aluno_has_Aula_Aluno1`
    FOREIGN KEY (`Aluno_numero`)
    REFERENCES `bdHorarios`.`Aluno` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Aluno_has_Aula_Aula1`
    FOREIGN KEY (`Aula_id`)
    REFERENCES `bdHorarios`.`Aula` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
