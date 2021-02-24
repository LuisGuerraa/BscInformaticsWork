using DAL;
using DAL.mapper;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Transactions;
using TP2.Mappers;
using TP2.Model;

namespace TP2.Concrete
{
    class MapperCurso : AbstractMapper<Curso, string, List<Curso>>, IMapperCurso
    {
        public MapperCurso(IContext ctx) : base(ctx) {
        }
        protected override string SelectAllCommandText
        {
            get
            {
                return "SELECT curso_sigla, dpt, curso_desc FROM Curso";
            }
        }

        protected override string SelectCommandText
        {
            get
            {
                return String.Format("{0} WHERE curso_sigla=@id", SelectAllCommandText);
            }
        }
        protected override string UpdateCommandText
        {
            get
            {
                return "UPDATE Curso SET curso_sigla = @sigla, dpt = @dpt, curso_desc = @desc WHERE curso_sigla = @sigla; select @id=scope_identity()";
            }
        }
        protected override string DeleteCommandText
        {
            get
            {
                return "DELETE FROM Curso WHERE curso_sigla = @id; select @id=scope_identity()";
            }
        }
        protected override string InsertCommandText
        {
            get
            {
                return "INSERT INTO Curso (curso_sigla,dpt,curso_desc) VALUES(@sigla,@dpt,@desc); select @id=scope_identity()";
            }
        }

        public override Curso Create(Curso entity) {
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
        public override Curso Delete(Curso entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = DeleteCommandText;
                    cmd.CommandType = DeleteCommandType;
                    DeleteParameters(cmd, entity);
                    cmd.ExecuteNonQuery();
                    //entity = UpdateEntityID(cmd, entity);
                }
                ts.Complete();
                return entity;
            }
        }
        public override Curso Read(string id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Curso entity = new Curso();
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
        public override List<Curso> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Curso> result = new List<Curso>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Curso c = Map(info);
                        result.Add(c);
                    }
                }
                ts.Complete();
                return result;
            }
        }
        public override Curso Update(Curso entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = UpdateCommandText;
                    cmd.CommandType = UpdateCommandType;
                    UpdateParameters(cmd, entity);
                    cmd.ExecuteNonQuery();
                    //entity = UpdateEntityID(cmd, entity);
                }
                ts.Complete();
                return entity;
            }
        }

        protected override void DeleteParameters(IDbCommand command, Curso e) {
            SqlParameter sigla = new SqlParameter("@id", e.Sigla);
            command.Parameters.Add(sigla);
        }

        protected override void InsertParameters(IDbCommand command, Curso e) {
            SqlParameter sigla = new SqlParameter("@sigla", e.Sigla);
            SqlParameter dpt = new SqlParameter("@dpt", e.Departamento);
            SqlParameter desc = new SqlParameter("@desc", e.Descricao);

            command.Parameters.Add(sigla);
            command.Parameters.Add(dpt);
            command.Parameters.Add(desc);
        }

        protected override Curso Map(IDataRecord record) {
            Curso c = new Curso();
            c.Sigla = record.GetString(0);
            c.Departamento = record.GetString(1);
            c.Descricao = record.GetString(2);
            return c;
        }

        protected override void SelectParameters(IDbCommand command, string k) {
            SqlParameter p1 = new SqlParameter("@id", k);
            command.Parameters.Add(p1);
        }

        protected override Curso UpdateEntityID(IDbCommand cmd, Curso e) {
            var param = cmd.Parameters["@id"] as SqlParameter;
            e.Sigla = param.Value.ToString();
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Curso e) {
            InsertParameters(command, e);
        }
    }
}
