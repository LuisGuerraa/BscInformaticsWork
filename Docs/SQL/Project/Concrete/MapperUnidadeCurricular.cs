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
    class MapperUnidadeCurricular : AbstractMapper<UnidadeCurricular, UnidadeCurricular.ID, List<UnidadeCurricular>>, IMapperUnidadeCurricular
    {
        public MapperUnidadeCurricular(IContext ctx) : base(ctx) { }
        protected override string SelectAllCommandText { get { return "SELECT * FROM Unidade_Curricular"; } }

        protected override string SelectCommandText { get { return "SELECT * FROM Unidade_Curricular WHERE uc_sigla = @sigla AND ano = @ano"; } }

        protected override string UpdateCommandText { get { return "UPDATE Unidade_Curricular SET ano = @ano, creditos = @creditos, uc_desc = @uc_desc, regente = @regente WHERE uc_sigla = @sigla"; } }

        protected override string DeleteCommandText { get { return "DELETE FROM Unidade_Curricular WHERE uc_sigla = @sigla AND ano = @ano"; } }

        protected override string InsertCommandText { get { return "INSERT INTO Unidade_Curricular VALUES(@sigla, @ano, @creditos, @uc_desc, @regente )"; } }

        public override UnidadeCurricular Create(UnidadeCurricular entity) {
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

        public override UnidadeCurricular Delete(UnidadeCurricular entity) {
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
                }
                ts.Complete();
                return entity;
            }
        }

        public override UnidadeCurricular Read(UnidadeCurricular.ID id) {
            using TransactionScope ts = new TransactionScope(TransactionScopeOption.Required);
            UnidadeCurricular entity = new UnidadeCurricular();
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

        public UnidadeCurricular Read(int id) {
            throw new NotImplementedException();
        }

        public override List<UnidadeCurricular> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<UnidadeCurricular> result = new List<UnidadeCurricular>();
                EnsureContext();
                context.EnlistTransaction();
                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        UnidadeCurricular uc = Map(info);
                        result.Add(uc);
                    }
                    reader.Close();
                }
                ts.Complete();
                return result;
            }
        }
        public override UnidadeCurricular Update(UnidadeCurricular entity) {
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

        protected override void DeleteParameters(IDbCommand command, UnidadeCurricular e) {
            SqlParameter id = new SqlParameter("@sigla", e._ID.Sigla);
            SqlParameter ano = new SqlParameter("@ano", e._ID.Data);
            command.Parameters.Add(id);
            command.Parameters.Add(ano);
        }

        protected override void InsertParameters(IDbCommand command, UnidadeCurricular e) {
            // @sigla, @ano, @aluno_cc, @creditos, @uc_desc, @regente 
            SqlParameter uc_sigla = new SqlParameter("@sigla", e._ID.Sigla);
            SqlParameter ano = new SqlParameter("@ano", e._ID.Data);
            SqlParameter creditos = new SqlParameter("@creditos", e.Creditos);
            SqlParameter uc_desc = new SqlParameter("@uc_desc", e.Descricao);
            SqlParameter regente = new SqlParameter("@regente", e.Regente);
            uc_desc.Direction = ParameterDirection.InputOutput;

            if (e.Descricao == null)
                uc_desc.Value = DBNull.Value;

            command.Parameters.Add(uc_sigla);
            command.Parameters.Add(ano);
            command.Parameters.Add(creditos);
            command.Parameters.Add(uc_desc);
            command.Parameters.Add(regente);
        }

        protected override UnidadeCurricular Map(IDataRecord record) {
            UnidadeCurricular uc = new UnidadeCurricular();
            uc._ID.Sigla = record.GetString(0);
            uc._ID.Data = record.GetDateTime(1);
            uc.Creditos = record.GetInt32(2);
            uc.Descricao = record.GetString(3);
            uc.Regente = record.GetString(4);
            return uc;
        }

        protected override void SelectParameters(IDbCommand command, UnidadeCurricular.ID k) {
            SqlParameter p1 = new SqlParameter("@uc_sigla", k.Sigla);
            SqlParameter p2 = new SqlParameter("@ano", k.Data);
            command.Parameters.Add(p1);
            command.Parameters.Add(p2);
        }

        protected override UnidadeCurricular UpdateEntityID(IDbCommand cmd, UnidadeCurricular e) {
            var param1 = cmd.Parameters["@sigla"] as SqlParameter;
            var param2 = cmd.Parameters["@ano"] as SqlParameter;
            e._ID.Sigla = param1.Value.ToString();
            e._ID.Data = DateTime.Parse(param2.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, UnidadeCurricular e) {
            InsertParameters(command, e);
        }
    }
}