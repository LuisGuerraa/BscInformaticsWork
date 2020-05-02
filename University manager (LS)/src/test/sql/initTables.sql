
insert into users
	values
		('professor1@gmail.com', 'Pedro Felix'),
		('professor2@outlook.com','Joao Oliveira'),
		('professor3@gmail.com', 'Jose Gomes'),
		('professor4@gmail.com','Manuel Machado'),
		('professor5@hotmail.com','Maria Tavares'),
		('aluno1@outlook.com', 'Filipe Cardoso'),
		('aluno2@gmail.com', 'Diogo Mendes'),
		('aluno3@hotmail.com', 'Joana Bastos'),
		('aluno4@outlook.com', 'Andre Santos'),
		('aluno5@gmail.com', 'Filipa Andrade'),
		('aluno6@hotmail.com', 'Tiago Santana'),
		('luis@hotmail.com','Luis Guerra'),
		('pedro@gmail.com', 'Pedro Tereso'),
		('wilson@hotmail.com', 'Wilson Costa');

insert into programme
	values
		('LEIC', 'Engenharia Informatica e de Computadores', 6),
		('LEIM', 'Engenharia Informatica e Multimedia', 6),
		('LMATE', 'Engenharia de Matematica Aplicada', 6);

insert into student
	values
		(40001, 'aluno1@outlook.com', 'LEIC'),
		(40002, 'aluno2@gmail.com', 'LEIC'),
		(40003, 'aluno3@hotmail.com', 'LEIC'),
		(40004, 'aluno4@outlook.com', 'LMATE'),
		(40005, 'aluno5@gmail.com', 'LMATE'),
		(40006, 'aluno6@hotmail.com', 'LMATE'),
		(43593, 'wilson@hotmail.com', 'LEIC'),
		(43755, 'luis@hotmail.com','LEIC'),
		(44015, 'pedro@gmail.com', 'LEIC');

insert into teacher
	values
		(1, 'professor1@gmail.com'),
		(2, 'professor2@outlook.com'),
		(3, 'professor3@gmail.com'),
		(4, 'professor4@gmail.com'),
		(5, 'professor5@hotmail.com');

insert into course
	values
		('LS', 'Laboratorio de Software', 1),
		('AVE', 'Ambientes Virtuais de Execucao', 2),
		('IA', 'Inteligencia Artificial', 3),
		('SO', 'Sistemas Operativos', 4),
		('MPD', 'Modelacao e Padroes Desenho', 5);

insert into semester
	values
		(1718, 'i'),
		(1819, 'i'),
		(1819, 'v');

insert into mandatory_course
	values
		('LS','LEIC', 4),
		('AVE','LEIC',4),
		('SO', 'LEIC', 4),
		('IA', 'LMATE', 4);

insert into optional_course
	values
		('MPD','LEIC',4),
		('IA', 'LEIC',4),
		('IA','LEIC', 6);

insert into class
	values
		('41D', 1718, 'i', 'LS'),
		('41D', 1718, 'i', 'AVE'),
		('41D', 1718, 'i', 'SO'),

		('41D', 1819, 'v','LS'),
		('41D', 1819, 'v','AVE'),
		('41D', 1819, 'v','SO'),
		('41D', 1819, 'v','IA'),
		('41D', 1819, 'v','MPD'),

		('41D', 1819, 'i','LS'),
		('41D', 1819, 'i','AVE'),
		('41D', 1819, 'i','SO'),

		('42D', 1819, 'v','LS'),
		('42D', 1819, 'v','AVE'),
		('42D', 1819, 'v','SO'),
		('42D', 1819, 'v','MPD'),
		('42D', 1819, 'v','IA'),

		('42D', 1819, 'i','LS'),
		('42D', 1819, 'i','AVE'),
		('42D', 1819, 'i','SO');


insert into teacher_class
	values
		('41D', 1718, 'i', 1, 'LS'),
		('41D', 1718, 'i', 2, 'AVE'),
		('41D', 1718, 'i', 3, 'SO'),

		('41D', 1819, 'v', 1,'LS'),
		('41D', 1819, 'v', 2,'AVE'),
		('41D', 1819, 'v', 3,'SO'),
		('41D', 1819, 'v', 4,'IA'),
		('41D', 1819, 'v', 5,'MPD'),

		('41D', 1819, 'i', 1, 'LS'),
		('41D', 1819, 'i', 2, 'AVE'),
		('41D', 1819, 'i', 3, 'SO'),

		('42D', 1819, 'v', 1, 'LS'),
		('42D', 1819, 'v', 2, 'AVE'),
		('42D', 1819, 'v', 3, 'SO'),
		('42D', 1819, 'v', 4, 'MPD'),
		('42D', 1819, 'v', 5, 'IA'),

		('42D', 1819, 'i', 1, 'LS'),
		('42D', 1819, 'i', 2, 'AVE'),
		('42D', 1819, 'i', 3, 'SO');

insert into student_class
	values
		(43593, '41D', 1819, 'v', 'LS'),
		(43755, '41D', 1819, 'v', 'LS'),
		(44015, '41D', 1819, 'v', 'LS'),

		(43593, '41D', 1819, 'v', 'AVE'),
		(43755, '41D', 1819, 'v', 'AVE'),
		(44015, '41D', 1819, 'v', 'AVE'),

		(43593, '41D', 1819, 'v', 'SO'),
		(43755, '42D', 1819, 'v', 'SO'),
		(44015, '41D', 1819, 'v', 'SO'),

		(40001, '41D', 1819, 'v', 'LS'),
		(40002, '41D', 1819, 'v', 'LS'),
		(40003, '41D', 1819, 'v', 'LS'),

		(40004, '41D', 1819, 'v', 'IA'),
		(40005, '41D', 1819, 'v', 'IA'),
		(40006, '41D', 1819, 'v', 'IA');


insert into groups
	values
		(1, '41D', 1819, 'v', 'LS'),
		(2, '41D', 1819, 'v', 'LS'),
		(1, '41D', 1819, 'v', 'AVE'),
		(1, '41D', 1819, 'v', 'SO'),
		(1, '42D', 1819, 'v', 'SO'),
		(1, '41D', 1819, 'v', 'IA');

insert into group_members
	values
		(1, 44015, '41D', 1819, 'v', 'LS'),
		(1, 43593, '41D', 1819, 'v', 'LS'),
		(1, 43755, '41D', 1819, 'v', 'LS'),

		(2, 40001, '41D', 1819, 'v', 'LS'),
		(2, 40002, '41D', 1819, 'v', 'LS'),
		(2, 40003, '41D', 1819, 'v', 'LS'),


		(1, 44015, '41D', 1819, 'v', 'AVE'),
		(1, 43593, '41D', 1819, 'v', 'AVE'),
		(1, 43755, '41D', 1819, 'v', 'AVE'),


		(1, 43755, '42D', 1819, 'v', 'SO'),

		(1, 43593, '41D', 1819, 'v', 'SO'),
		(1, 44015, '41D', 1819, 'v', 'SO'),


		(1, 40004, '41D', 1819, 'v', 'IA'),
		(1, 40005, '41D', 1819, 'v', 'IA'),
		(1, 40006, '41D', 1819, 'v', 'IA');
