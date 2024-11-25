import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContadorPalabrasParalelo {

    public static void main(String[] args) {
        String archivo = "texto.txt"; // Ruta
        
        try {
            Map<String, Long> frecuenciaPalabras = Files.lines(Paths.get(archivo))  // Leer las lineas del archivo
                    .parallel()  // Hacerlo en paralelo
                    .flatMap(linea -> Arrays.stream(linea.split("\\W+")))  // Dividir cada linea en palabras
                    .filter(palabra -> !palabra.isEmpty())  // Filtrar palabras vacias
                    .map(String::toLowerCase)  // Convertir a minusculas para evitar distinción de mayusculas
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));  // Contar frecuencia

            // Mostrar el resultado
            frecuenciaPalabras.forEach((palabra, frecuencia) -> {
                System.out.println(palabra + ": " + frecuencia);
            });

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
