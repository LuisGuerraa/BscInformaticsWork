using System;
using TP2.Functions;
using TP2.Model;

namespace TP2.Testes
{
    class TesteSeccao
    {
        public static void CreateSeccao(string sigla, string departamento, string coordenador, string descricao) {
            Seccao s = new Seccao();
            s.Sigla = sigla;
            s.Departamento = departamento;
            s.Coordenador = coordenador;
            s.Descricao = descricao;
            s = InsertFuncions.CreateSeccao(s);
            Console.WriteLine("Seccao " + s.Sigla + "\t" + " created");
        }

        public static void UpdateSeccao(string sigla, string departamento, string coordenador, string desc) {
            Seccao s = new Seccao();
            s.Sigla = sigla;
            s.Departamento = departamento;
            s.Coordenador = coordenador;
            s.Descricao = desc;
            s = UpdateFunctions.UpdateSeccao(s);
            Console.WriteLine("Seccao " + s.Sigla + "\t" + " updated");
        }

        public static void DeleteSeccao(string sigla) {
            Seccao s = new Seccao();
            s.Sigla = sigla;
            s = DeleteFunctions.DeleteSeccao(s);
            Console.WriteLine("Seccao " + s.Sigla + "\t" + "deleted");
        }

        public static void Main2(String[] args) {
            // CreateSeccao("TESTE", "TESTE", "9876","TESTE");
            // UpdateSeccao("TESTE", "TESTE", "9876", "TESTE1");
            // DeleteSeccao("TESTE");
        }
    }
}
