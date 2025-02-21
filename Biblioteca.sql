-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 16-Ago-2018 às 03:24
-- Versão do servidor: 10.1.31-MariaDB
-- PHP Version: 7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
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

CREATE TABLE `adms` (
  `codigo` int(11) NOT NULL,
  `login` varchar(15) NOT NULL,
  `senha` varchar(40) NOT NULL,
  `nome` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `adms`
--

INSERT INTO `adms` (`codigo`, `login`, `senha`, `nome`) VALUES
(1, '084.278.063-70', '19851988', 'Israel'),
(2, '084.278.063-70', '06042001', 'Fabricio');

-- --------------------------------------------------------

--
-- Estrutura da tabela `aluno`
--

CREATE TABLE `aluno` (
  `CODIGO` int(11) NOT NULL,
  `CODIGO_TURMA` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `aluno`
--

INSERT INTO `aluno` (`CODIGO`, `CODIGO_TURMA`) VALUES
(14, 2),
(12, 8),
(13, 8),
(9, 9),
(10, 9),
(11, 10);

-- --------------------------------------------------------

--
-- Estrutura da tabela `autor`
--

CREATE TABLE `autor` (
  `codigo` int(11) NOT NULL,
  `nome` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `autor`
--

INSERT INTO `autor` (`codigo`, `nome`) VALUES
(3, 'FABRICIO DE CAPRIO'),
(4, 'TAFINHA RET'),
(5, 'RING STONE'),
(6, 'ZORAIDE PANDA'),
(7, 'VCCCCCCCCC');

-- --------------------------------------------------------

--
-- Estrutura da tabela `cdd`
--

CREATE TABLE `cdd` (
  `CODIGO` int(11) NOT NULL,
  `DESCRICAO` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `cdd`
--

INSERT INTO `cdd` (`CODIGO`, `DESCRICAO`) VALUES
(1, 'AVENTURA'),
(2, 'FANTASIA'),
(3, 'FICCAO'),
(4, 'COMEDIA'),
(5, 'DRAMA'),
(15436778, 'TIRAMOS EU TIRO'),
(453453545, 'TESTE'),
(974667392, 'FABRICINHO ESTRESSADO');

-- --------------------------------------------------------

--
-- Estrutura da tabela `livro`
--

CREATE TABLE `livro` (
  `codigo` bigint(13) NOT NULL,
  `titulo` varchar(45) NOT NULL,
  `codigo_autor` int(11) DEFAULT NULL,
  `codigo_cdd` int(11) DEFAULT NULL,
  `qtd` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `livro`
--

INSERT INTO `livro` (`codigo`, `titulo`, `codigo_autor`, `codigo_cdd`, `qtd`) VALUES
(34356729, 'EU TU TU TU', 6, 2, 1000),
(1234567891675, 'A NACONDA', 6, 1, 10),
(3414344535455, 'UM MALUCO NO PEDACO', 4, 4, 5),
(4354543543455, 'ERA UMA VEZ NO MATO', 5, 5, 6),
(7463871928273, 'OS ESTRESSES DA VIDA', 6, 974667392, 1000);

-- --------------------------------------------------------

--
-- Estrutura da tabela `loca`
--

CREATE TABLE `loca` (
  `codigo` int(11) NOT NULL,
  `codigo_livro` bigint(13) NOT NULL,
  `cpfLocatario` varchar(14) NOT NULL,
  `dataDeLocacao` date NOT NULL,
  `dataParaDevolucao` date NOT NULL,
  `atrasado` varchar(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `loca`
--

INSERT INTO `loca` (`codigo`, `codigo_livro`, `cpfLocatario`, `dataDeLocacao`, `dataParaDevolucao`, `atrasado`) VALUES
(14, 3414344535455, '084.278.063-70', '2018-06-08', '2018-06-09', 'Y'),
(15, 1234567891675, '785.475.466-29', '2018-06-08', '2018-06-08', 'Y'),
(16, 1234567891675, '886.675.556-75', '2018-06-08', '2018-06-17', 'Y'),
(17, 4354543543455, '543.403.434-30', '2018-06-08', '2018-06-10', 'Y');

-- --------------------------------------------------------

--
-- Estrutura da tabela `locatario`
--

CREATE TABLE `locatario` (
  `CPF` varchar(14) NOT NULL,
  `NOME` varchar(45) NOT NULL,
  `telefone` varchar(14) DEFAULT NULL,
  `CODIGO_PROFESSOR` int(11) DEFAULT NULL,
  `CODIGO_ALUNO` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `locatario`
--

INSERT INTO `locatario` (`CPF`, `NOME`, `telefone`, `CODIGO_PROFESSOR`, `CODIGO_ALUNO`) VALUES
('084.278.063-70', 'FABRICIO SOUZA', '(88)99942-6000', NULL, 10),
('343.958.938-37', 'APARECIDA', '(88)43434-2434', 7, NULL),
('453.454.397-79', 'ZORAIDE VICENTE', '(88)99353-4656', NULL, 13),
('543.403.434-30', 'JOSE', '(86)84749-0384', NULL, 12),
('754.958.947-64', 'ISRAEL', '(99)99999-9999', 6, NULL),
('785.475.466-29', 'TAFINHA', '(88)99934-9343', NULL, 11),
('886.675.556-75', 'ZORAIDE', '(88)89765-6757', NULL, 14);

-- --------------------------------------------------------

--
-- Estrutura da tabela `professor`
--

CREATE TABLE `professor` (
  `CODIGO` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `professor`
--

INSERT INTO `professor` (`CODIGO`) VALUES
(6),
(7);

-- --------------------------------------------------------

--
-- Estrutura da tabela `turma`
--

CREATE TABLE `turma` (
  `CODIGO` int(11) NOT NULL,
  `DESCRICAO` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `turma`
--

INSERT INTO `turma` (`CODIGO`, `DESCRICAO`) VALUES
(1, '1º ADM'),
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
-- Indexes for dumped tables
--

--
-- Indexes for table `adms`
--
ALTER TABLE `adms`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `aluno`
--
ALTER TABLE `aluno`
  ADD PRIMARY KEY (`CODIGO`),
  ADD KEY `CODIGO_TURMA` (`CODIGO_TURMA`);

--
-- Indexes for table `autor`
--
ALTER TABLE `autor`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `cdd`
--
ALTER TABLE `cdd`
  ADD PRIMARY KEY (`CODIGO`);

--
-- Indexes for table `livro`
--
ALTER TABLE `livro`
  ADD PRIMARY KEY (`codigo`),
  ADD KEY `autor` (`codigo_autor`),
  ADD KEY `cdd` (`codigo_cdd`);

--
-- Indexes for table `loca`
--
ALTER TABLE `loca`
  ADD PRIMARY KEY (`codigo`),
  ADD KEY `FK_cpfLocatario` (`cpfLocatario`),
  ADD KEY `FK_codigo_livro` (`codigo_livro`);

--
-- Indexes for table `locatario`
--
ALTER TABLE `locatario`
  ADD PRIMARY KEY (`CPF`),
  ADD KEY `CODIGO_PROFESSOR` (`CODIGO_PROFESSOR`),
  ADD KEY `CODIGO_ALUNO` (`CODIGO_ALUNO`);

--
-- Indexes for table `professor`
--
ALTER TABLE `professor`
  ADD PRIMARY KEY (`CODIGO`);

--
-- Indexes for table `turma`
--
ALTER TABLE `turma`
  ADD PRIMARY KEY (`CODIGO`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `adms`
--
ALTER TABLE `adms`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `aluno`
--
ALTER TABLE `aluno`
  MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `autor`
--
ALTER TABLE `autor`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `cdd`
--
ALTER TABLE `cdd`
  MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=974667393;

--
-- AUTO_INCREMENT for table `loca`
--
ALTER TABLE `loca`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `professor`
--
ALTER TABLE `professor`
  MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `turma`
--
ALTER TABLE `turma`
  MODIFY `CODIGO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `aluno`
--
ALTER TABLE `aluno`
  ADD CONSTRAINT `aluno_ibfk_1` FOREIGN KEY (`CODIGO_TURMA`) REFERENCES `turma` (`CODIGO`);

--
-- Limitadores para a tabela `livro`
--
ALTER TABLE `livro`
  ADD CONSTRAINT `autor` FOREIGN KEY (`codigo_autor`) REFERENCES `autor` (`codigo`),
  ADD CONSTRAINT `cdd` FOREIGN KEY (`codigo_cdd`) REFERENCES `cdd` (`CODIGO`);

--
-- Limitadores para a tabela `loca`
--
ALTER TABLE `loca`
  ADD CONSTRAINT `FK_codigo_livro` FOREIGN KEY (`codigo_livro`) REFERENCES `livro` (`codigo`),
  ADD CONSTRAINT `FK_cpfLocatario` FOREIGN KEY (`cpfLocatario`) REFERENCES `locatario` (`CPF`);

--
-- Limitadores para a tabela `locatario`
--
ALTER TABLE `locatario`
  ADD CONSTRAINT `locatario_ibfk_1` FOREIGN KEY (`CODIGO_PROFESSOR`) REFERENCES `professor` (`CODIGO`),
  ADD CONSTRAINT `locatario_ibfk_2` FOREIGN KEY (`CODIGO_ALUNO`) REFERENCES `aluno` (`CODIGO`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
