package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d5fc68df";
    private ConvierteDatos conversor = new ConvierteDatos();

    private SerieRepository repositorio;
    private List<DatosSerie> listaDeSeries = new ArrayList<>();
    private  List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repo) {
        this.repositorio = repo;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por titulo
                    5 - Mejores series
                    6 - Buscar por categoria
                    7 - Buscar por número de temporadas
                    8 - Buscar por calificación
                    9 - Buscar por evaluacion y numero de temporadas
                    10 - Buscar episodio por titulo 
                    11 - Top 5 episodios
                    12 - Top 5 episodios por serie
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarseriesBuscadas();
                    break;
                    
                case 4:
                    buscarSerieTitulo();
                    break;
                case 5:
                    buscarTopSeries();
                    break;

                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscaPorNumeroDeTemporadas();
                    break;
                case 8:
                    buscarPorNumeroDeCalificacion();
                    break;
                case 9:
                    buscarPorNumeroDeCalificacionYTotalTemporadas();
                    break;
                case 10:
                    buscarEpisodioPorTitulo();
                    break;
                case 11:
                    mostraTop5Episodios();
                    break;
                case 12:
                    top5EpisodiosPorSerie();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void top5EpisodiosPorSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            var episodios = this.repositorio.top5EpisodiosPorSerie(serieBuscada.get());
            episodios.forEach(System.out::println);
        }else{
            System.out.println("La serie no existe");
        }

    }

    private void mostraTop5Episodios() {
        var episosodios = this.repositorio.top5Episodios();
        System.out.println("episodios");
        episosodios.forEach(System.out::println);
    }

    private void buscarEpisodioPorTitulo() {
        System.out.println("Ingrese el titulo del episodio: ");
        var  titulo = teclado.nextLine();
        List<Episodio> episodios = this.repositorio.episodioPorNombre(titulo);
        episodios.forEach(System.out::println);
    }


    private void buscarPorNumeroDeCalificacionYTotalTemporadas() {
        System.out.println("Ingresa el total de temporadas por lo que quieres filtrar las series: ");
        var  temporadas = teclado.nextInt();
        System.out.println("Ingresa el valor de la calificación: ");
        var evaluacion = teclado.nextDouble();

        List<Serie>  series= this.repositorio.seriesTemporadasYEvaluacion(temporadas,evaluacion);
        series.forEach(s -> System.out.println("nombre: " + s.getTitulo() + " -> " + "evaluacion: " + s.getEvaluacion() + " temporadas: " + s.getTotalTemporadas()) );

    }

    private void buscarPorNumeroDeCalificacion() {
        System.out.println("Ingresa el valor de la calificación: ");
        var evaluacion = teclado.nextDouble();
        List<Serie> series = this.repositorio.findByEvaluacionGreaterThanEqual(evaluacion);
        System.out.println("Series encontradas: ");
        series.forEach(System.out::println);

    }

    private void buscaPorNumeroDeTemporadas() {
        System.out.println("Ingresa el total de temporadas por lo que quieres filtrar las series: ");
        var  temporadas = teclado.nextInt();
        List<Serie> series = this.repositorio.findByTotalTemporadasGreaterThanEqual(temporadas);
        System.out.println("Series encontradas: ");
        series.forEach(System.out::println);

    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoría de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoría " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }



    private void buscarTopSeries() {
        List<Serie> topSeries = this.repositorio.findTop5ByOrderByEvaluacionDesc();

        topSeries.forEach(s -> System.out.printf("\n" +
                "Titulo: " + s.getTitulo() + "\n" +
                "Evaluacion: " + s.getEvaluacion() + "\n" ));

    }

    private void buscarSerieTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }

    }


    private void mostrarseriesBuscadas() {
        this.series = this.repositorio.findAll();

        System.out.println("Series:");
        this.series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);




    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarseriesBuscadas();
        System.out.println("Escribe el nombre de la serie, para obtener sus episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            episodios.forEach(e -> e.setSerie(serieEncontrada)); // Establecer la relación bidireccional

            serieEncontrada.setEpisodios(episodios);
            this.repositorio.save(serieEncontrada);
        }
    }





    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        this.repositorio.save(serie);
        System.out.println(datos);
    }


}

