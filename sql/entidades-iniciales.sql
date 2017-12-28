-- Ajustar el contador de IDs para que no colision con los IDs anteriores
UPDATE USUARIO_GEN set GEN_VAL=100 WHERE GEN_NAME='USUARIO_GEN';


-- Administrador inicial con login "admin" y contraseña "admin"
INSERT INTO `ADMINISTRADOR` (`ID`,`FECHAALTA`,`LOGIN`,`NOMBRE`,
                             `PASSWORD`,`TIPO_USUARIO`,`ULTIMOACCESO`)
   VALUES (1,'2017-11-11 01:04:42','admin','Administrador inicial',
           'ggm44T97GWJ6Rryx3do4yvl1bZ+gmUfG','ADMINISTRADOR','2017-11-11 01:04:42');

-- Medico con dni "11111111A", num. colegiado "11111" y contraseña "11111"
INSERT INTO `CENTROSALUD` (`ID`,`NOMBRE`,`TELEFONO`,`CODIGOPOSTAL`,
                           `DOMICILIO`,`LOCALIDAD`,`PROVINCIA`)
   VALUES (1,'Centro salud pepe','988888888','12345',
           'C/. Pepe, nº 3','Ourense','Ourense');
INSERT INTO `MEDICO` (`ID`,`APELLIDOS`,`DNI`,`EMAIL`,`FECHAALTA`,`NOMBRE`,
                      `NUMEROCOLEGIADO`,`PASSWORD`,`TELEFONO`,`TIPO_USUARIO`,
                      `ULTIMOACCESO`,`CENTROSALUD_ID`)
   VALUES (2,'Gomez Gomez','11111111A','a@a.com','2017-11-11 01:04:42','Antonio',
           '11111','lUTQ2zg2voe4Z5OqpITFIjcBziNH10d6','988123456','MEDICO',
           '2017-11-11 01:04:42',1),
		   (4, 'Over', '99999999A', 'bendover@yahoo.com', '1980-12-01 06:16:26', 'Bend',
		   '9999999999', 'lUTQ2zg2voe4Z5OqpITFIjcBziNH10d6', '988264573', 'MEDICO',
		   '2017-12-01 00:00:00', 1);

-- Paciente con dni "22222222B", num. tarjeta sanitaria "2222222222", num seg. social "2222222222222" y contraseña "22222"
INSERT INTO `PACIENTE` (`ID`,`APELLIDOS`,`DNI`,`EMAIL`,`FECHAALTA`,
                        `NOMBRE`,`NUMEROSEGURIDADSOCIAL`,`NUMEROTARJETASANITARIA`,
                        `PASSWORD`,`TELEFONO`,`TIPO_USUARIO`,`ULTIMOACCESO`,
                        `CODIGOPOSTAL`,`DOMICILIO`,`LOCALIDAD`,`PROVINCIA`,`MEDICO_ID`)
   VALUES (3,'Benito Carmona','22222222B','b@b.com','2017-11-11 01:04:42',
           'Ana','2222222222222','2222222222',
           'auJIfVxFAN0IKkDVovGmzfUENiABIC5g','981123456','PACIENTE','2017-11-11 01:04:42',
           '12345','C/. Benito, nº 2, 4º N','Coruña','Coruña',2),
		   (5, 'Nitales', '88888888L', 'jorge@outlook.com', '2017-12-03 00:00:00',
		   'Jorge', '7777777777777', '2222222222',
		   'auJIfVxFAN0IKkDVovGmzfUENiABIC5g', '977456723', 'PACIENTE', '2017-12-03 04:12:15',
		   '32890', 'Narnia 9, entresuelo A', 'Narnia town', 'Narnia', 2);

-- Cita con fecha "2017-11-23" y hora "09:00:00"
INSERT INTO `CITA`(`ID`,`DURACION`,`ESTADO`,`FECHA`,`HORA`,`MEDICO_ID`,`PACIENTE_ID`)
   VALUES (1, 15, 'PLANIFICADA', '2017-11-23', '09:00:00', '2', '3');

-- Farmacia con nif "33333333C" y contraseña "33333"
INSERT INTO `FARMACIA`(`ID`,`FECHAALTA`,`NIF`,`NOMBREFARMACIA`,
                       `PASSWORD`,`TIPO_USUARIO`,`ULTIMOACCESO`,
                       `CODIGOPOSTAL`,`DOMICILIO`,`LOCALIDAD`,`PROVINCIA`)
   VALUES (4,'2017-11-11 01:04:42','33333333C','Farmacia de prueba',
           '/QpUw+ZRH3ndoNb3N4gRpT5cz0C7pT9v','FARMACIA','2017-11-11 01:04:42',
           '12345','C/. Farmacia, nº 2, 4º N','Coruña','Coruña');
		   
-- Medicamentos de ejemplo
INSERT INTO `MEDICAMENTO` (`ID`, `FABRICANTE`, `FAMILIA`, `NOMBRE`, `NUMERODOSIS`, `PRINCIPIOACTIVO`) VALUES
(1, 'Bayern', 'Familia de Bayern', 'Hard Drugs', 3, 'Cocaine'),
(2, 'IbuprofenPRO', 'Familia de IbuprofenPRO', 'DailyIbuprofen 3.0', 2, 'Codeina');

-- Prescripción de ejemplo
INSERT INTO `PRESCRIPCION` (`ID`, `DOSIS`, `FECHAFIN`, `FECHAINICIO`, `INDICACIONES`, `MEDICAMENTO_ID`, `MEDICO_ID`, `PACIENTE_ID`) VALUES
(1, 3, '2018-01-26', '2017-12-01', 'Tomar con mucho cuidado la pastilla. Ha causado accidentes graves.', 1, 2, 3),
(2, '2', '2018-01-15', '2017-12-30', 'No se drogue.', '2', '2', '3');
