using System;

namespace TP2.Model
{
    public class UnidadeCurricular
    {
        public UnidadeCurricular() {
            _ID = new ID();
        }
        public int Creditos { get; set; }
        public String Descricao { get; set; }
        public String Regente { get; set; } // references CC Professor

        public ID _ID;

        public class ID
        {
            public String Sigla { get; set; }
            public DateTime Data { get; set; }  // date only
        }
    }
}
