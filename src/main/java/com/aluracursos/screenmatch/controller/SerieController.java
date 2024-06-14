package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.DTO.EpisodioDTO;
import com.aluracursos.screenmatch.DTO.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.service.SeriesService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/series")
@RestController
public class SerieController {
    @Autowired
    private SeriesService service;
    @GetMapping()
    public List<SerieDTO> getSeries(){
        return this.service.getAll();
    }
    @GetMapping("/top5")
    public List<SerieDTO> getTop5Series(){
        return this.service.getTop5Series();
    }

    @GetMapping("/masRecientes")
    public List<SerieDTO> getMasRecientes(){
        return this.service.obtenerLanzamientosMasRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO getSerie(@PathVariable Long id){
        return this.service.getById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id){
        return  this.service.getEpisodiosBySerie(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id, @PathVariable int temporada){
        return  this.service.getEpisodiosBySerieAndTemporada(id, temporada);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> obtenerSeriesPorGenero(@PathVariable String genero){
        return this.service.getSeriesByCategoria(genero);
    }



}
