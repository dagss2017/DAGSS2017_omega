SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `dagss`
--
CREATE DATABASE IF NOT EXISTS `dagss` DEFAULT CHARACTER SET latin1 COLLATE latin1_spanish_ci;
USE `dagss`;
GRANT all privileges ON dagss.* to dagss@localhost identified by "dagss";

-- --------------------------------------------------------

--
-- Table structure for table `ADMINISTRADOR`
--

DROP TABLE IF EXISTS `ADMINISTRADOR`;
CREATE TABLE IF NOT EXISTS `ADMINISTRADOR` (
  `ID` bigint(20) NOT NULL,
  `FECHAALTA` datetime DEFAULT NULL,
  `LOGIN` varchar(25) DEFAULT NULL,
  `NOMBRE` varchar(75) DEFAULT NULL,
  `PASSWORD` varchar(64) DEFAULT NULL,
  `TIPO_USUARIO` varchar(20) DEFAULT NULL,
  `ULTIMOACCESO` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ADMINISTRADOR`
--

INSERT INTO `ADMINISTRADOR` (`ID`, `FECHAALTA`, `LOGIN`, `NOMBRE`, `PASSWORD`, `TIPO_USUARIO`, `ULTIMOACCESO`) VALUES
(1, '2017-11-11 01:04:42', 'admin', 'Administrador inicial', 'ggm44T97GWJ6Rryx3do4yvl1bZ+gmUfG', 'ADMINISTRADOR', '2017-11-11 01:04:42');

-- --------------------------------------------------------

--
-- Table structure for table `CENTROSALUD`
--

DROP TABLE IF EXISTS `CENTROSALUD`;
CREATE TABLE IF NOT EXISTS `CENTROSALUD` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(50) NOT NULL,
  `TELEFONO` varchar(9) DEFAULT NULL,
  `CODIGOPOSTAL` varchar(5) NOT NULL,
  `DOMICILIO` varchar(75) NOT NULL,
  `LOCALIDAD` varchar(30) NOT NULL,
  `PROVINCIA` varchar(30) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CENTROSALUD`
--

INSERT INTO `CENTROSALUD` (`ID`, `NOMBRE`, `TELEFONO`, `CODIGOPOSTAL`, `DOMICILIO`, `LOCALIDAD`, `PROVINCIA`) VALUES
(1, 'Centro salud pepe', '988888888', '12345', 'C/. Pepe, nº 3', 'Ourense', 'Ourense');

-- --------------------------------------------------------

--
-- Table structure for table `CITA`
--

DROP TABLE IF EXISTS `CITA`;
CREATE TABLE IF NOT EXISTS `CITA` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DURACION` int(11) DEFAULT NULL,
  `ESTADO` varchar(255) DEFAULT NULL,
  `FECHA` date DEFAULT NULL,
  `HORA` time DEFAULT NULL,
  `MEDICO_ID` bigint(20) DEFAULT NULL,
  `PACIENTE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CITA_PACIENTE_ID` (`PACIENTE_ID`),
  KEY `FK_CITA_MEDICO_ID` (`MEDICO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CITA`
--

INSERT INTO `CITA` (`ID`, `DURACION`, `ESTADO`, `FECHA`, `HORA`, `MEDICO_ID`, `PACIENTE_ID`) VALUES
(1, 15, 'PLANIFICADA', '2017-11-23', '09:00:00', 2, 3),
(2, 20, 'PLANIFICADA', '2018-01-14', '09:00:00', 2, 3),
(3, 30, 'ANULADA', '2018-01-11', '12:00:00', 2, 3),
(4, 10, 'AUSENTE', '2018-01-02', '14:20:00', 2, 3),
(5, 45, 'COMPLETADA', '2018-01-02', '20:00:00', 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `FARMACIA`
--

DROP TABLE IF EXISTS `FARMACIA`;
CREATE TABLE IF NOT EXISTS `FARMACIA` (
  `ID` bigint(20) NOT NULL,
  `FECHAALTA` datetime DEFAULT NULL,
  `NIF` varchar(9) NOT NULL,
  `NOMBREFARMACIA` varchar(75) NOT NULL,
  `PASSWORD` varchar(64) DEFAULT NULL,
  `TIPO_USUARIO` varchar(20) DEFAULT NULL,
  `ULTIMOACCESO` datetime DEFAULT NULL,
  `CODIGOPOSTAL` varchar(5) NOT NULL,
  `DOMICILIO` varchar(75) NOT NULL,
  `LOCALIDAD` varchar(30) NOT NULL,
  `PROVINCIA` varchar(30) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `FARMACIA`
--

INSERT INTO `FARMACIA` (`ID`, `FECHAALTA`, `NIF`, `NOMBREFARMACIA`, `PASSWORD`, `TIPO_USUARIO`, `ULTIMOACCESO`, `CODIGOPOSTAL`, `DOMICILIO`, `LOCALIDAD`, `PROVINCIA`) VALUES
(4, '2017-11-11 01:04:42', '33333333C', 'Farmacia de prueba', '/QpUw+ZRH3ndoNb3N4gRpT5cz0C7pT9v', 'FARMACIA', '2018-01-11 16:49:16', '12345', 'C/. Farmacia, nº 2, 4º N', 'Coruña', 'Coruña');

-- --------------------------------------------------------

--
-- Table structure for table `MEDICAMENTO`
--

DROP TABLE IF EXISTS `MEDICAMENTO`;
CREATE TABLE IF NOT EXISTS `MEDICAMENTO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FABRICANTE` varchar(50) NOT NULL,
  `FAMILIA` varchar(50) NOT NULL,
  `NOMBRE` varchar(50) NOT NULL,
  `NUMERODOSIS` int(11) DEFAULT NULL,
  `PRINCIPIOACTIVO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MEDICAMENTO`
--

INSERT INTO `MEDICAMENTO` (`ID`, `FABRICANTE`, `FAMILIA`, `NOMBRE`, `NUMERODOSIS`, `PRINCIPIOACTIVO`) VALUES
(1, 'UCB PHARMA', 'Ansiolitico, antihistaminico', 'Atarax', 32, 'Hidroxicina'),
(2, 'GLAXOSMITHKLINE', 'Antibiotico', 'Augmentine', 12, 'Amoxicilina y acido clabulanico'),
(3, 'AVENTIS PHARMA', 'Antibiotico', 'Rhodogil', 6, 'Espiramicina y metronidazol'),
(4, 'SANOFI AVENTIS S.A.', 'Diuretico', 'Seguril', 24, 'Furosemida'),
(5, 'BOEHRINGER INGELHEIM S.A.', 'Analgesico, antipiretico', 'Nolotil', 18, 'Metamizol'),
(6, 'MYLAN PHARMACEUTICALS', 'Analgesico, antiinflamatorio', 'Neobrufen', 32, 'Ibuprofeno'),
(7, 'ALCALA FARMA S.L.', 'Analgesico, antipiretico', 'Termalgin', 24, 'Paracetamol'),
(8, 'MYLAN PHARMACEUTICALS', 'Analgesico, antiinflamatorio', 'Dalsy', 32, 'Ibuprofeno'),
(9, 'CINFA', 'Antiinflamatorio', 'Cortef', 16, 'Hidrocortisona'),
(10, 'MERCK, SHARP AND DOHME', 'Bifosfonato', 'Fosamax', 20, 'Alendronato sodico'),
(11, 'ROCHE PHARMA', 'Bifosfonato', 'Bonviva', 12, 'Ibandronato'),
(12, 'Laboratorios VIR', 'Antihipertensivo', 'Zabart', 18, 'Amlodipino'),
(13, 'CINFA', 'Antitusigeno', 'Cinfatos', 24, 'Dextrometorfano'),
(14, 'Bayer', 'Analgesico, antipiretico', 'Aspirina', 32, 'Acido Acetil Salicilico');

-- --------------------------------------------------------

--
-- Table structure for table `MEDICO`
--

DROP TABLE IF EXISTS `MEDICO`;
CREATE TABLE IF NOT EXISTS `MEDICO` (
  `ID` bigint(20) NOT NULL,
  `APELLIDOS` varchar(50) NOT NULL,
  `DNI` varchar(9) NOT NULL,
  `EMAIL` varchar(25) DEFAULT NULL,
  `FECHAALTA` datetime DEFAULT NULL,
  `NOMBRE` varchar(30) NOT NULL,
  `NUMEROCOLEGIADO` varchar(10) NOT NULL,
  `PASSWORD` varchar(64) DEFAULT NULL,
  `TELEFONO` varchar(9) DEFAULT NULL,
  `TIPO_USUARIO` varchar(20) DEFAULT NULL,
  `ULTIMOACCESO` datetime DEFAULT NULL,
  `CENTROSALUD_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_MEDICO_CENTROSALUD_ID` (`CENTROSALUD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MEDICO`
--

INSERT INTO `MEDICO` (`ID`, `APELLIDOS`, `DNI`, `EMAIL`, `FECHAALTA`, `NOMBRE`, `NUMEROCOLEGIADO`, `PASSWORD`, `TELEFONO`, `TIPO_USUARIO`, `ULTIMOACCESO`, `CENTROSALUD_ID`) VALUES
(2, 'Gomez Gomez', '11111111A', 'a@a.com', '2017-11-11 01:04:42', 'Antonio', '11111', 'lUTQ2zg2voe4Z5OqpITFIjcBziNH10d6', '988123456', 'MEDICO', '2017-11-11 01:04:42', 1);

-- --------------------------------------------------------

--
-- Table structure for table `PACIENTE`
--

DROP TABLE IF EXISTS `PACIENTE`;
CREATE TABLE IF NOT EXISTS `PACIENTE` (
  `ID` bigint(20) NOT NULL,
  `APELLIDOS` varchar(50) NOT NULL,
  `DNI` varchar(9) NOT NULL,
  `EMAIL` varchar(25) DEFAULT NULL,
  `FECHAALTA` datetime DEFAULT NULL,
  `NOMBRE` varchar(30) NOT NULL,
  `NUMEROSEGURIDADSOCIAL` varchar(13) NOT NULL,
  `NUMEROTARJETASANITARIA` varchar(10) NOT NULL,
  `PASSWORD` varchar(64) DEFAULT NULL,
  `TELEFONO` varchar(9) DEFAULT NULL,
  `TIPO_USUARIO` varchar(20) DEFAULT NULL,
  `ULTIMOACCESO` datetime DEFAULT NULL,
  `CODIGOPOSTAL` varchar(5) NOT NULL,
  `DOMICILIO` varchar(75) NOT NULL,
  `LOCALIDAD` varchar(30) NOT NULL,
  `PROVINCIA` varchar(30) NOT NULL,
  `MEDICO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PACIENTE_MEDICO_ID` (`MEDICO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PACIENTE`
--

INSERT INTO `PACIENTE` (`ID`, `APELLIDOS`, `DNI`, `EMAIL`, `FECHAALTA`, `NOMBRE`, `NUMEROSEGURIDADSOCIAL`, `NUMEROTARJETASANITARIA`, `PASSWORD`, `TELEFONO`, `TIPO_USUARIO`, `ULTIMOACCESO`, `CODIGOPOSTAL`, `DOMICILIO`, `LOCALIDAD`, `PROVINCIA`, `MEDICO_ID`) VALUES
(3, 'Benito Carmona', '22222222B', 'b@b.com', '2017-11-11 01:04:42', 'Ana', '2222222222222', '2222222222', 'auJIfVxFAN0IKkDVovGmzfUENiABIC5g', '981123456', 'PACIENTE', '2017-11-11 01:04:42', '12345', 'C/. Benito, nº 2, 4º N', 'Coruña', 'Coruña', 2);

-- --------------------------------------------------------

--
-- Table structure for table `PRESCRIPCION`
--

DROP TABLE IF EXISTS `PRESCRIPCION`;
CREATE TABLE IF NOT EXISTS `PRESCRIPCION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DOSIS` int(11) DEFAULT NULL,
  `FECHAFIN` date DEFAULT NULL,
  `FECHAINICIO` date DEFAULT NULL,
  `INDICACIONES` varchar(255) DEFAULT NULL,
  `MEDICAMENTO_ID` bigint(20) DEFAULT NULL,
  `MEDICO_ID` bigint(20) DEFAULT NULL,
  `PACIENTE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRESCRIPCION_MEDICO_ID` (`MEDICO_ID`),
  KEY `FK_PRESCRIPCION_PACIENTE_ID` (`PACIENTE_ID`),
  KEY `FK_PRESCRIPCION_MEDICAMENTO_ID` (`MEDICAMENTO_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `PRESCRIPCION`
--

INSERT INTO `PRESCRIPCION` (`ID`, `DOSIS`, `FECHAFIN`, `FECHAINICIO`, `INDICACIONES`, `MEDICAMENTO_ID`, `MEDICO_ID`, `PACIENTE_ID`) VALUES
(1, 3, '2018-04-07', '2018-01-11', 'Tomar solo las dosis prescritas. Peligroso.', 1, 2, 3),
(2, 5, '2018-04-07', '2018-01-11', 'Puede notar dolores de estomago.', 2, 2, 3),
(3, 10, '2018-04-07', '2018-01-11', 'Causa hemorragias nasales.', 4, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `RECETA`
--

DROP TABLE IF EXISTS `RECETA`;
CREATE TABLE IF NOT EXISTS `RECETA` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CANTIDAD` int(11) DEFAULT NULL,
  `ESTADORECETA` varchar(20) DEFAULT NULL,
  `FINVALIDEZ` date DEFAULT NULL,
  `INICIOVALIDEZ` date DEFAULT NULL,
  `FARMACIADISPENSADORA_ID` bigint(20) DEFAULT NULL,
  `PRESCRIPCION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RECETA_FARMACIADISPENSADORA_ID` (`FARMACIADISPENSADORA_ID`),
  KEY `FK_RECETA_PRESCRIPCION_ID` (`PRESCRIPCION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `RECETA`
--

INSERT INTO `RECETA` (`ID`, `CANTIDAD`, `ESTADORECETA`, `FINVALIDEZ`, `INICIOVALIDEZ`, `FARMACIADISPENSADORA_ID`, `PRESCRIPCION_ID`) VALUES
(1, 1, 'GENERADA', '2018-02-24', '2018-01-11', 4, 1),
(2, 2, 'SERVIDA', '2018-03-24', '2018-01-11', 4, 1),
(3, 3, 'ANULADA', '2018-04-24', '2018-01-11', 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `USUARIO_GEN`
--

DROP TABLE IF EXISTS `USUARIO_GEN`;
CREATE TABLE IF NOT EXISTS `USUARIO_GEN` (
  `GEN_NAME` varchar(50) NOT NULL,
  `GEN_VAL` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`GEN_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `USUARIO_GEN`
--

INSERT INTO `USUARIO_GEN` (`GEN_NAME`, `GEN_VAL`) VALUES
('USUARIO_GEN', '100');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `CITA`
--
ALTER TABLE `CITA`
  ADD CONSTRAINT `FK_CITA_MEDICO_ID` FOREIGN KEY (`MEDICO_ID`) REFERENCES `MEDICO` (`ID`),
  ADD CONSTRAINT `FK_CITA_PACIENTE_ID` FOREIGN KEY (`PACIENTE_ID`) REFERENCES `PACIENTE` (`ID`);

--
-- Constraints for table `MEDICO`
--
ALTER TABLE `MEDICO`
  ADD CONSTRAINT `FK_MEDICO_CENTROSALUD_ID` FOREIGN KEY (`CENTROSALUD_ID`) REFERENCES `CENTROSALUD` (`ID`);

--
-- Constraints for table `PACIENTE`
--
ALTER TABLE `PACIENTE`
  ADD CONSTRAINT `FK_PACIENTE_MEDICO_ID` FOREIGN KEY (`MEDICO_ID`) REFERENCES `MEDICO` (`ID`);

--
-- Constraints for table `PRESCRIPCION`
--
ALTER TABLE `PRESCRIPCION`
  ADD CONSTRAINT `FK_PRESCRIPCION_MEDICAMENTO_ID` FOREIGN KEY (`MEDICAMENTO_ID`) REFERENCES `MEDICAMENTO` (`ID`),
  ADD CONSTRAINT `FK_PRESCRIPCION_MEDICO_ID` FOREIGN KEY (`MEDICO_ID`) REFERENCES `MEDICO` (`ID`),
  ADD CONSTRAINT `FK_PRESCRIPCION_PACIENTE_ID` FOREIGN KEY (`PACIENTE_ID`) REFERENCES `PACIENTE` (`ID`);

--
-- Constraints for table `RECETA`
--
ALTER TABLE `RECETA`
  ADD CONSTRAINT `FK_RECETA_FARMACIADISPENSADORA_ID` FOREIGN KEY (`FARMACIADISPENSADORA_ID`) REFERENCES `FARMACIA` (`ID`),
  ADD CONSTRAINT `FK_RECETA_PRESCRIPCION_ID` FOREIGN KEY (`PRESCRIPCION_ID`) REFERENCES `PRESCRIPCION` (`ID`);
