using System;

namespace TP2.Model
{
    public class Certificado
    {
        public Certificado() { }
        public int Nota_Final { get; set; }
        public DateTime Data_Conclusao { get; set; }    // date only
        public int Aluno { get; set; }  // references Numero Aluno
    }
}
