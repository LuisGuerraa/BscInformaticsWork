using System;
using System.Collections.Generic;
using TP2.Functions;
using TP2.Model;
using static TP2.Functions.Funcs;

namespace TP2
{
	class Methods
	{
		// Insert Departamento
		public static int I1() {
			Console.WriteLine("Create Departamento");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Departamento d = new Departamento();
			Departamento ans = new Departamento();
			ans = InsertFuncions.CreateDepartamento(sigla, desc);
			if (ans.Descricao == null)
				Console.WriteLine("An error has ocurred. Department wasn't created.");
			else
				Console.WriteLine("Department {0} was created.", ans.Sigla);
			return d.Sigla != null ? 0 : -1;
		}
		// Insert Seccao
		public static int I2() {
			Console.WriteLine("Create Seccao");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Departamento? = ");
			var dep = Console.ReadLine();
			Console.Write("Coordenador? = ");
			var c = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Seccao d = new Seccao
			{
				Sigla = sigla,
				Departamento = dep,
				Coordenador = c,
				Descricao = desc
			};
			Seccao ans = InsertFuncions.CreateSeccao(d);
			if (ans.Sigla == null) {
				Console.WriteLine("An error has ocurred. Seccao wasn't created.");
				return -1;
			}
			else {
				Console.WriteLine("Seccao {0} was created, with the following parameters : {1}, {2}, {3}.", ans.Sigla, ans.Departamento, ans.Coordenador, ans.Descricao);
				return 0;
			}
		}
		// Insert UC
		public static int I3() {
			Console.WriteLine("Create Unidade Curricular");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Ano letivo? = ");
			var data = Console.ReadLine();
			Console.Write("Creditos? = ");
			var creditos = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Console.Write("Regente? = ");
			var reg = Console.ReadLine();
			UnidadeCurricular d = new UnidadeCurricular();
			d._ID.Sigla = sigla;
			d._ID.Data = DateTime.Parse(data);
			d.Creditos = int.Parse(creditos);
			d.Descricao = desc;
			d.Regente = reg;
			UnidadeCurricular ans = InsertFuncions.CreateUnidadeCurricular(d);
			if (ans._ID.Sigla == null) {
				Console.WriteLine("An error has ocurred. Unidade Curricular wasn't created.");
				return -1;
			}
			else {
				Console.WriteLine("Unidade Curricular {0} was created, with the following parameters : {1}, {2}, {3}.", ans._ID.Sigla, ans._ID.Data, ans.Creditos, ans.Descricao, ans.Regente);
				return 0;
			}
		}

		// Update Departamento
		public static int U1() {
			Console.WriteLine("Update Departamento");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Departamento d = new Departamento();
			Departamento ans = new Departamento();
			d.Sigla = sigla;
			d.Descricao = desc;
			ans = UpdateFunctions.UpdateDepartamento(d);
			if (ans == null) {
				Console.WriteLine("An error has ocurred. Department wasn't updated.");
				return -1;
			}
			else {
				Console.WriteLine("Department {0} was updated with description \"{1}\".", ans.Sigla, ans.Descricao);
				return 0;
			}
		}
		// Update Seccao
		public static int U2() {
			Console.WriteLine("Update Seccao");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Departamento? = ");
			var dep = Console.ReadLine();
			Console.Write("Coordenador? = ");
			var coo = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Seccao s = new Seccao();
			Seccao ans = new Seccao();
			s.Sigla = sigla;
			s.Departamento = dep;
			s.Coordenador = coo;
			s.Descricao = desc;
			ans = UpdateFunctions.UpdateSeccao(s);
			if (ans == null) {
				Console.WriteLine("An error has ocurred. Seccao wasn't updated.");
				return -1;
			}
			else {
				Console.WriteLine("Seccao {0} updated.", ans.Sigla);
				return 0;
			}
		}
		// Update UC
		public static int U3() {
			Console.WriteLine("Update Unidade Curricular");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Data? = ");
			var data = Console.ReadLine();
			Console.Write("Creditos? = ");
			var cr = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			Console.Write("Regente? = ");
			var reg = Console.ReadLine();
			UnidadeCurricular ans;
			UnidadeCurricular uc = new UnidadeCurricular();
			uc._ID.Sigla = sigla;
			uc._ID.Data = DateTime.Parse(data);
			uc.Creditos = int.Parse(cr);
			uc.Descricao = desc;
			uc.Regente = reg;
			ans = UpdateFunctions.UpdateUnidadeCurricular(uc);
			if (ans == null) {
				Console.WriteLine("An error has ocurred. Unidade Curricular wasn't updated.");
				return -1;
			}
			else {
				Console.WriteLine("Unidade Curricular {0} was updated .", ans._ID.Sigla);
				return 0;
			}
		}
		// Delete Departamento
		public static int D1() {
			Console.WriteLine("Delete Departamento");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Departamento ans;
			ans = DeleteFunctions.DeleteDepartamento(sigla);
			if (ans == null) {
				Console.WriteLine("Departamento {0} was deleted.", sigla);

				return 0;
			}
			else {
				Console.WriteLine("An error has ocurred. Departamento wasn't deleted.");
				return -1;
			}
		}
		// Delete Seccao
		public static int D2() {
			Console.WriteLine("Delete Seccao");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Seccao s = new Seccao();
			s.Sigla = sigla;
			s = DeleteFunctions.DeleteSeccao(s);
			if (s == null) {
				Console.WriteLine("Seccao {0} was deleted.", sigla);

				return 0;
			}
			else {
				Console.WriteLine("An error has ocurred. Seccao wasn't deleted.");
				return -1;
			}
		}
		// Delete UC
		public static int D3() {
			Console.WriteLine("Delete Unidade Curricular");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Ano Letivo? = ");
			var data = Console.ReadLine();
			UnidadeCurricular uc = new UnidadeCurricular();
			UnidadeCurricular ans = new UnidadeCurricular();
			uc._ID.Sigla = sigla;
			uc._ID.Data = DateTime.Parse(data);
			ans = DeleteFunctions.DeleteUnidadeCurricular(uc);
			if (ans == null) {
				Console.WriteLine("Seccao {0} was deleted.", sigla);

				return 0;
			}
			else {
				Console.WriteLine("An error has ocurred. Unidade Curricular wasn't deleted.");
				return -1;
			}
		}

