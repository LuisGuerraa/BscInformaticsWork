use SI2
insert into Departamento values ('ADEETC', 'Departamento de Engenharia Eletronica, Telecomunicações e Computadores')
insert into Departamento values ('ADEQ' , 'Departamento de Quimica')
insert into Departamento values ('ADEM' , 'Departamento de Mecânica')


insert into Curso values ('LEIC','Licenciatura em Engenharia Informática e Computadores',180,'ADEETC')
insert into Semestre values (1), (2), (3),(4),(5),(6)


insert into Cur_Sem values ('LEIC',1)
insert into Cur_Sem values ('LEIC',2)
insert into Cur_Sem values ('LEIC',3)
insert into Cur_Sem values ('LEIC',4)
insert into Cur_Sem values ('LEIC',5)
insert into Cur_Sem values ('LEIC',6)

insert into Professor values ('Afonso Remédios', 185353333,'Sistemas de Informação','A') -- nao sabemos o que é uma categoria
insert into Professor values ('Miguel Gamboa', 123781391,'Programação Orientada por Objectos','A') -- nao sabemos o que é uma categoria
insert into Professor values ('Paulo Pereira', 12313141,'Programação Web','A')
insert into Professor values ('Pedro Félix', 14235252,'Programação','A')

insert into Secção values ('Secção 1', 'ADEETC', ' Coluna da secção 1' ,185353333)
insert into Secção values ('Secção 2', 'ADEETC', ' Coluna da secção 2' ,123781391)

insert into Sec_Prof values('Secção 1',185353333)
insert into Sec_Prof values('Secção 2',123781391)
insert into Sec_Prof values('Secção 1',12313141)
insert into Sec_Prof values('Secção 2',14235252)


insert into Unidade_Curricular values ('SI2','Sistemas de Informação II',6)
insert into Unidade_Curricular values ('DAW','Desenvolvimento de Aplicações Web',6)
insert into Unidade_Curricular values ('AVE','Ambientes Virtuais de Execução',6)

insert into Uc_Ano values (2020,'SI2', 185353333)
insert into Uc_Ano values (2020,'DAW', 14235252)
insert into Uc_Ano values (2019,'AVE', 123781391)
insert into Uc_Ano values (2020,'AVE', 123781391)

insert into Prof_UC_Ano values (2020,'SI2',185353333)
insert into Prof_UC_Ano values (2020,'DAW',12313141)
insert into Prof_UC_Ano values (2020,'DAW',14235252)
insert into Prof_UC_Ano values (2019,'AVE',123781391)
insert into Prof_UC_Ano values (2020,'AVE',123781391)

insert into Aluno values (43755,264239822,'Luis Maria de Figueiredo Cruz Guerra','Régua','1998-07-17')
insert into Aluno values (43593,212342325,'Wilson Rúben de Figueiredo Cação Martins Costa','Entroncamento','1997-04-19')
insert into Aluno values (44803,264239822,'Beatriz Gonçalves','Lisboa','1999-02-19')
insert into Aluno values (10000,234234242,'Aluno Passado','Portugal','1998-01-01')

insert into Matricula values (1,2020,43755,'LEIC')
insert into Matricula values (2,2020,43593,'LEIC')
insert into Matricula values (3,2020,44803,'LEIC')
insert into Matricula values (4,2019,43755,'LEIC')

insert into Matricula_Uc values (1,2020,'DAW',null,0)
insert into Matricula_Uc values (1,2020,'SI2',null,0)
insert into Matricula_Uc values (1,2020,'AVE',null,0)
insert into Matricula_Uc values (4,2019,'AVE',8,0)
insert into Matricula_Uc values (2,2020,'DAW',null,0)
insert into Matricula_Uc values (3,2020,'AVE',null,0)
insert into Matricula_Uc values (3,2020,'SI2',null,0)

insert into Lista_Anual_UC_Curso values ('LEIC', 6, 2020,'DAW')
insert into Lista_Anual_UC_Curso values ('LEIC',4, 2020, 'AVE')
insert into Lista_Anual_UC_Curso values ('LEIC',5, 2020, 'SI2')

insert into Certificado values (20,'2020-05-09',10000)