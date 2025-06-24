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
            System.out.println("\n¿Existe 19?: " + arbol.search(19));
            System.out.println("¿Existe 100?: " + arbol.search(100));

        }
    }
}