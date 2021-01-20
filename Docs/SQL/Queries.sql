use SI2
--d)
    -- insert 
DROP PROCEDURE IF EXISTS insertDepartmento
GO

create procedure insertDepartmento  @sigla CHAR(6), @descrição VARCHAR(120)
as
begin
	INSERT INTO Departamento VALUES(@sigla , @descrição)
END 
GO

       -- update 
DROP PROCEDURE IF EXISTS updateDepartamento
GO

create procedure updateDepartamento  @oldSigla CHAR(6),  @newSigla CHAR(6), @newDesc VARCHAR(120)
as
begin
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(SELECT * FROM Departamento WHERE sigla = @oldSigla)
			BEGIN
				UPDATE Departamento SET
					sigla = @newSigla , descrição = @newDesc
				WHERE sigla = @oldSigla
				IF EXISTS(SELECT * FROM Curso WHERE sigla_dept = @oldSigla)
				BEGIN
					UPDATE Curso SET
						sigla_dept = @newSigla
					WHERE sigla_dept = @oldSigla
				END
				IF EXISTS(SELECT * FROM Secção WHERE dpt = @oldSigla)
				BEGIN
					UPDATE Secção SET
						dpt = @newSigla
					WHERE dpt = @oldSigla
				END
			END
			COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not update Departamento'
			rollback
		END CATCH
END
GO

       -- remove 
DROP PROCEDURE IF EXISTS deleteDepartamento
GO

create procedure deleteDepartamento @d_sigla CHAR(6) 
as
begin
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(Select * FROM Departamento WHERE sigla = @d_sigla)
			begin
				delete from Departamento WHERE sigla = @d_sigla
			end
		COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not remove Departamento because it does not exist'
			rollback
		END CATCH
END



--e)
GO
create procedure insertSecção  @sigla CHAR(30), @dpt CHAR(6), @sect_desc varchar (120), @ccProfCoord char (12)
as
begin
	INSERT INTO Secção VALUES(@sigla , @dpt,@sect_desc,@ccProfCoord)
END 
GO

       -- update 
DROP PROCEDURE IF EXISTS updateSecção
GO

create procedure updateSecção  @oldSigla CHAR(6),  @newSigla CHAR(6), @newdpt CHAR(6), @newSect_desc varchar (120), @newccProfCoord char (12)
as
begin
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(SELECT * FROM Secção WHERE sigla = @oldSigla)
			BEGIN
				UPDATE Secção SET
					sigla = @newSigla , 
					dpt = @newdpt,
					sect_desc = @newSect_desc,
					ccProfCoord = @newccProfCoord
				WHERE sigla = @oldSigla
			END             -- nao afeta nada para lá da tabela secção
			COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not update Secção'
			rollback
		END CATCH
END
GO

       -- remove 
DROP PROCEDURE IF EXISTS deleteSecção
GO

create procedure deleteSecção @s_sigla CHAR(6) 
as
begin
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(Select * FROM Secção WHERE sigla = @s_sigla)
			begin
				delete from Secção WHERE sigla = @s_sigla
			end
		COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not remove Secção because it does not exist'
			rollback
		END CATCH
END

--f)
GO
create procedure insertUc  @sigla CHAR(3), @descrição varchar (120), @ects int 
as
begin
	INSERT INTO Unidade_Curricular VALUES(@sigla , @descrição,@ects)
END 
GO

       -- update 
DROP PROCEDURE IF EXISTS updateUC
GO

