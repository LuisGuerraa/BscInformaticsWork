

create table users(
	email varchar(50) primary key,
	username varchar(20) not null
	);

create table programme(
	acr varchar(6) primary key,
	p_name varchar(50) not null,
	numSem int not null
);

create table student(
	num int primary key,
	email varchar(50) unique not null references users(email) on update cascade,
	p_acr varchar(6) references programme(acr) on update cascade
);

create table teacher(
	num int primary key,
	email varchar(50) unique not null references users(email) on update cascade
);


create table course(
	acr varchar(3) primary key,
	c_name varchar(50) not null,
	numCoord int unique not null references teacher(num) on update cascade
);

create table semester(
	yearSem int,
	season char,
	primary key (yearSem, season)
);

create table mandatory_course(
	acr varchar(3) references course(acr),
	acrProgramme varchar(6) references programme(acr),
	num_sem int,
	primary key(acr,acrProgramme,num_sem)
);

create table optional_course(
	acr varchar(3) references course(acr),
	acrProgramme varchar(6) references programme(acr),
	num_sem int,
	primary key(acr,acrProgramme,num_sem)
);

create table class(
	id varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3) references course(acr),
	foreign key (yearSem, season) references semester(yearSem, season),
	primary key (id,yearSem,season,acrCourse)
);

create table teacher_class(
	id varchar(3),
	yearSem int,
	season char,
	numTeacher int references teacher(num) on update cascade,
	acrCourse varchar(3),
	foreign key (id, yearSem, season, acrCourse) references class(id, yearSem, season, acrCourse),
	primary key (id, yearSem, season, numTeacher, acrCourse)
);

create table groups(
	number int,
	classID varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3),
	foreign key(classID, yearSem, season, acrCourse) references Class(id, yearSem, season, acrCourse),
	primary key(number, classID, yearSem, season, acrCourse)
);

create table student_class(
	numStudent int references student(num) on update cascade,
	idClass varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3),
	foreign key (idClass,yearSem,season,acrCourse) references class(id, yearSem, season, acrCourse),
	primary key (numStudent, idClass, yearSem, season, acrCourse)
);

create table group_members(
	number int,
	studentNumber int references student(num) on update cascade,
	classID varchar(3),
	yearSem int,
	season char,
	acrCourse varchar(3),
	foreign key (number, classID, yearSem, season, acrCourse) references groups(number, classID, yearSem, season, acrCourse),
	primary key (studentNumber, classID, yearSem, season, acrCourse)
);