package pruebas;
import arbol.BTree;
import modelo.RegistroEstudiante;

public class MainEstudiantes {
    public static void main(String[] args) {
        //crear arbol B de orden 4 que almacena objetos de tipo RegistroEstudiante
        BTree<RegistroEstudiante> arbol = new BTree<>(4);
    }
}

