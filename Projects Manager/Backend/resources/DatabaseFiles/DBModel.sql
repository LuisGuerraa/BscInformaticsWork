CREATE TYPE IssueState AS ENUM ('open', 'closed', 'archived');
CREATE TYPE IssueLabel AS ENUM ('defect','new_functionality','exploration');

CREATE TABLE Project (
	name varchar(255),
	description varchar(255),
	PRIMARY KEY(name)
);

-- N:N association with Project and IssueState
CREATE TABLE Transition (
	ProjectName varchar(255),
	stateType IssueState,
	nextState IssueState,
	PRIMARY KEY(ProjectName, stateType),
	FOREIGN KEY (ProjectName) REFERENCES Project (Name) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Project_IssueLabel (
	ProjectName varchar(255),
	LabelType IssueLabel,
	PRIMARY KEY(ProjectName, LabelType),
	FOREIGN KEY (ProjectName) REFERENCES Project (Name) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Issue (
	ID serial,
	Name varchar(255) NOT NULL,
	ProjectName varchar(255),
	Description varchar(255),
	CreatedBy varchar(50),
	CreationDate varchar(20) NOT NULL,
	CloseDate varchar(20),
	StateType IssueState,
	FOREIGN KEY (ProjectName) REFERENCES Project (Name) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY (ID, ProjectName, StateType)
); 

CREATE TABLE IssueComment (
	ID serial,
	IssueID serial,
	ProjectName varchar(255),
	StateType IssueState,
	Text varchar(255),
	CreationDate varchar(20),
	CreatedBy varchar(50),
	FOREIGN KEY (IssueID, ProjectName, StateType) REFERENCES Issue (ID, ProjectName, StateType) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY (ID, IssueID, ProjectName, StateType)
);

CREATE TABLE Issue_IssueLabel (
	IssueID serial,
	StateType IssueState,
	LabelType IssueLabel,
	ProjectName varchar(255),
	FOREIGN KEY (IssueID, ProjectName, StateType) REFERENCES Issue (ID, ProjectName, StateType) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY (ProjectName, IssueID, StateType, LabelType)
);

CREATE TABLE APIUser (
		ID serial PRIMARY KEY,
		username varchar(20) UNIQUE,
		password varchar(255)
);

CREATE TABLE Contributor (
		projectName varchar(255),
		userid integer,
		FOREIGN KEY (projectName) REFERENCES Project(name) ON UPDATE cascade,
		foreign key (userid) references APIUser(ID) on update cascade,
		primary key (projectName, userid)
);