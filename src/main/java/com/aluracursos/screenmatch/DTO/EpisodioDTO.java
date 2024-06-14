package com.aluracursos.screenmatch.DTO;

import java.time.LocalDate;

public record EpisodioDTO(
        Long ID,
        Integer temporada,
        String titulo,
        Integer numeroEpisodio,
        Double evaluacion,
        LocalDate fechaDeLanzamiento
) {
}
