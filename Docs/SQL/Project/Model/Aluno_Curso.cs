using System;

namespace TP2.Model
{
    public class Aluno_Curso
    {
        public Aluno_Curso() { }
        public int Aluno { get; set; }  // references Numero Aluno
        public String Curso { get; set; }   // references Sigla Curso
        public DateTime Ano { get; set; }   // date only
    }
}
