use SI2;

create table Departamento (
	sigla CHAR(6) primary Key NOT NULL,
	descrição VARCHAR(120)
)

create table Curso (
	sigla CHAR (6) primary key not null,
	descrição varchar (160),
	ects_final int,
	sigla_dept char (6) foreign key references Departamento (sigla)
)
create table Semestre(
	num int primary key check ( num between 1 and 6) 
)

create table Cur_Sem (
	siglaCur char (6) foreign key references Curso(sigla),
	numSem int foreign key references Semestre (num),
	primary key(siglaCur,numSem)
)

create table Professor (
	nome VARCHAR (120),
	cc CHAR (12) primary key not null,
	area_espec varchar(160),
	categoria varchar(30)
)
create table Secção (
	sigla CHAR(30) PRIMARY KEY NOT NULL,
	dpt CHAR(6) FOREIGN KEY REFERENCES Departamento(sigla),
	sect_desc VARCHAR(120),
	ccProfCoord char (12) foreign key references Professor (cc)
)

create table Sec_Prof (           -- professores de uma secção
	siglaSec char(30) foreign key references Secção(sigla),
	cc char(12) foreign key references Professor(cc),

)

create table Unidade_Curricular(
	sigla char (3) primary key not null,
	descrição varchar (120),
	ects int
)

create table Uc_Ano (
	ano integer not null check ( ano between 1852 and 9999),
	uc char(3)  foreign key references Unidade_Curricular (sigla),
	prof_regente char(12) foreign key references Professor(cc),
	primary key (ano, uc)
)


create table Prof_UC_Ano(
	ano integer,
	uc char(3) ,           
	prof_cc char (12) foreign key references Professor(cc),
	foreign key (ano,uc) references Uc_Ano (ano,uc),
	primary key( ano, uc, prof_cc)
	
	
)

create table Aluno (
	nº_aluno int primary key not null,
	cc char (12),
	nome varchar (120),
	morada varchar (160),
	dt_nascimento Date -- (2020-05-09) 

)

create table Matricula(
	id_matricula int not null,
	ano integer not null check ( ano between 1852 and 9999),
	nº_aluno int foreign key references Aluno(nº_aluno),
	curso char(6) foreign key references Curso(sigla),
	primary key(id_matricula, ano)

)

create table Matricula_Uc(
	id_matriculaUC int ,
	anoUC int ,
	foreign key (id_matriculaUC, anoUC) references Matricula (id_matricula,ano),
	uc char (3) foreign key references Unidade_Curricular(sigla),
	nota int check (nota between 0 and 20),
	aprovado BIT,  -- 1 or 0
	primary key (id_matriculaUC,anoUC,uc)
)

create table Lista_Anual_UC_Curso(
	siglaCur char (6),
	numSem int ,
	ano integer ,
	uc char(3) ,
	foreign key (ano, uc) references Uc_Ano (ano,uc),
	foreign key (siglaCur,numSem) references Cur_Sem(siglaCur,numSem),
	primary key (ano, uc, siglaCur,numSem)
)

create table Certificado(
   nota_final float ,
   data_conclusão Date,
   naluno int foreign key references Aluno(nº_aluno),
   primary key (naluno)
)


