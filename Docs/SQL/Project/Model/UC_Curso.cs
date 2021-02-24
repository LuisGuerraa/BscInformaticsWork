using System;

namespace TP2.Model
{
    public class UC_Curso
    {
        public UC_Curso() {
            _ID = new ID();
        }
        public DateTime Date { get; set; }  // date only, references Data UC
        public String Curso { get; set; }   // references Sigla Curso
        public int Semestre { get; set; }  // references ID Semestre

        public ID _ID;

        public class ID
        {
            public String UC { get; set; }  // references Sigla UC
        }
    }

}
