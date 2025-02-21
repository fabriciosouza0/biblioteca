SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `biblioteca`
--
CREATE DATABASE IF NOT EXISTS `biblioteca` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `biblioteca`;

-- --------------------------------------------------------

--
-- Estrutura da tabela `adms`
--

CREATE TABLE `adms`
(
    `codigo` int(11)     NOT NULL,
    `login`  varchar(15) NOT NULL,
    `senha`  varchar(40) NOT NULL,
    `nome`   varchar(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Extraindo dados da tabela `adms`
--

INSERT INTO `adms` (`codigo`, `login`, `senha`, `nome`)
VALUES (1, '000.000.000-00', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Estrutura da tabela `aluno`
--

CREATE TABLE `aluno`
(
    `CODIGO`       int(11) NOT NULL,
    `CODIGO_TURMA` int(11) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `autor`
--

CREATE TABLE `autor`
(
    `codigo` int(11) NOT NULL,
    `nome`   varchar(45) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `cdd`
--

CREATE TABLE `cdd`
(
    `CODIGO`    int(11)     NOT NULL,
    `DESCRICAO` varchar(45) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `livro`
--

CREATE TABLE `livro`
(
    `codigo`       bigint(13)  NOT NULL,
    `titulo`       varchar(45) NOT NULL,
    `codigo_autor` int(11) DEFAULT NULL,
    `codigo_cdd`   int(11) DEFAULT NULL,
    `qtd`          int(11) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `loca`
--

CREATE TABLE `loca`
(
    `codigo`            int(11)     NOT NULL,
    `codigo_livro`      bigint(13)  NOT NULL,
    `cpfLocatario`      varchar(14) NOT NULL,
    `dataDeLocacao`     date        NOT NULL,
    `dataParaDevolucao` date        NOT NULL,
    `atrasado`          varchar(1)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `locatario`
--

CREATE TABLE `locatario`
(
    `CPF`              varchar(14) NOT NULL,
    `NOME`             varchar(45) NOT NULL,
    `telefone`         varchar(14) DEFAULT NULL,
    `CODIGO_PROFESSOR` int(11)     DEFAULT NULL,
    `CODIGO_ALUNO`     int(11)     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `professor`
--

CREATE TABLE `professor`
(
    `CODIGO` int(11) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `turma`
--

CREATE TABLE `turma`
(
    `CODIGO`    int(11)     NOT NULL,
    `DESCRICAO` varchar(45) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Extraindo dados da tabela `turma`
--

INSERT INTO `turma` (`CODIGO`, `DESCRICAO`)
VALUES (1, '1º ADM'),
       (2, '1º AGRO'),
       (3, '1º FIN'),
       (4, '1º INF'),
       (5, '2º ADM'),
       (6, '2º AGRO'),
       (7, '2º DCC'),
       (8, '2º FIN'),
       (9, '2º INF'),
       (10, '3º ADM'),
       (11, '3º AGRO'),
       (12, '3º DCC'),
       (13, '3º FIN'),
       (14, '3º INF');

--
-- Primary Key `adms`
--
ALTER TABLE `adms`
    ADD PRIMARY KEY (`codigo`);

--
-- Primary Key `aluno`
--
ALTER TABLE `aluno`
    ADD PRIMARY KEY (`CODIGO`),
    ADD KEY `CODIGO_TURMA` (`CODIGO_TURMA`);

--
-- Primary Key `autor`
--
ALTER TABLE `autor`
    ADD PRIMARY KEY (`codigo`);

--
-- Primary Key `cdd`
--
ALTER TABLE `cdd`
    ADD PRIMARY KEY (`CODIGO`);

--
-- Primary Key `livro`
--
ALTER TABLE `livro`
    ADD PRIMARY KEY (`codigo`),
    ADD KEY `autor` (`codigo_autor`),
    ADD KEY `cdd` (`codigo_cdd`);

--
-- Primary Key `loca`
--
ALTER TABLE `loca`
    ADD PRIMARY KEY (`codigo`),
    ADD KEY `FK_cpfLocatario` (`cpfLocatario`),
    ADD KEY `FK_codigo_livro` (`codigo_livro`);

--
-- Primary Key `locatario`
--
ALTER TABLE `locatario`
    ADD PRIMARY KEY (`CPF`),
    ADD KEY `CODIGO_PROFESSOR` (`CODIGO_PROFESSOR`),
    ADD KEY `CODIGO_ALUNO` (`CODIGO_ALUNO`);

--
-- Primary Key `professor`
--
ALTER TABLE `professor`
    ADD PRIMARY KEY (`CODIGO`);

--
-- Primary Key `turma`
--
ALTER TABLE `turma`
    ADD PRIMARY KEY (`CODIGO`);

--
-- AUTO_INCREMENT `adms`
--
ALTER TABLE `adms`
    MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT `aluno`
--
ALTER TABLE `aluno`
    MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 15;

--
-- AUTO_INCREMENT `autor`
--
ALTER TABLE `autor`
    MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 8;

--
-- AUTO_INCREMENT `cdd`
--
ALTER TABLE `cdd`
    MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 974667393;

--
-- AUTO_INCREMENT `loca`
--
ALTER TABLE `loca`
    MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 18;

--
-- AUTO_INCREMENT `professor`
--
ALTER TABLE `professor`
    MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 8;

--
-- AUTO_INCREMENT `turma`
--
ALTER TABLE `turma`
    MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 15;

--
-- Restrições para tabelas
--

--
-- Restrições para a tabela `aluno`
--
ALTER TABLE `aluno`
    ADD CONSTRAINT `aluno_ibfk_1` FOREIGN KEY (`CODIGO_TURMA`) REFERENCES `turma` (`CODIGO`);

--
-- Restrições para a tabela `livro`
--
ALTER TABLE `livro`
    ADD CONSTRAINT `autor` FOREIGN KEY (`codigo_autor`) REFERENCES `autor` (`codigo`),
    ADD CONSTRAINT `cdd` FOREIGN KEY (`codigo_cdd`) REFERENCES `cdd` (`CODIGO`);

--
-- Restrições para a tabela `loca`
--
ALTER TABLE `loca`
    ADD CONSTRAINT `FK_codigo_livro` FOREIGN KEY (`codigo_livro`) REFERENCES `livro` (`codigo`),
    ADD CONSTRAINT `FK_cpfLocatario` FOREIGN KEY (`cpfLocatario`) REFERENCES `locatario` (`CPF`);

--
-- Restrições para a tabela `locatario`
--
ALTER TABLE `locatario`
    ADD CONSTRAINT `locatario_ibfk_1` FOREIGN KEY (`CODIGO_PROFESSOR`) REFERENCES `professor` (`CODIGO`),
    ADD CONSTRAINT `locatario_ibfk_2` FOREIGN KEY (`CODIGO_ALUNO`) REFERENCES `aluno` (`CODIGO`);
COMMIT;

--
-- Criação de TRIGGERS para loca
--

SET FOREIGN_KEY_CHECKS=0;

DELIMITER $

CREATE TRIGGER TGR_QTD_LIVRO_INSERT AFTER INSERT
ON LOCA
FOR EACH ROW
BEGIN
	UPDATE LIVRO SET QTD = QTD - 1
    WHERE CODIGO = NEW.CODIGO_LIVRO;
END$

CREATE TRIGGER TGR_QTD_LIVRO_DELETE AFTER DELETE
ON LOCA
FOR EACH ROW
BEGIN
	UPDATE LIVRO SET QTD = QTD + 1
    WHERE CODIGO = OLD.CODIGO_LIVRO;
END$

DELIMITER ;

SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
