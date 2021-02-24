using System;
using System.Collections.Generic;

namespace TP2
{
	class App
	{

		public enum OPTIONS
		{
			NONE,
			Inserir_Departamento, Remover_Departamento, Atualizar_Departamento,
			Inserir_Seccao, Remover_Seccao, Atualizar_Seccao,
			Inserir_UC, Remover_UC, Atualizar_UC,
			Criar_Curso,
			Inserir_UnidadeCurricular, Remover_UnidadeCurricular,
			Matricular_Aluno,
			Inscrever_Aluno,
			Atribuir_Nota,
			Listar_Matriculas_UC,
			Eliminar_Aluno,
			Exit
		};

		private delegate int DBMethod();
		private Dictionary<OPTIONS, DBMethod> funcs;
		private static App _instance;
		public static App Instance
		{
			get
			{
				if (_instance == null)
					_instance = new App();
				return _instance;
			}
			private set { }
		}

		private App() {
			funcs = new Dictionary<OPTIONS, DBMethod>();
			funcs.Add(OPTIONS.Inserir_Departamento, Methods.I1);
			funcs.Add(OPTIONS.Inserir_Seccao, Methods.I2);
			funcs.Add(OPTIONS.Inserir_UC, Methods.I3);
			funcs.Add(OPTIONS.Remover_Departamento, Methods.D1);
			funcs.Add(OPTIONS.Remover_Seccao, Methods.D2);
			funcs.Add(OPTIONS.Remover_UC, Methods.D3);
			funcs.Add(OPTIONS.Atualizar_Departamento, Methods.U1);
			funcs.Add(OPTIONS.Atualizar_Seccao, Methods.U2);
			funcs.Add(OPTIONS.Atualizar_UC, Methods.U3);
			funcs.Add(OPTIONS.Criar_Curso, Methods.CC);
			funcs.Add(OPTIONS.Inserir_UnidadeCurricular, Methods.IUC);
			funcs.Add(OPTIONS.Remover_UnidadeCurricular, Methods.RUC);
			funcs.Add(OPTIONS.Matricular_Aluno, Methods.ESC);
			funcs.Add(OPTIONS.Inscrever_Aluno, Methods.ESUS);
			funcs.Add(OPTIONS.Atribuir_Nota, Methods.SSG);
			funcs.Add(OPTIONS.Listar_Matriculas_UC, Methods.LMUC);
			funcs.Add(OPTIONS.Eliminar_Aluno, Methods.EA);
		}
		public OPTIONS DisplayMenu() {
			OPTIONS option = OPTIONS.NONE;
			try {
				Array arr = Enum.GetValues(typeof(OPTIONS));
				Console.WriteLine("Choose an option");
				for (int i = 1; i < arr.Length; i++) {
					Console.WriteLine("{0} {1}. ", i, arr.GetValue(i).ToString());
				}
				Console.WriteLine();
				Console.Write("-> ");
				var result = Console.ReadLine();
				option = (OPTIONS)Enum.Parse(typeof(OPTIONS), result);
			}
			catch (ArgumentException ex) {
				//nothing to do. User press select no option and press enter.
			}
			return option;
		}

		public void Run() {
			OPTIONS userInput = OPTIONS.NONE;
			do {
				Console.Clear();
				userInput = DisplayMenu();
				Console.Clear();
				try {
					funcs[userInput]();
					Console.WriteLine("Press any key to return.");
					Console.ReadKey();
				}
				catch (KeyNotFoundException ex) {
					//Nothing to do. The option was not a valid one. Read another.
				}

			} while (userInput != OPTIONS.Exit);
		}
	}



	class MainClass
	{
		public static void Main(String[] args) {
			App.Instance.Run();
		}
	}
}
