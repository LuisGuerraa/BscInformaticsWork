namespace TP2.Mappers.interfaces
{
    public interface IMapper<T, Tid, TCol>
    {
        T Create(T entity);
        T Read(Tid id);
        TCol ReadAll();
        T Update(T entity);
        T Delete(T entity);
    }
}
