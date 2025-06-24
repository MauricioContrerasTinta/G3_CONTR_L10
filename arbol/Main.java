package arbol;
import excepciones.ItemNoFound;

public class Main {
    public static void main(String[] args) {
        try {
            String ruta = "arbolB.txt";
            BTree<Integer> arbol = BTree.building_Btree(ruta);
            
            System.out.println("Arbol B construido:");
            System.out.println(arbol.toString());

            // Prueba de búsqueda
            System.out.println("\n¿Existe 27?: " + arbol.search(19));
            System.out.println("¿Existe 77?: " + arbol.search(100));

            // Prueba de inserción
            arbol.insert(13);
            System.out.println("\nDespues de insertar 13:");
            System.out.println(arbol);

            // Prueba de eliminación
            arbol.remove(10);
            System.out.println("\nDespués de eliminar 10:");
            System.out.println(arbol);

        } catch (ItemNoFound e) {
            System.err.println("Error al construir: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Otro error: " + e.getMessage());
        }
    }
}