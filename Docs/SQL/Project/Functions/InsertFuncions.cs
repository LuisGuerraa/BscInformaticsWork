using DAL.concrete;
using System.Configuration;
using TP2.Concrete;
using TP2.Model;

namespace TP2.Functions
{
    class InsertFuncions
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["TL51N_6"].ConnectionString;

        public static Departamento CreateDepartamento(string sigla, string descricao) {
            Departamento dp = new Departamento();
            dp.Sigla = sigla;
            dp.Descricao = descricao;
            using (Context ctx = new Context(connectionString)) {
                MapperDepartamento md = new MapperDepartamento(ctx);
                dp = md.Create(dp);
            }
            return dp;
        }

        public static Seccao CreateSeccao(Seccao s) {
            using (Context ctx = new Context(connectionString)) {
                MapperSeccao md = new MapperSeccao(ctx);
                s = md.Create(s);
            }
            return s;
        }
        public static UnidadeCurricular CreateUnidadeCurricular(UnidadeCurricular uc) {
            using (Context ctx = new Context(connectionString)) {
                MapperUnidadeCurricular md = new MapperUnidadeCurricular(ctx);
                uc = md.Create(uc);
            }
            return uc;
        }
    }
}