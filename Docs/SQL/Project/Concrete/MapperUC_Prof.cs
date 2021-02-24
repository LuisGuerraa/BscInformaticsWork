using DAL;
using DAL.mapper;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Transactions;
using TP2.Mappers.interfaces;
using TP2.Model;

namespace TP2.Concrete
{
    class MapperUC_Prof : AbstractMapper<UC_Prof, UC_Prof.ID, List<UC_Prof>>, IMapperUC_Prof
    {

        public MapperUC_Prof(IContext ctx) : base(ctx) { }
        protected override string SelectAllCommandText { get { return "SELECT * FROM UC_Prof"; } }
        protected override string SelectCommandText { get { return "SELECT * FROM UC_Prof WHERE uc=@uc AND ano=@ano"; } }

        protected override string UpdateCommandText { get { return "UPDATE UC_Prof SET prof=@prof WHERE uc=@uc AND ano=@ano"; } }

        protected override string DeleteCommandText { get { return "DELETE FROM UC_Prof WHERE uc=@uc AND ano=@ano"; } }

        protected override string InsertCommandText { get { return "INSERT INTO UC_Prof VALUES(@uc, @ano, @prof)"; } }

        public override UC_Prof Read(UC_Prof.ID id) {
            using TransactionScope ts = new TransactionScope(TransactionScopeOption.Required);
            UC_Prof entity = new UC_Prof();
            EnsureContext();
            context.EnlistTransaction();
            using (IDbCommand cmd = context.createCommand()) {
                cmd.CommandText = SelectCommandText;
                cmd.CommandType = SelectCommandType;
                SelectParameters(cmd, id);
                IDataReader reader = cmd.ExecuteReader();
                while (reader.Read()) {
                    IDataRecord info = reader;
                    entity = Map(info);
                }
                reader.Close();
            }
            ts.Complete();
            return entity;
        }

        public UC_Prof Read(string id) {
            throw new NotImplementedException();
        }

        protected override void DeleteParameters(IDbCommand command, UC_Prof e) {
            SqlParameter uc = new SqlParameter("@uc", SqlDbType.Char);
            SqlParameter ano = new SqlParameter("@ano", SqlDbType.DateTime);
            command.Parameters.Add(uc);
            command.Parameters.Add(ano);
        }

        protected override void InsertParameters(IDbCommand command, UC_Prof e) {
            SqlParameter uc = new SqlParameter("@uc", e._ID.UC);
            SqlParameter ano = new SqlParameter("@ano", e._ID.Date);
            SqlParameter prof = new SqlParameter("@prof", e.Professor);
            command.Parameters.Add(uc);
            command.Parameters.Add(ano);
            command.Parameters.Add(prof);
        }

        protected override UC_Prof Map(IDataRecord record) {
            UC_Prof uc = new UC_Prof();
            uc._ID.UC = record.GetString(0);
            uc._ID.Date = record.GetDateTime(1);
            uc.Professor = record.GetString(2);
            return uc;
        }

        protected override void SelectParameters(IDbCommand command, UC_Prof.ID k) {
            SqlParameter p1 = new SqlParameter("@uc", k.UC);
            SqlParameter p2 = new SqlParameter("@ano", k.Date);
            command.Parameters.Add(p1);
            command.Parameters.Add(p2);
        }

        protected override UC_Prof UpdateEntityID(IDbCommand cmd, UC_Prof e) {
            var param1 = cmd.Parameters["@uc"] as SqlParameter;
            var param2 = cmd.Parameters["@ano"] as SqlParameter;
            e._ID.UC = param1.Value.ToString();
            e._ID.Date = DateTime.Parse(param2.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, UC_Prof e) {
            InsertParameters(command, e);
        }
    }
}
