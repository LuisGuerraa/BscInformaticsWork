using System;

namespace TP2.Model
{
    public class UC_Prof
    {
        public UC_Prof() {
            _ID = new ID();
        }
        public String Professor { get; set; }   // references CC Professor

        public ID _ID;
        public class ID
        {
            public String UC { get; set; }  // references Sigla UC
            public DateTime Date { get; set; }  // date only, references Data UC
        }
    }
}
