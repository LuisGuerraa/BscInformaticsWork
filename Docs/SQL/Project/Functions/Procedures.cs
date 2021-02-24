using DAL.concrete;
using System;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;

namespace TP2.Functions
{
    class Procedures
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["TL51N_6"].ConnectionString;

        public static int CreateBasicCourse(string sigla_curso, string departamento, string descricao_curso) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var sigla = new SqlParameter("@sigla_curso", sigla_curso);
                var dpt = new SqlParameter("@dpt", departamento);
                var desc = new SqlParameter("@curso_desc", descricao_curso);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "createCourseBasic";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(sigla);
                cmd.Parameters.Add(dpt);
                cmd.Parameters.Add(desc);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }

        public static int InsertUcOnCourse(string uc, DateTime ano, string sigla_curso, int semestre) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var _uc = new SqlParameter("@uc", uc);
                var _ano = new SqlParameter("@ano", ano);
                var _sigla_curso = new SqlParameter("@sigla_curso", sigla_curso);
                var _semestre = new SqlParameter("@semestre", semestre);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "insertUC_on_sem";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(_uc);
                cmd.Parameters.Add(_ano);
                cmd.Parameters.Add(_sigla_curso);
                cmd.Parameters.Add(_semestre);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }

        public static int RemoveUcFromCourse(string uc, string sigla_curso) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var _uc = new SqlParameter("@uc", uc);
                var _sigla_curso = new SqlParameter("@sigla_curso", sigla_curso);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "removeUC_from_sem";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(_uc);
                cmd.Parameters.Add(_sigla_curso);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }

        public static int EnrollStudentOnCourse(int aluno, string sigla_curso, DateTime ano) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var _aluno = new SqlParameter("@aluno", aluno);
                var _sigla_curso = new SqlParameter("@sigla_curso", sigla_curso);
                var _ano = new SqlParameter("@ano", ano);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "matricularAluno";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(_aluno);
                cmd.Parameters.Add(_sigla_curso);
                cmd.Parameters.Add(_ano);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }

        public static int EnrollStudentOnUC(int aluno, string sigla_curso, DateTime ano, string uc) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var _aluno = new SqlParameter("@aluno", aluno);
                var _sigla_curso = new SqlParameter("@sigla_curso", sigla_curso);
                var _ano = new SqlParameter("@ano", ano);
                var _uc = new SqlParameter("@uc_sigla", uc);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "inscreverAluno_UC";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(_aluno);
                cmd.Parameters.Add(_sigla_curso);
                cmd.Parameters.Add(_ano);
                cmd.Parameters.Add(_uc);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }

        public static int SetStudentGrade(int aluno, string uc_sigla, DateTime ano, int nota, string sigla_curso) {
            int exit_code;
            using (Context ctx = new Context(connectionString)) {
                var _aluno = new SqlParameter("@aluno", aluno);
                var _uc_sigla = new SqlParameter("@uc_sigla", uc_sigla);
                var _ano = new SqlParameter("@ano", ano);
                var _nota = new SqlParameter("@nota", nota);
                var _sigla_curso = new SqlParameter("@sigla_curso", sigla_curso);
                IDbCommand cmd = ctx.createCommand();
                cmd.CommandText = "atribuirNota";
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Add(_aluno);
                cmd.Parameters.Add(_uc_sigla);
                cmd.Parameters.Add(_ano);
                cmd.Parameters.Add(_nota);
                cmd.Parameters.Add(_sigla_curso);
                exit_code = cmd.ExecuteNonQuery();

            }
            return exit_code;
        }
    }
}
