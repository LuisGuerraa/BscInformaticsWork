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
    class MapperAluno_Curso : AbstractMapper<Aluno_Curso, int, List<Aluno_Curso>>, IMapperAluno_Curso
    {
        public MapperAluno_Curso(IContext ctx) : base(ctx) {
        }

        protected override string SelectAllCommandText => "select * from Aluno_Curso";

        protected override string SelectCommandText => "select * from Aluno_Curso where aluno = @numAluno";

        protected override string UpdateCommandText => "update Aluno_Curso set curso = @curso and ano = @ano where aluno = @numAluno";

        protected override string DeleteCommandText => "delete from Aluno_Curso where aluno = @numAluno";

        protected override string InsertCommandText => "insert into Aluno_Curso values (@numAluno,@curso,@ano)";

        protected override void DeleteParameters(IDbCommand command, Aluno_Curso e) {
            SqlParameter aluno = new SqlParameter("@numAluno", e.Aluno);
            command.Parameters.Add(aluno);
        }

        protected override void InsertParameters(IDbCommand command, Aluno_Curso e) {
            SqlParameter aluno = new SqlParameter("@numAluno", e.Aluno);
            SqlParameter curso = new SqlParameter("@curso", e.Curso);
            SqlParameter ano = new SqlParameter("@ano", e.Ano);

            command.Parameters.Add(aluno);
            command.Parameters.Add(curso);
            command.Parameters.Add(ano);

        }

        protected override Aluno_Curso Map(IDataRecord record) {
            Aluno_Curso ac = new Aluno_Curso
            {
                Aluno = record.GetInt32(0),
                Curso = record.GetString(1),
                Ano = record.GetDateTime(2),

            };
            return ac;
        }

        protected override void SelectParameters(IDbCommand command, int k) {
            SqlParameter p1 = new SqlParameter("@numAluno", k);
            command.Parameters.Add(p1);
        }

        protected override Aluno_Curso UpdateEntityID(IDbCommand cmd, Aluno_Curso e) {
            var param = cmd.Parameters["@numAluno"] as SqlParameter;
            e.Aluno = int.Parse(param.Value.ToString());
            return e;
        }

        protected override void UpdateParameters(IDbCommand command, Aluno_Curso e) {
            InsertParameters(command, e);
        }

        public override Aluno_Curso Create(Aluno_Curso entity) {
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

        public override Aluno_Curso Delete(Aluno_Curso entity) {
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

        public override Aluno_Curso Read(int id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Aluno_Curso entity = new Aluno_Curso();
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

        public override List<Aluno_Curso> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Aluno_Curso> result = new List<Aluno_Curso>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Aluno_Curso a = Map(info);
                        result.Add(a);
                    }
                }
                ts.Complete();
                return result;
            }
        }

        public override Aluno_Curso Update(Aluno_Curso entity) {
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
