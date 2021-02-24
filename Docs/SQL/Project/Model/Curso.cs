using System;

namespace TP2.Model
{
    public class Curso
    {
        public Curso() { }
        public String Sigla { get; set; }
        public String Departamento { get; set; }    // references sigla departamento
        public String Descricao { get; set; }
    }
}
