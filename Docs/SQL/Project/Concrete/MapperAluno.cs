using DAL;
using DAL.mapper;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Transactions;
using TP2.Mappers.interfaces;
using TP2.Model;

namespace TP2.Concrete
{
    class MapperAluno : AbstractMapper<Aluno, int, List<Aluno>>, IMapperAluno
    {
        public MapperAluno(IContext ctx) : base(ctx) {
        }

        protected override string SelectAllCommandText => "select * from Aluno";

        protected override string SelectCommandText => "select * from Aluno where num_aluno = @num";

        protected override string UpdateCommandText => "update Aluno set nome = @nome where num_aluno = @num";

        protected override string DeleteCommandText => "delete from Aluno where num_aluno = @num";

        protected override string InsertCommandText => "insert into Aluno values (@num,@cc,@nome,@morada,@data)";
        public override Aluno Create(Aluno entity) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = InsertCommandText;
                    cmd.CommandType = InsertCommandType;
                    InsertParameters(cmd, entity);
                }
                ts.Complete();
                return entity;
            }
        }

        public override Aluno Delete(Aluno entity) {
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

        public override Aluno Read(int id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Aluno entity = new Aluno();
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
                }
                ts.Complete();
                return entity;
            }
        }

        public override List<Aluno> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Aluno> result = new List<Aluno>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Aluno a = Map(info);
                        result.Add(a);
                    }
                }
                ts.Complete();
                return result;
            }
        }

        public override Aluno Update(Aluno entity) {
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

        protected override void DeleteParameters(IDbCommand command, Aluno e) {
            SqlParameter num = new SqlParameter("@num", e.Numero);
            command.Parameters.Add(num);
        }

        protected override void InsertParameters(IDbCommand command, Aluno e) {
            SqlParameter numero = new SqlParameter("@num", e.Numero);
            SqlParameter cc = new SqlParameter("@cc", e.CC);
            SqlParameter nome = new SqlParameter("@nome", e.Nome);
            SqlParameter morada = new SqlParameter("@morada", e.Morada);
            SqlParameter data = new SqlParameter("@data", e.Data);

            command.Parameters.Add(numero);
            command.Parameters.Add(cc);
            command.Parameters.Add(nome);
            command.Parameters.Add(morada);
            command.Parameters.Add(data);
        }

        protected override Aluno Map(IDataRecord record) {
            Aluno a = new Aluno();
            a.Numero = (int)record.GetDecimal(0);
            a.CC = record.GetString(1);
            a.Nome = record.GetString(2);
            a.Morada = record.GetString(3);
            a.Data = record.GetDateTime(4);
            return a;
        }

        protected override void SelectParameters(IDbCommand command, int k) {
            SqlParameter p1 = new SqlParameter("@num", k);
            command.Parameters.Add(p1);
        }

        protected override Aluno UpdateEntityID(IDbCommand cmd, Aluno e) {
            var param = cmd.Parameters["@num"] as SqlParameter;
            e.Numero = int.Parse(param.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Aluno e) {
            InsertParameters(command, e);
        }
    }
}
