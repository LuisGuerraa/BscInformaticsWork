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
    class MapperSemestre : AbstractMapper<Semestre, Semestre.ID, List<Semestre>>, IMapperSemestre
    {
        public MapperSemestre(IContext ctx) : base(ctx) { }
        protected override string SelectAllCommandText { get { return "SELECT * FROM Semestre"; } }

        protected override string SelectCommandText { get { return "SELECT * FROM Semestre WHERE id = @id"; } }


        protected override string UpdateCommandText { get { return "UPDATE Semestre SET curso = @curso WHERE id = @id"; } }


        protected override string DeleteCommandText { get { return "DELETE FROM Semestre WHERE id = @id"; } }


        protected override string InsertCommandText { get { return "INSERT INTO Semestre VALUES{ @curso, @id }"; } }


        public override Semestre Read(Semestre.ID id) {
            using TransactionScope ts = new TransactionScope(TransactionScopeOption.Required);
            Semestre entity = new Semestre();
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

        public Semestre Read(int id) {
            throw new NotImplementedException();
        }

        protected override void DeleteParameters(IDbCommand command, Semestre e) {
            SqlParameter id = new SqlParameter("@id", e._ID.Ident);
            command.Parameters.Add(id);
        }

        protected override void InsertParameters(IDbCommand command, Semestre e) {
            SqlParameter curso = new SqlParameter("@curso", e.Curso);
            SqlParameter id = new SqlParameter("@id", SqlDbType.Int);
            command.Parameters.Add(curso);
            command.Parameters.Add(id);
        }

        protected override Semestre Map(IDataRecord record) {
            Semestre s = new Semestre();
            s.Curso = record.GetString(0);
            s._ID.Ident = record.GetInt32(1);
            return s;
        }

        protected override void SelectParameters(IDbCommand command, Semestre.ID k) {
            SqlParameter p1 = new SqlParameter("@id", k.Ident);
            command.Parameters.Add(p1);
        }

        protected override Semestre UpdateEntityID(IDbCommand cmd, Semestre e) {
            var param = cmd.Parameters["@id"] as SqlParameter;
            e._ID.Ident = int.Parse(param.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Semestre e) {
            InsertParameters(command, e);
        }
    }
}
