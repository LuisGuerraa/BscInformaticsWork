INSERT INTO project values
('Grupo 3', 'Project of Group 3'),
('Grupo 4', 'Project of Group 4'),
('Grupo 5', 'Project of Group 5');

INSERT INTO transition values
('Grupo 3','open', 'closed'),
('Grupo 3','closed','open'),
('Grupo 3','closed','archived'),
('Grupo 3','archived', 'archived');

INSERT INTO project_issuelabel VALUES('Grupo 3','defect'), ('Grupo 3','new_functionality'), ('Grupo 3','exploration');

INSERT INTO issue(projectname,"name",description,statetype,creationdate,closedate)
	VALUES('Grupo 3','Model','Problem with model','open',CURRENT_DATE, CURRENT_DATE + INTERVAL '1 day');

INSERT INTO issue_issuelabel(projectname,issueid,statetype,labeltype) values
	('Grupo 3', 1, 'open', 'defect'),
	('Grupo 3', 1, 'open', 'new_functionality'),
	('Grupo 3', 1, 'open', 'exploration');	