		public static int CC() {
			Console.WriteLine("Create Basic Course");
			Console.Write("Sigla? = ");
			var sigla = Console.ReadLine();
			Console.Write("Departamento? = ");
			var dep = Console.ReadLine();
			Console.Write("Descricao? = ");
			var desc = Console.ReadLine();
			int res = Procedures.CreateBasicCourse(sigla, dep, desc);
			if (res == -1)
				Console.WriteLine("An error has ocurred. Course wasn't created.");
			else
				Console.WriteLine("Course {0} was created. {1} rows affected", sigla, res);
			return res;
		}

		public static int IUC() {
			Console.WriteLine("Insert UC on course");
			Console.Write("Sigla UC? = ");
			var sigla = Console.ReadLine();
			Console.Write("Ano letivo? = ");
			var ano = Console.ReadLine();
			Console.Write("Sigla Curso? = ");
			var curso = Console.ReadLine();
			Console.Write("Semestre? = ");
			var sem = Console.ReadLine();
			int res = Procedures.InsertUcOnCourse(sigla, DateTime.Parse(ano), curso, int.Parse(sem));
			if (res == -1)
				Console.WriteLine("An error has ocurred. UC wasn't inserted.");
			else
				Console.WriteLine("UC {0} was inserted on {1} on the year {2}. {3} rows affected", sigla, curso, DateTime.Parse(ano), res);
			return res;
		}

		public static int RUC() {
			Console.WriteLine("Remove UC from course");
			Console.Write("Sigla UC? = ");
			var sigla = Console.ReadLine();
			Console.Write("Sigla Curso? = ");
			var curso = Console.ReadLine();
			int res = Procedures.RemoveUcFromCourse(sigla, curso);
			if (res == -1)
				Console.WriteLine("An error has ocurred. UC wasn't removed.");
			else
				Console.WriteLine("UC {0} was removed from {1} .{2} rows affected", sigla, curso, res);
			return res;
		}

		public static int ESC() {
			Console.WriteLine("Enroll student on course");
			Console.Write("Numero aluno? = ");
			var sigla = Console.ReadLine();
			Console.Write("Sigla Curso? = ");
			var curso = Console.ReadLine();
			Console.Write("Ano letivo? = ");
			var ano = Console.ReadLine();
			int res = Procedures.EnrollStudentOnCourse(int.Parse(sigla), curso, DateTime.Parse(ano));
			if (res == -1)
				Console.WriteLine("An error has ocurred. Student wasnt enrolled.");
			else
				Console.WriteLine("Student {0} was enrolled on course {1} on the year {2}. {3} rows affected", int.Parse(sigla), curso, DateTime.Parse(ano).ToString("yyyy-MM-dd"), res);
			return res;
		}

		public static int ESUS() {
			Console.WriteLine("Enroll student on UC");
			Console.Write("Numero aluno? = ");
			var num = Console.ReadLine();
			Console.Write("Sigla Curso? = ");
			var curso = Console.ReadLine();
			Console.Write("Ano letivo? = ");
			var ano = Console.ReadLine();
			Console.Write("Sigla UC? = ");
			var uc = Console.ReadLine();
			int res = Procedures.EnrollStudentOnUC(int.Parse(num), curso, DateTime.Parse(ano), uc);
			if (res == -1)
				Console.WriteLine("An error has ocurred. Student wasnt enrolled.");
			else
				Console.WriteLine("Student {0} was enrolled on UC {1} on the year {2}. {3} rows affected", int.Parse(num), uc, DateTime.Parse(ano).ToString("yyyy-MM-dd"), res);
			return res;
		}

		public static int SSG() {
			Console.WriteLine("Set student grade");
			Console.Write("Numero aluno? = ");
			var num = Console.ReadLine();
			Console.Write("Sigla UC? = ");
			var uc = Console.ReadLine();
			Console.Write("Ano letivo? = ");
			var ano = Console.ReadLine();
			Console.Write("Nota? = ");
			var nota = Console.ReadLine();
			Console.Write("Sigla curso? = ");
			var curso = Console.ReadLine();
			int res = Procedures.SetStudentGrade(int.Parse(num), uc, DateTime.Parse(ano), int.Parse(nota), curso);
			if (res == -1)
				Console.WriteLine("An error has ocurred. Student grade wasnt set.");
			else
				Console.WriteLine("Student {0} has finished {1} with a final grade of {2}. {3} rows affected", int.Parse(num), uc, int.Parse(nota), res);
			return res;
		}

		internal static int LMUC() {
			Console.WriteLine("List registrations(Not implemented)");
			Console.Write("Ano letivo? = ");
			var ano = Console.ReadLine();
			return Funcs.ShowRegistrationsOfUC(DateTime.Parse(ano));
		}

		internal static int EA() {
			Console.WriteLine("Remove Student");
			Console.Write("Numero aluno? = ");
			var num = Console.ReadLine();
			int res = Funcs.DeleteStudent(int.Parse(num));
			if (res == -1)
				Console.WriteLine("Student not found.");
			else
				Console.WriteLine("Student removed.");
			return res;
		}

	}
}