create procedure updateUC  @oldSigla CHAR(6),  @newSigla CHAR(6), @newdesc varchar(120), @newects int
as
begin
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(SELECT * FROM Secção WHERE sigla = @oldSigla)
			BEGIN
				UPDATE Unidade_Curricular SET
					sigla = @newSigla , 
					descrição = @newdesc,
					ects = @newects
				WHERE sigla = @oldSigla
				
				IF EXISTS(SELECT * FROM Uc_Ano WHERE uc = @oldSigla)
				BEGIN
					UPDATE Uc_Ano SET
						uc = @newSigla
					WHERE uc = @oldSigla
				END
				IF EXISTS(SELECT * FROM Prof_UC_Ano WHERE uc = @oldSigla)
				BEGIN
					UPDATE Uc_Ano SET
						uc = @newSigla
					WHERE uc = @oldSigla
				END
				IF EXISTS(SELECT * FROM Lista_Anual_UC_Curso WHERE uc = @oldSigla)
				BEGIN
					UPDATE Uc_Ano SET
						uc = @newSigla
					WHERE uc = @oldSigla
				END
			END             

			COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not update Unidade Curricular'
			rollback
		END CATCH
END
GO

       -- remove 
DROP PROCEDURE IF EXISTS deleteSecção
GO

create procedure deleteSecção @s_sigla CHAR(6) 
as
begin
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	BEGIN TRAN
		BEGIN TRY
			IF EXISTS(Select * FROM Unidade_Curricular WHERE sigla = @s_sigla)
			begin
				delete from Unidade_Curricular WHERE sigla = @s_sigla
			end
		COMMIT
		END TRY
		BEGIN CATCH
			print 'Could not remove Unidade Curricular because it does not exist'
			rollback
		END CATCH
END



--g)
	insert into Curso values ( 'LEIM', 'Engenharia Informática e Multimédia',180,'ADEETC')
	insert into Cur_Sem values ('LEIM',1)
	insert into Cur_Sem values ('LEIM',2)
	insert into Cur_Sem values ('LEIM',3)
	insert into Cur_Sem values ('LEIM',4)
	insert into Cur_Sem values ('LEIM',5)
	insert into Cur_Sem values ('LEIM',6)

--h)
insert into Unidade_Curricular values ( 'MPD', 'Modelação e Padrões de Desenho',6)
insert into Professor values ('Luís Falcão',324242343, 'Programação Funcional', 'A')
insert into Uc_Ano values(2020,'MPD',324242343)
insert into Lista_Anual_UC_Curso values ('LEIC',3,2020,'MPD')

delete from Lista_Anual_UC_Curso where uc = 'MPD'
delete from Uc_Ano where uc = 'MPD'
delete from Unidade_Curricular where sigla = 'MPD'



--i)
	insert into Aluno values (44444, 234242442, 'Eusébio','Lisboa, Benfica', '1942-01-05')
	insert into Matricula values (10,2020,44444,'LEIC') -- id da matricula por ano


--j)
	insert into Matricula_Uc values (10,2020, 'MPD',null, 0)-- inscreve aluno com a inscrição id no mesmo ano na UC MPD
	insert into Matricula_Uc values (10,2020, 'DAW',null,0)
	
--k)
	update Matricula_Uc 
	set nota = 16
	where id_matriculaUc = 10 and uc ='MPD'  

--l)
	select ects_final from Curso where sigla = 'LEIC'

