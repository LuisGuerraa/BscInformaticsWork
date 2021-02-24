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
    class MapperSeccao : AbstractMapper<Seccao, string, List<Seccao>>, IMapperSeccao
    {
        public MapperSeccao(IContext ctx) : base(ctx) { }
        protected override string SelectAllCommandText
        {
            get
            {
                return "SELECT sect_sigla, dpt, coordenador, sect_desc FROM Seccao";
            }
        }

        protected override string SelectCommandText
        {
            get
            {
                return String.Format("{0} WHERE sect_sigla=@sigla", SelectAllCommandText);
            }
        }
        protected override string UpdateCommandText
        {
            get
            {
                return "UPDATE Seccao SET dpt = @dpt, coordenador = @coord, sect_desc = @desc WHERE sect_sigla = @sigla; select @sigla=scope_identity()";
            }
        }
        protected override string DeleteCommandText
        {
            get
            {
                return "DELETE FROM Seccao WHERE sect_sigla = @sigla; select @sigla=scope_identity()";
            }
        }
        protected override string InsertCommandText
        {
            get
            {
                return "INSERT INTO Seccao VALUES(@sigla,@dpt,@coord,@desc); select @sigla=scope_identity()";
            }
        }

        public override Seccao Create(Seccao entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = InsertCommandText;
                    cmd.CommandType = InsertCommandType;
                    InsertParameters(cmd, entity);
                    cmd.ExecuteNonQuery();
                }
                ts.Complete();
                return entity;
            }
        }
        public override Seccao Delete(Seccao entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = DeleteCommandText;
                    cmd.CommandType = DeleteCommandType;
                    DeleteParameters(cmd, entity);
                    int res = cmd.ExecuteNonQuery();
                    if (res > 0) {
                        entity = null;    // signal query was successfully executed
                    }
                    ts.Complete();
                    return entity;
                }
            }
        }
        public override Seccao Read(string id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Seccao entity = new Seccao();
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
        }
        public override List<Seccao> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Seccao> result = new List<Seccao>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Seccao c = Map(info);
                        result.Add(c);
                    }
                }
                ts.Complete();
                return result;
            }
        }
        public override Seccao Update(Seccao entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = UpdateCommandText;
                    cmd.CommandType = UpdateCommandType;
                    UpdateParameters(cmd, entity);
                    int res = cmd.ExecuteNonQuery();
                    if (res == -1 || res == 0) {
                        entity = null;    // signal query wasnt successfully executed
                    }
                }
                ts.Complete();
                return entity;
            }
        }
        protected override void DeleteParameters(IDbCommand command, Seccao e) {
            SqlParameter sigla = new SqlParameter("@sigla", e.Sigla);
            command.Parameters.Add(sigla);
        }

        protected override void InsertParameters(IDbCommand command, Seccao e) {
            SqlParameter sigla = new SqlParameter("@sigla", e.Sigla);
            SqlParameter dpt = new SqlParameter("@dpt", e.Departamento);
            SqlParameter coord = new SqlParameter("@coord", e.Coordenador);
            SqlParameter desc = new SqlParameter("@desc", e.Descricao);

            command.Parameters.Add(sigla);
            command.Parameters.Add(dpt);
            command.Parameters.Add(coord);
            command.Parameters.Add(desc);
        }

        protected override Seccao Map(IDataRecord record) {
            Seccao c = new Seccao();
            c.Sigla = record.GetString(0);
            c.Departamento = record.GetString(1);
            c.Coordenador = record.GetString(2);
            c.Descricao = record.GetString(3);
            return c;
        }

        protected override void SelectParameters(IDbCommand command, string k) {
            SqlParameter p1 = new SqlParameter("@sigla", k);
            command.Parameters.Add(p1);
        }

        protected override Seccao UpdateEntityID(IDbCommand cmd, Seccao e) {
            if (cmd.CommandText == DeleteCommandText) {
                e.Sigla = null;
                return e;
            }
            else /*(cmd.CommandText == UpdateCommandText)*/ {
                var d = cmd.Parameters["@dpt"] as SqlParameter;
                var c = cmd.Parameters["@coord"] as SqlParameter;
                var ds = cmd.Parameters["@desc"] as SqlParameter;
                e.Departamento = d.Value.ToString();
                e.Coordenador = c.Value.ToString();
                e.Descricao = ds.Value.ToString();
                return e;
            }
        }

        protected override void UpdateParameters(IDbCommand command, Seccao e) {
            InsertParameters(command, e);
        }
    }
}
