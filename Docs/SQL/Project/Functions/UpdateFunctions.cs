using DAL.concrete;
using System.Configuration;
using TP2.Concrete;
using TP2.Model;

namespace TP2.Functions
{
    class UpdateFunctions
    {
        private static string connectionString = ConfigurationManager.ConnectionStrings["TL51N_6"].ConnectionString;

        public static Departamento UpdateDepartamento(Departamento dp) {
            using (Context ctx = new Context(connectionString)) {
                MapperDepartamento md = new MapperDepartamento(ctx);
                dp = md.Update(dp);
            }
            return dp;
        }

        public static Seccao UpdateSeccao(Seccao dp) {
            using (Context ctx = new Context(connectionString)) {
                MapperSeccao md = new MapperSeccao(ctx);
                dp = md.Update(dp);
            }
            return dp;
        }

        public static UnidadeCurricular UpdateUnidadeCurricular(UnidadeCurricular dp) {
            using (Context ctx = new Context(connectionString)) {
                MapperUnidadeCurricular md = new MapperUnidadeCurricular(ctx);
                dp = md.Update(dp);
            }
            return dp;
        }
    }
}
