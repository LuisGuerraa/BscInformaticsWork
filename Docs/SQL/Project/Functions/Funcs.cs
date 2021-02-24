using DAL.concrete;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Transactions;
using TP2.Concrete;
using TP2.Model;

namespace TP2.Functions
{
    class Funcs
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["TL51N_6"].ConnectionString;

        public static int DeleteStudent(int student_number) {
            Aluno a = new Aluno();
            a.Numero = student_number;
            Aluno to_get = new Aluno();

            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {

                using (Context ctx = new Context(connectionString)) {
                    MapperAluno ma = new MapperAluno(ctx);
                    to_get = ma.Read(student_number);
                }
                if (to_get.Nome == null) {
                    Console.WriteLine("Student doesn't exist.");
                    return -1;
                }

                Console.WriteLine("Student {0}, {1}, will be removed from the database.", to_get.Numero, to_get.Nome);

                using (Context ctx = new Context(connectionString)) {
                    MapperAluno_Curso mac = new MapperAluno_Curso(ctx);
                    mac.Delete(new Aluno_Curso { Aluno = student_number });
                    ctx.Dispose();
                }
                using (Context ctx = new Context(connectionString)) {
                    MapperAluno_UC mauc = new MapperAluno_UC(ctx);
                    mauc.Delete(new Aluno_UC { Numero = student_number });
                    ctx.Dispose();
                }
                using (Context ctx = new Context(connectionString)) {
                    MapperAluno ma = new MapperAluno(ctx);
                    ma.Delete(to_get);
                    ctx.Dispose();
                }
                ts.Complete();
            }
            Console.WriteLine("Student {0}, {1}, was removed from the database.", to_get.Numero, to_get.Nome);
            return 0;
        }

        public static int ShowRegistrationsOfUC(DateTime ano) {
            List<Aux> ans = new List<Aux>();
            using (Context ctx = new Context(connectionString)) {
                // var _uc = new SqlParameter("@uc", sigla_uc);
                var _ano = new SqlParameter("@ano_letivo", ano);
                IDbCommand cmd = ctx.createCommand();
                // cmd.CommandText = "select aluno from Aluno_UC where uc = @uc and ano_letivo = @ano_letivo";
                cmd.CommandText = "select count(distinct aluno) as 'matriculas',uc, ano_letivo from Aluno_UC where  ano_letivo = '2020-12-12' group by uc, ano_letivo";
                cmd.CommandType = CommandType.Text;
                // cmd.Parameters.Add(_uc);
                cmd.Parameters.Add(_ano);
                // cmd.ExecuteNonQuery();
                IDataReader reader = cmd.ExecuteReader();
                while (reader.Read()) {
                    IDataRecord info = reader;
                    ans.Add(Map(info));
                }
                reader.Close();
            }
            if (ans.Count > 0) {
                printAux(ans);
                return 0;
            }
            else {
                Console.WriteLine("No data was found.");
                return -1;
            }
        }

        private static void printAux(List<Aux> print) {
            Console.WriteLine("{0,10}\t{1,18}\t{2,10}", "Matriculas", "|Unidade Curricular", "|Ano Letivo");
            foreach (Aux a in print) {
                Console.WriteLine("{0,-10}\t|{1,-18}\t|{2,-20}", a.matriculas, a.uc, a.ano_letivo.ToString("yyyy-MMM-dd"));
            }
        }

        public static Aux Map(IDataRecord i) {
            Aux x = new Aux();
            x.matriculas = i.GetInt32(0);
            x.uc = i.GetString(1);
            x.ano_letivo = i.GetDateTime(2);
            return x;
        }
        public class Aux
        {
            public int matriculas;
            public string uc;
            public DateTime ano_letivo;
        }
    }


}