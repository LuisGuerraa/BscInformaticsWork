using System;
using System.Data.SqlClient;

namespace DAL
{
    interface IContext : IDisposable
    {
        void Open();
        SqlCommand createCommand();
        void EnlistTransaction();
    }
}
