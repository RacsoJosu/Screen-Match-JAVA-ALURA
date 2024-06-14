package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.DTO.EpisodioDTO;
import com.aluracursos.screenmatch.DTO.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesService {
    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> getAll(){
        return this.transformarDatos( this.repository.findAll());
    }

    public List<SerieDTO> getTop5Series(){
        return this.transformarDatos(this.repository.findTop5ByOrderByEvaluacionDesc());

    }

    public List<SerieDTO> obtenerLanzamientosMasRecientes() {
        return this.transformarDatos(this.repository.seriesMasRecientes());
    }

    public List<EpisodioDTO> getEpisodiosBySerie(Long id) {
        var serie = this.repository.findById(id);

        if (serie.isPresent()){
            return this.transformarDatosEpisodio(this.repository.episodiosPorSerie(serie.get()));
        }else{
            return null;
        }

    }

    public List<SerieDTO> getSeriesByCategoria(String genero){

        Categoria categoria = Categoria.fromEspanol(genero);
        return this.transformarDatos(this.repository.findByGenero(categoria));

    }

    public List<EpisodioDTO> getEpisodiosBySerieAndTemporada(Long id, int temporada) {
        var serie = this.repository.findById(id);

        if (serie.isPresent()){
            return this.transformarDatosEpisodio(this.repository.episodiosPorSerieTemporada(serie.get(), temporada));
        }else{
            return null;
        }

    }

    public List<EpisodioDTO> transformarDatosEpisodio(List<Episodio> episodios){
        return episodios.stream()
                .map(e -> new EpisodioDTO(e.getID(), e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio(), e.getEvaluacion(), e.getFechaDeLanzamiento()))
                .collect(Collectors.toList());
    }
    public List<SerieDTO> transformarDatos(List<Serie> series) {

        return series.stream()
                .map(s -> new SerieDTO(s.getID(),s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(), s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis()))
                .collect(Collectors.toList());
    }

    public SerieDTO getById(Long id) {
        var s = this.repository.findById(id);
        if (s.isPresent()){
            return new SerieDTO(s.get().getID(),s.get().getTitulo(), s.get().getTotalTemporadas(), s.get().getEvaluacion(), s.get().getPoster(),
                    s.get().getGenero(), s.get().getActores(), s.get().getSinopsis() );
        }else {
            return null;
        }
    }


}
