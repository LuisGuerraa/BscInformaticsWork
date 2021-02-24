using System;

namespace TP2.Model
{
    public class Seccao
    {
        public Seccao() { }
        public String Sigla { get; set; }
        public String Departamento { get; set; }    // references Sigla Departamento
        public String Coordenador { get; set; } // references CC Professor
        public String Descricao { get; set; }
    }
}
