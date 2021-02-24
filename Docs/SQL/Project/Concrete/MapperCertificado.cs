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
    class MapperCertificado : AbstractMapper<Certificado, int, List<Certificado>>, IMapperCertificado
    {
        public MapperCertificado(IContext ctx) : base(ctx) {
        }

        protected override string SelectAllCommandText => "select * from Certificado";

        protected override string SelectCommandText => "select * from Certificado where aluno = @aluno";

        protected override string UpdateCommandText => "update set nota_final = @nota where aluno = @aluno";

        protected override string DeleteCommandText => "delete from Certificado where aluno = @aluno";

        protected override string InsertCommandText => "insert into Certificado values (@nota_final,@data_conclusao,@aluno)";

        protected override void DeleteParameters(IDbCommand command, Certificado e) {
            SqlParameter date = new SqlParameter("@date", e.Data_Conclusao);
            command.Parameters.Add(date);
        }

        protected override void InsertParameters(IDbCommand command, Certificado e) {
            SqlParameter nota = new SqlParameter("@nota_final", e.Nota_Final);
            command.Parameters.Add(nota);
        }

        protected override Certificado Map(IDataRecord record) {
            Certificado c = new Certificado();
            c.Nota_Final = record.GetInt32(0);
            c.Data_Conclusao = record.GetDateTime(1);
            c.Aluno = record.GetInt32(2);

            return c;
        }

        protected override void SelectParameters(IDbCommand command, int k) {
            throw new NotImplementedException();
        }

        protected override Certificado UpdateEntityID(IDbCommand cmd, Certificado e) {
            throw new NotImplementedException();
        }

        protected override void UpdateParameters(IDbCommand command, Certificado e) {
            throw new NotImplementedException();
        }
        public override Certificado Create(Certificado entity) {
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

        public override Certificado Delete(Certificado entity) {
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

        public override Certificado Read(int id) {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                Certificado entity = new Certificado();
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

        public override List<Certificado> ReadAll() {
            using (TransactionScope ts = new TransactionScope(TransactionScopeOption.Required)) {
                List<Certificado> result = new List<Certificado>();
                EnsureContext();
                context.EnlistTransaction();

                using (IDbCommand cmd = context.createCommand()) {
                    cmd.CommandText = SelectAllCommandText;
                    cmd.CommandType = SelectAllCommandType;
                    SelectAllParameters(cmd);
                    IDataReader reader = cmd.ExecuteReader();
                    while (reader.Read()) {
                        IDataRecord info = reader;
                        Certificado a = Map(info);
                        result.Add(a);
                    }
                }
                ts.Complete();
                return result;
            }
        }

        public override Certificado Update(Certificado entity) {
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
