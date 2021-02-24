using System;
using TP2.Functions;

namespace TP2.Testes
{
    class TesteProcedures
    {

        public static void TesteCreateBasicCourse(string sigla_curso, string departamento, string descricao_curso) {
            int res = Procedures.CreateBasicCourse(sigla_curso, departamento, descricao_curso);
            if (res == -1)
                Console.WriteLine("An error has ocurred. Course wasn't created.");
            else
                Console.WriteLine("Course {0} was created. {1} rows affected", sigla_curso, res);
        }

        public static void TesteInsertUcOnCourse(string uc, DateTime ano, string sigla_curso, int semestre) {
            int res = Procedures.InsertUcOnCourse(uc, ano, sigla_curso, semestre);
            if (res == -1)
                Console.WriteLine("An error has ocurred. UC wasn't inserted.");
            else
                Console.WriteLine("UC {0} was inserted on {1} on the year {2}. {3} rows affected", uc, sigla_curso, ano, res);
        }

        public static void TesteRemoveUcOnCourse(string uc, string sigla_curso) {
            int res = Procedures.RemoveUcFromCourse(uc, sigla_curso);
            if (res == -1)
                Console.WriteLine("An error has ocurred. UC wasn't removed.");
            else
                Console.WriteLine("UC {0} was removed from {1} .{2} rows affected", uc, sigla_curso, res);
        }

        public static void TesteEnrollStudent(int aluno, string sigla_curso, DateTime ano) {
            int res = Procedures.EnrollStudentOnCourse(aluno, sigla_curso, ano);
            if (res == -1)
                Console.WriteLine("An error has ocurred. Student wasnt enrolled.");
            else
                Console.WriteLine("Student {0} was enrolled on course {1} on the year {2}. {3} rows affected", aluno, sigla_curso, ano.ToString("yyyy-MM-dd"), res);
        }

        public static void TesteEnrollStudentOnUC(int aluno, string sigla_curso, DateTime ano, string uc) {
            int res = Procedures.EnrollStudentOnUC(aluno, sigla_curso, ano, uc);
            if (res == -1)
                Console.WriteLine("An error has ocurred. Student wasnt enrolled.");
            else
                Console.WriteLine("Student {0} was enrolled on UC {1} on the year {2}. {3} rows affected", aluno, uc, ano.ToString("yyyy-MM-dd"), res);
        }

        public static void TesteSetStudentGrade(int aluno, string uc_sigla, DateTime ano, int nota, string sigla_curso) {
            int res = Procedures.SetStudentGrade(aluno, uc_sigla, ano, nota, sigla_curso);
            if (res == -1)
                Console.WriteLine("An error has ocurred. Student grade wasnt set.");
            else
                Console.WriteLine("Student {0} has finished {1} with a final grade of {2}. {3} rows affected", aluno, uc_sigla, nota, res);
        }

        public static void Main5(String[] args) {
            // TesteCreateBasicCourse("LEDA", "NONE", "Licenciatura em Engenharia do Deixa Andar");     // sem departamento
            // TesteCreateBasicCourse("LEDA", "ADEETC", "Licenciatura em Engenharia do Deixa Andar");   // works
            // TesteEnrollStudent(9999, "LEIC", DateTime.Now.Date);
            // TesteEnrollStudentOnUC(6969, "LEIC", new DateTime(2020,12,12), "PG");   // DOEST WORK ------------------------------------##########################################################################
            // TesteSetStudentGrade(6969, "M2", new DateTime(2019,12,12), 14, "LEIC");
        }


    }
}
