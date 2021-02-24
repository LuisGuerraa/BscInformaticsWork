using System;

namespace TP2.Model
{
    public class Aluno_UC
    {
        public Aluno_UC() { }
        public int Numero { get; set; } // references Numero Aluno
        public String Curso { get; set; }   // references Sigla Curso
        public String UC { get; set; }  // references Sigla UnidadeCurricular
        public DateTime Ano_Letivo { get; set; }    // date only
        public DateTime Ano_Conclusao { get; set; }    // date only
        public byte Nota { get; set; }
    }
}
