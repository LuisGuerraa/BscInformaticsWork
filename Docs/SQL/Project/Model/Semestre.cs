using System;

namespace TP2.Model
{
    public class Semestre
    {
        public Semestre() {
            _ID = new ID();
        }
        public String Curso { get; set; }   // references Sigla Curso

        public ID _ID;
        public class ID
        {
            private int aux;
            public int Ident
            {
                get { return aux; }

                set
                {
                    if (value > 6)
                        aux = 6;    // max number of semesters is 6
                    else aux = value;
                }
            }
        }
    }
}
