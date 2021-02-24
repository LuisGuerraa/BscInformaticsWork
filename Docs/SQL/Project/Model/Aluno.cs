using System;

namespace TP2.Model
{
    public class Aluno
    {
        public Aluno() { }

        public int Numero { get; set; }
        public String CC { get; set; }
        public String Nome { get; set; }
        public String Morada { get; set; }
        public DateTime Data { get; set; } // date only
    }
}
