SELECT * FROM issue;
SELECT * FROM issue_issuelabel;
SELECT * FROM issuecomment;
SELECT * FROM project;
SELECT * FROM project_issuelabel;
SELECT * FROM transition;

SELECT P.name, P.description, PS.StateType, PL.labeltype
            FROM Project P INNER JOIN Project_IssueState PS ON (P.name = PS.ProjectName)
            INNER JOIN Project_IssueLabel PL ON (P.name = PL.ProjectName) WHERE P.name = 'S1920V-LI61N-G03'
            
SELECT labeltype FROM Project_IssueLabel PL WHERE PL.ProjectName = 'S1920V-LI61N-G03'

SELECT statetype FROM Project_IssueState PS WHERE PS.ProjectName = 'S1920V-LI61N-G03'

SELECT * FROM Project P WHERE P.name = 'S1920V-LI61N-G03'

SELECT * FROM transition 
SELECT * FROM project p 

INSERT INTO transition(projectname, statetype) VALUES ('olaola','open')

UPDATE transition SET statetype = 'closed' WHERE projectname = 'olaola'
UPDATE project SET name = 'olaola' WHERE name = 'Grupo 3'

DELETE FROM issue WHERE projectname = 'Grupo 3'
DELETE FROM issue_issuelabel WHERE projectname = 'Grupo 3'

SELECT * FROM issue WHERE id = 200