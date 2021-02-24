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
    class MapperDepartamento : AbstractMapper<Departamento, string, List<Departamento>>, IMapperDepartamento
    {
        public MapperDepartamento(IContext ctx) : base(ctx) {
        }
        protected override string SelectAllCommandText
        {
            get
            {
                return "SELECT dpt_sigla, dpt_desc FROM Departamento";
            }
        }

        protected override string SelectCommandText
        {
            get
            {
                return String.Format("{0} WHERE dpt_sigla=@sigla", SelectAllCommandText);
            }
        }
        protected override string UpdateCommandText
        {
            get
            {
                return "UPDATE Departamento SET dpt_sigla = @sigla, dpt_desc = @desc WHERE dpt_sigla = @sigla; select @sigla=scope_identity()";
            }
        }
        protected override string DeleteCommandText
        {
            get
            {
                return "DELETE FROM Departamento WHERE dpt_sigla = @sigla; select @sigla=scope_identity()";
            }
        }
        protected override string InsertCommandText
        {
            get
            {
                return "INSERT INTO Departamento (dpt_sigla,dpt_desc) VALUES(@sigla,@desc); select @sigla=scope_identity()";
            }
        }
        public override Departamento Create(Departamento entity) {
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
        public override Departamento Delete(Departamento entity) {
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
        public override Departamento Read(string id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Departamento entity = new Departamento();
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
        public override List<Departamento> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Departamento> result = new List<Departamento>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Departamento c = Map(info);
                        result.Add(c);
                    }
                }
                ts.Complete();
                return result;
            }
        }
        public override Departamento Update(Departamento entity) {
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
                    ts.Complete();
                    return entity;
                }
            }
        }

        protected override void DeleteParameters(IDbCommand command, Departamento e) {
            SqlParameter sigla = new SqlParameter("@sigla", e.Sigla);
            command.Parameters.Add(sigla);
        }

        protected override void InsertParameters(IDbCommand command, Departamento e) {
            SqlParameter sigla = new SqlParameter("@sigla", e.Sigla);
            SqlParameter desc = new SqlParameter("@desc", e.Descricao);

            command.Parameters.Add(sigla);
            command.Parameters.Add(desc);
        }

        protected override Departamento Map(IDataRecord record) {
            Departamento c = new Departamento();
            c.Sigla = record.GetString(0);
            c.Descricao = record.GetString(1);
            return c;
        }

        protected override void SelectParameters(IDbCommand command, string k) {
            SqlParameter p1 = new SqlParameter("@sigla", k);
            command.Parameters.Add(p1);
        }

        protected override Departamento UpdateEntityID(IDbCommand cmd, Departamento e) {
            var param = cmd.Parameters["@sigla"] as SqlParameter;
            e.Sigla = param.Value.ToString();
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Departamento e) {
            InsertParameters(command, e);
        }
    }
}
