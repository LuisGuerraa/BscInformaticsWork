using System.Collections.Generic;
using TP2.Mappers.interfaces;
using TP2.Model;

namespace TP2.Mappers
{
    public interface IMapperCurso : IMapper<Curso, string, List<Curso>>
    {
    }
}
