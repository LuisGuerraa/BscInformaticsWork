using DAL.concrete;
using System.Configuration;
using TP2.Concrete;
using TP2.Model;

namespace TP2.Functions
{
    class DeleteFunctions
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["TL51N_6"].ConnectionString;

        public static Departamento DeleteDepartamento(string sigla) {
            Departamento dp = new Departamento();
            dp.Sigla = sigla;
            using (Context ctx = new Context(connectionString)) {
                MapperDepartamento md = new MapperDepartamento(ctx);
                dp = md.Delete(dp);
            }
            return dp;
        }

        public static Seccao DeleteSeccao(Seccao s) {
            using (Context ctx = new Context(connectionString)) {
                MapperSeccao md = new MapperSeccao(ctx);
                s = md.Delete(s);
            }
            return s;
        }

        public static UnidadeCurricular DeleteUnidadeCurricular(UnidadeCurricular uc) {
            using (Context ctx = new Context(connectionString)) {
                MapperUnidadeCurricular md = new MapperUnidadeCurricular(ctx);
                uc = md.Delete(uc);
            }
            return uc;
        }
    }
}
