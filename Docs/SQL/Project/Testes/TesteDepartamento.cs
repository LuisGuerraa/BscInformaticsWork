using System;
using TP2.Functions;
using TP2.Model;

namespace TP2.Testes
{
    class TesteDepartamento
    {
        public static void TesteCreateDepartamento(string sigla, string desc) {
            Departamento d = new Departamento();
            d = InsertFuncions.CreateDepartamento(sigla, desc);
            Console.WriteLine("Departamento " + d.Sigla + "\t" + d.Descricao + "created");
        }

        public static void TesteUpdateDepartamento(string sigla, string desc) {
            Departamento d = new Departamento();
            d.Sigla = sigla;
            d.Descricao = desc;
            d = UpdateFunctions.UpdateDepartamento(d);
            Console.WriteLine("Departamento " + d.Sigla + "\t" + "updated with descrpition " + d.Descricao);
        }

        public static void TesteDeleteDepartamento(string sigla) {
            Departamento d = new Departamento();
            d = DeleteFunctions.DeleteDepartamento(sigla);
            Console.WriteLine("Departamento " + d.Sigla + "\t" + d.Descricao + "deleted");
        }

        public static void Main4(String[] args) {
            // TesteCreateDepartamento("TESTE", "TESTE");
            // TesteDeleteDepartamento("TESTE");
            // TesteUpdateDepartamento("TESTE", "TESTE1");
        }
    }
}
