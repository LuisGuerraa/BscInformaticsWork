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
    class MapperAluno_UC : AbstractMapper<Aluno_UC, int, List<Aluno_UC>>, IMapperAluno_UC
    {
        public MapperAluno_UC(IContext ctx) : base(ctx) {
        }

        protected override string SelectAllCommandText => "select * from Aluno_UC";

        protected override string SelectCommandText => "select * from Aluno_UC where aluno = @Numero";

        protected override string UpdateCommandText => "update set nota = @nota where aluno = @Numero";

        protected override string DeleteCommandText => "delete from Aluno_UC where aluno = @Numero";

        protected override string InsertCommandText => "insert into Aluno_UC values(@Numero, @curso, @uc, @ano_letivo,@ano_conclusao,@nota)";

        protected override void DeleteParameters(IDbCommand command, Aluno_UC e) {
            SqlParameter num = new SqlParameter("@Numero", e.Numero);
            command.Parameters.Add(num);
        }

        protected override void InsertParameters(IDbCommand command, Aluno_UC e) {
            SqlParameter aluno = new SqlParameter("@Numero", e.Numero);
            command.Parameters.Add(aluno);
        }

        protected override Aluno_UC Map(IDataRecord record) {
            Aluno_UC au = new Aluno_UC
            {
                Numero = record.GetInt32(0),
                Curso = record.GetString(1),
                UC = record.GetString(2),
                Ano_Letivo = record.GetDateTime(3),
                Ano_Conclusao = record.GetDateTime(4),
                Nota = record.GetByte(5)

            };
            return au;
        }

        protected override void SelectParameters(IDbCommand command, int k) {
            SqlParameter p1 = new SqlParameter("@Numero", k);
            command.Parameters.Add(p1);
        }

        protected override Aluno_UC UpdateEntityID(IDbCommand cmd, Aluno_UC e) {
            var param = cmd.Parameters["@Numero"] as SqlParameter;
            e.Numero = int.Parse(param.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Aluno_UC e) {
            InsertParameters(command, e);
        }

        public override Aluno_UC Create(Aluno_UC entity) {
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

        public override Aluno_UC Delete(Aluno_UC entity) {
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

        public override Aluno_UC Read(int id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Aluno_UC entity = new Aluno_UC();
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

        public override List<Aluno_UC> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Aluno_UC> result = new List<Aluno_UC>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Aluno_UC a = Map(info);
                        result.Add(a);
                    }
                }
                ts.Complete();
                return result;
            }
        }

        public override Aluno_UC Update(Aluno_UC entity) {
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
    }
}