--m)
GO
	CREATE or alter TRIGGER emitirCertificado --Select from matricula UCs where aluno = .. if (soma ects = curso.ectsfinal)
	on Matricula_Uc
	after insert,update 
	as begin
	declare @id_matricula int
	declare @total_creditos_aluno int
	declare @creditos_necessarios int 
	declare @numero_aluno int
	declare @curso char(6)
 

	select @id_matricula = id_matriculaUC from inserted 
	select @numero_aluno = nº_aluno, @curso=curso from Matricula where id_matricula = @id_matricula
	select @creditos_necessarios = ects_final from Curso where sigla = @curso

	SELECT nota,ects into #TempTable
	from Matricula_Uc as MC
	inner join Unidade_Curricular as UC 
	on Mc.uc = UC.sigla 
	inner join Matricula as M 
	on MC.id_matriculaUC = M.id_matricula
	where M.nº_aluno = @numero_aluno and MC.aprovado = 1
	 
	Select @total_creditos_aluno =  sum (#TempTable.ects) from #TempTable

	IF @total_creditos_aluno >= @creditos_necessarios        -- media = notaUC * ectsDaUc + notaUc * ectsDaUc / ects_finais
	begin
	declare @ects int
	declare @nota int 
	declare @media float
	set @media = 0
	declare @cursor cursor 
	set @cursor = cursor for select * from #TempTable
		open @cursor
			fetch next from @cursor into @nota , @ects 
			while(@@FETCH_STATUS = 0)
			begin
				set @media += @nota * @ects
				fetch next from @cursor into @nota , @ects 
			end
		close @cursor
		deallocate @cursor
			set @media = @media / @total_creditos_aluno
			
		if exists ( select * from Certificado where naluno = @numero_aluno) 
		begin
			update Certificado
			set nota_final = @media
		end
		else
			begin
				insert into Certificado values (@media,GETDATE(),@numero_aluno) 
			end
		
	end
	
		delete from Matricula_Uc where id_matriculaUC = 12 and uc ='MPD'
		insert into Matricula_Uc values (12,2016, 'MPD',19, 1)
		Select * from Certificado
		
	
--n) -- select de UC que tao na tabela de UC e nao estao em Matricula_UC
	insert into Unidade_Curricular values ( 'POO', 'Programação Orientada por Objetos',6)
	insert into Uc_Ano values(2017,'POO',324242343)
	insert into Lista_Anual_UC_Curso values ('LEIC',2,2017,'POO')
	
GO
	create or alter function entre2anos (@ano1 int , @ano2 int)
	returns @Ucs table(sigla char (3), ano int)
	as begin
		declare @cursor cursor
		declare @ano int
		declare @uc char (3) 
		set @cursor = cursor for select uc,ano from Uc_Ano where ano between @ano1 and @ano2
		open @cursor
			fetch next from @cursor into @uc, @ano 
			while(@@FETCH_STATUS = 0)
			begin
				if not exists (select * from Matricula_Uc where uc=@uc and anoUC=@ano)
				begin
					insert into @Ucs values (@uc,@ano)
				end
				fetch next from @cursor into @uc , @ano 
			end
		close @cursor
		deallocate @cursor

	return
	end
GO

select * from entre2anos (2016,2019)

--o)
Select * from Lista_Anual_UC_Curso

	
GO	
	Create or alter view CreditosCurso as
	select siglaCur,uc, C.ects_final
	from Lista_Anual_UC_Curso  as L_A
	inner join Curso as C
	on C.sigla = L_A.siglaCur



GO
	Create or alter trigger CreditosCurso on
	CreditosCurso
	instead of update
	as begin
	declare @sigla char(3)
	declare @newCredits int 
	select @sigla = siglaCur, @newCredits = I.ects_final from inserted as I
	
	update Curso
	set ects_final = @newCredits
	where sigla = @sigla

	update CreditosCurso
	set ects_final = @newCredits
    where siglaCur = @sigla

	end
	

		
--p)
	select distinct ano , nº_aluno from Matricula as M    -- Matricula_Uc inner join Certificado inner Join aluno
	inner join Matricula_Uc as MC
	on MC.id_matriculaUC = M.id_matricula
	where M.ano <= YEAR(GETDATE()) - 3 
GO
	create or alter function alunosMatricula3anos ()
	returns @alunos table ( ano int, nº_aluno int)
	as begin
		declare @ano int
		declare @naluno int
		declare @cursor cursor
		set @cursor = cursor for select distinct ano, nº_aluno from Matricula as M    -- Matricula_Uc inner join Certificado inner Join aluno
		inner join Matricula_Uc as MC
		on MC.id_matriculaUC = M.id_matricula
		where M.ano <= YEAR(GETDATE()) - 3 
		open @cursor
			fetch next from @cursor into @ano, @naluno 
		while(@@FETCH_STATUS = 0)
			begin
				if not exists (select * from Certificado where naluno = @naluno)
				begin 
					insert into @alunos values (@ano, @naluno)
				end
				fetch next from @cursor into @ano , @naluno 
			end
		close @cursor
		deallocate @cursor
	return
	end
GO

select * from alunosMatricula3anos()