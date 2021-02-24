using System;
using TP2.Functions;
using TP2.Model;

namespace TP2.Testes
{
    class TesteUnidadeCurricular
    {
        public static void TesteInsertUC(string sigla, DateTime data, int creditos, string descricao, string regente) {
            UnidadeCurricular uc = new UnidadeCurricular();
            uc._ID.Sigla = sigla;
            uc._ID.Data = data;
            uc.Creditos = creditos;
            uc.Descricao = descricao;
            uc.Regente = regente;
            uc = InsertFuncions.CreateUnidadeCurricular(uc);
            Console.WriteLine("Unidade Curricular " + uc._ID.Sigla + "\t" + " created");
        }

        public static void TesteUpdateUC(string sigla, DateTime data, int creditos, string descricao, string regente) {
            UnidadeCurricular uc = new UnidadeCurricular();
            uc._ID.Sigla = sigla;
            uc._ID.Data = data;
            uc.Creditos = creditos;
            uc.Descricao = descricao;
            uc.Regente = regente;
            uc = UpdateFunctions.UpdateUnidadeCurricular(uc);
            Console.WriteLine("Unidade Curricular " + uc._ID.Sigla + "\t" + " updated");
        }

        public static void TesteDeleteUC(string sigla, DateTime date) {
            UnidadeCurricular s = new UnidadeCurricular();
            s._ID.Sigla = sigla;
            s._ID.Data = date;
            s = DeleteFunctions.DeleteUnidadeCurricular(s);
            Console.WriteLine("Unidade Curricular " + s._ID.Sigla + "\t" + "deleted");

        }

        public static void Main4(String[] args) {
            // DateTime.Now.ToString("yyyy-MM-dd")
            // TesteInsertUC("TESTE", DateTime.Now.Date, 6, "TESTE", "6672");
            // TesteUpdateUC("TESTE", DateTime.Now.Date, 5, "TESTE1", "6672");
            // TesteDeleteUC("TESTE", DateTime.Now.Date);
        }
    }
}
