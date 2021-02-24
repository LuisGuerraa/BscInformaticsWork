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
    class MapperUC_Curso : AbstractMapper<UC_Curso, UC_Curso.ID, List<UC_Curso>>, IMapperUC_Curso
    {

        public MapperUC_Curso(IContext ctx) : base(ctx) { }
        protected override string SelectAllCommandText { get { return "SELECT * FROM UC_Curso"; } }

        protected override string SelectCommandText { get { return "SELECT * FROM UC_Curso WHERE uc = @uc"; } }

        protected override string UpdateCommandText { get { return "UPDATE UC_Curso SET curso = @curso WHERE uc = @uc"; } }

        protected override string DeleteCommandText { get { return "DELETE FROM UC_Curso WHERE uc = @uc"; } }

        protected override string InsertCommandText { get { return "INSERT INTO UC_Curso VALUES (@uc, @ano, @curso, @semestre)"; } }

        public override UC_Curso Create(UC_Curso entity) {
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

        public override UC_Curso Delete(UC_Curso entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();
                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = DeleteCommandText;
                    cmd.CommandType = DeleteCommandType;
                    DeleteParameters(cmd, entity);
                    cmd.ExecuteNonQuery();
                }
                ts.Complete();
                return entity;
            }
        }

        public override UC_Curso Read(UC_Curso.ID id) {
            using TransactionScope ts = new TransactionScope(TransactionScopeOption.Required);
            UC_Curso entity = new UC_Curso();
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

        public UC_Curso Read(int id) {
            throw new NotImplementedException();
        }

        public override UC_Curso Update(UC_Curso entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = UpdateCommandText;
                    cmd.CommandType = UpdateCommandType;
                    UpdateParameters(cmd, entity);
                    cmd.ExecuteNonQuery();
                }
                ts.Complete();
                return entity;
            }
        }

        protected override void DeleteParameters(IDbCommand command, UC_Curso e) {
            SqlParameter uc = new SqlParameter("@uc", SqlDbType.Char);
            SqlParameter ano = new SqlParameter("@ano", SqlDbType.DateTime);
            command.Parameters.Add(uc);
            command.Parameters.Add(ano);
        }

        protected override void InsertParameters(IDbCommand command, UC_Curso e) {
            SqlParameter uc = new SqlParameter("@uc", e._ID.UC);
            SqlParameter ano = new SqlParameter("@ano", e.Date);
            SqlParameter curso = new SqlParameter("@curso", e.Curso);
            SqlParameter semestre = new SqlParameter("@semestre", e.Semestre);
            command.Parameters.Add(uc);
            command.Parameters.Add(ano);
            command.Parameters.Add(curso);
            command.Parameters.Add(semestre);
        }

        protected override UC_Curso Map(IDataRecord record) {
            UC_Curso uc = new UC_Curso();
            uc._ID.UC = record.GetString(0);
            uc.Date = record.GetDateTime(1);
            uc.Curso = record.GetString(2);
            uc.Semestre = record.GetInt32(3);
            return uc;
        }

        protected override void SelectParameters(IDbCommand command, UC_Curso.ID k) {
            SqlParameter p1 = new SqlParameter("@uc", k.UC);
            command.Parameters.Add(p1);
        }

        protected override UC_Curso UpdateEntityID(IDbCommand cmd, UC_Curso e) {
            var param = cmd.Parameters["@uc"] as SqlParameter;
            e._ID.UC = param.Value.ToString();
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, UC_Curso e) {
            InsertParameters(command, e);
        }
    }
}
