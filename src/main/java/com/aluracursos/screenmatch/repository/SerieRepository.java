package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long > {
    Optional<Serie> findById(Long id);
    Optional<Serie> findByTituloContainsIgnoreCase (String title);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByTotalTemporadasGreaterThanEqual(Integer total);
    List<Serie> findByEvaluacionGreaterThanEqual(Double evaluacion);

    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, double evaluacion);

    // busquedad con Query
    @Query(value = "SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesTemporadasYEvaluacion(int totalTemporadas, Double evaluacion);

    @Query(value = "SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo Like %:titulo%")
    List<Episodio> episodioPorNombre(String titulo);
    @Query(value="SELECT e from Serie s JOIN s.episodios e ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios();
    @Query(value="SELECT e FROM  Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie(Serie serie);

    @Query(value="SELECT s FROM  Serie s  JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 10 ")
    List<Serie> seriesMasRecientes();

    @Query(value="SELECT e FROM  Serie s JOIN s.episodios e WHERE s = :serie")
    List<Episodio> episodiosPorSerie(Serie serie);

    @Query(value="SELECT e FROM  Serie s JOIN s.episodios e WHERE s = :serie AND e.temporada = :temporada")
    List<Episodio> episodiosPorSerieTemporada(Serie serie, int temporada);

    //@Query(value = "SELECT e FROM Serie s WHERE s.genero = :genero")




}
