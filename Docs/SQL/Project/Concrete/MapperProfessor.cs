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
    class MapperProfessor : AbstractMapper<Professor, string, List<Professor>>, IMapperProfessor
    {
        public MapperProfessor(IContext ctx) : base(ctx) {
        }
        protected override string SelectAllCommandText
        {
            get
            {
                return "SELECT prof_cc, nome, area_espec, categoria FROM Professor";
            }
        }

        protected override string SelectCommandText
        {
            get
            {
                return String.Format("{0} WHERE prof_cc=@id", SelectAllCommandText);
            }
        }
        protected override string UpdateCommandText
        {
            get
            {
                return "UPDATE Professor SET prof_cc = @cc, nome = @nome, area_espec = @area, categoria = @categoria WHERE prof_cc = @cc; select @id=scope_identity()";
            }
        }
        protected override string DeleteCommandText
        {
            get
            {
                return "DELETE FROM Professor WHERE prof_cc = @id; select @id=scope_identity()";
            }
        }
        protected override string InsertCommandText
        {
            get
            {
                return "INSERT INTO Professor (prof_cc,nome,area_espec,categoria) VALUES(@cc,@nome,@area,@categoria); select @id=scope_identity()";
            }
        }

        public override Professor Create(Professor entity) {
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
        public override Professor Delete(Professor entity) {
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
        public override Professor Read(string id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Professor entity = new Professor();
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
        public override List<Professor> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Professor> result = new List<Professor>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Professor c = Map(info);
                        result.Add(c);
                    }
                }
                ts.Complete();
                return result;
            }
        }
        public override Professor Update(Professor entity) {
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

        protected override void DeleteParameters(IDbCommand command, Professor e) {
            SqlParameter sigla = new SqlParameter("@id", e.CC);
            command.Parameters.Add(sigla);
        }

        protected override void InsertParameters(IDbCommand command, Professor e) {
            SqlParameter cc = new SqlParameter("@cc", e.CC);
            SqlParameter nome = new SqlParameter("@nome", e.Nome);
            SqlParameter area = new SqlParameter("@area", e.Area_Espec);
            SqlParameter categoria = new SqlParameter("@categoria", e.Categoria);

            command.Parameters.Add(cc);
            command.Parameters.Add(nome);
            command.Parameters.Add(area);
            command.Parameters.Add(categoria);
        }

        protected override Professor Map(IDataRecord record) {
            Professor c = new Professor();
            c.CC = record.GetString(0);
            c.Nome = record.GetString(1);
            c.Area_Espec = record.GetString(2);
            c.Categoria = record.GetString(3);
            return c;
        }

        protected override void SelectParameters(IDbCommand command, string k) {
            SqlParameter p1 = new SqlParameter("@id", k);
            command.Parameters.Add(p1);
        }

        protected override Professor UpdateEntityID(IDbCommand cmd, Professor e) {
            var param = cmd.Parameters["@id"] as SqlParameter;
            e.CC = param.Value.ToString();
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Professor e) {
            InsertParameters(command, e);
        }
    }
}
