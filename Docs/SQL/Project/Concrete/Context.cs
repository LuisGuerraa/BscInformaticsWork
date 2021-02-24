using System.Data;
using System.Data.SqlClient;
using System.Transactions;

namespace DAL.concrete
{
    class Context : IContext
    {
        private string connectionString;
        private SqlConnection con = null;

        public Context(string cs) {
            connectionString = cs;
        }

        public void Open() {
            if (con == null) {
                con = new SqlConnection(connectionString);

            }
            if (con.State != ConnectionState.Open)
                con.Open();
        }

        public SqlCommand createCommand() {
            Open();
            SqlCommand cmd = con.CreateCommand();
            return cmd;
        }
        public void Dispose() {
            if (con != null) {
                con.Dispose();
                con = null;
            }

        }
        public void EnlistTransaction() {
            if (con != null) {
                con.EnlistTransaction(Transaction.Current);
            }
        }
    }
}
