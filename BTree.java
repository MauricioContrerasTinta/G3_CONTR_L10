public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden){
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty(){
        return this.root == null;
    }

    public void insert(E cl){
        up = false;
        E mediana;
        BNode<E> pnew;
        mediana = push(this.root, cl);
        if(up){
            pnew = new BNode<E>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl){
        int[] pos = new int[1];
        E mediana;
        if(current == null){
            up = true;
            nDes = null;
            return cl;
        } else {
            boolean found = current.searchNode(cl, pos);
            if(found){
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            mediana = push(current.childs.get(pos[0]), cl);

            if(up){
                if(current.nodeFull()){
                    mediana = divideNode(current, mediana, pos[0]);
                } else {
                    putNode(current, mediana, nDes, pos[0]);
                    up = false;
                }
            }
            return mediana;
        }
    }
    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k){
        int i;
        for (i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }

    private E divideNode(BNode<E> current, E cl, int k){
        BNode<E> rd = nDes;
        int i;
        int posMdna = (k <= orden / 2) ? orden / 2 : orden / 2 + 1;

        nDes = new BNode<E>(orden);
        for (i = posMdna; i < orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }

        nDes.count = (orden - 1) - posMdna;
        current.count = posMdna;

        if (k <= orden / 2) {
            putNode(current, cl, rd, k);
        } else {
            putNode(nDes, cl, rd, k - posMdna);
        }

        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;

        return median;
    }

    public void imprimirArbol(){
        imprimirArbol(this.root, 0);
    }

    private void imprimirArbol(BNode<E> nodo, int nivel){
        if(nodo != null){
            System.out.println("Nivel " + nivel + ": " + nodo);
            for (int i = 0; i <= nodo.count; i++) {
                imprimirArbol(nodo.childs.get(i), nivel + 1);
            }
        }
    }

    @Override
    public String toString(){
        String s = "";
        if (isEmpty()) {
            s += "BTree is empty...";
        } else {
            s = writeTree(this.root, 0);
        }
        return s;
    }

    private String writeTree(BNode<E> current, int nivel) {
        StringBuilder sb = new StringBuilder();

        if (current != null) {
            sb.append("Nivel ").append(nivel).append(" - Nodo ").append(current).append("\n");
            for (int i = 0; i <= current.count; i++) {
                sb.append(writeTree(current.childs.get(i), nivel + 1));
            }
        }
        return sb.toString();
    }

    public boolean search(E cl) {
    return search(this.root, cl);
}

    private boolean search(BNode<E> current, E cl) {
        if (current == null) {
            return false;
        }

        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);

        if (found) {
            System.out.println("Clave encontrada en el nodo con idNode = " + current.idNode + 
                            ", en la posición = " + pos[0]);
            return true;
        } else {
            return search(current.childs.get(pos[0]), cl);
        }
    }

    public void remove(E cl) {
        if (root != null) {
            remove(root, cl);

            // Si la raíz se quedó vacía y tiene hijos, se reduce la altura del árbol
            if (root.count == 0 && root.childs.get(0) != null) {
                root = root.childs.get(0);
            } else if (root.count == 0) {
                root = null;
            }
        }
    }

    private boolean remove(BNode<E> node, E cl) {
        int[] pos = new int[1];
        boolean found = node.searchNode(cl, pos);

        if (found) {
            if (esHoja(node)) {
                //clave en hoja
                for (int i = pos[0]; i < node.count - 1; i++) {
                    node.keys.set(i, node.keys.get(i + 1));
                }
                node.keys.set(node.count - 1, null);
                node.count--;
            } else {
                //clave en nodo interno
                BNode<E> predNode = node.childs.get(pos[0]);
                if (predNode.count >= orden / 2) {
                    E pred = getPredecesor(predNode);
                    node.keys.set(pos[0], pred);
                    remove(predNode, pred);
                } else {
                    BNode<E> succNode = node.childs.get(pos[0] + 1);
                    if (succNode.count >= orden / 2) {
                        E succ = getSucesor(succNode);
                        node.keys.set(pos[0], succ);
                        remove(succNode, succ);
                    } else {
                        merge(node, pos[0]);
                        remove(predNode, cl);
                    }
                }
            }
        } else {
            if (esHoja(node)) {
                System.out.println("no encontrada.");
                return false;
            }

            BNode<E> child = node.childs.get(pos[0]);
            if (child.count < orden / 2) {
                fixChild(node, pos[0]);
            }
            remove(node.childs.get(pos[0]), cl);
        }
        return true;
    }

    private boolean esHoja(BNode<E> node) {
        return node.childs.get(0) == null;
    }

    private E getPredecesor(BNode<E> node) {
        while (!esHoja(node)) node = node.childs.get(node.count);
        return node.keys.get(node.count - 1);
    }

    private E getSucesor(BNode<E> node) {
        while (!esHoja(node)) node = node.childs.get(0);
        return node.keys.get(0);
    }

    private void fixChild(BNode<E> parent, int index) {
        if (index > 0 && parent.childs.get(index - 1).count >= orden / 2) {
            prestadoIzquierda(parent, index);
        } else if (index < parent.count && parent.childs.get(index + 1).count >= orden / 2) {
            prestadoDerecha(parent, index);
        } else {
            if (index > 0) {
                merge(parent, index - 1);
            } else {
                merge(parent, index);
            }
        }
    }
    
    private void prestadoIzquierda(BNode<E> parent, int index) {
        BNode<E> child = parent.childs.get(index);
        BNode<E> nodoIzquierdo = parent.childs.get(index - 1);

        for (int i = child.count; i > 0; i--) {
            child.keys.set(i, child.keys.get(i - 1));
            child.childs.set(i + 1, child.childs.get(i));
        }
        child.childs.set(1, child.childs.get(0));
        child.keys.set(0, parent.keys.get(index - 1));
        child.childs.set(0, nodoIzquierdo.childs.get(nodoIzquierdo.count));
        child.count++;

        parent.keys.set(index - 1, nodoIzquierdo.keys.get(nodoIzquierdo.count - 1));
        nodoIzquierdo.count--;
    }

    private void prestadoDerecha(BNode<E> parent, int index) {
        BNode<E> child = parent.childs.get(index);
        BNode<E> nodoDerecha = parent.childs.get(index + 1);

        child.keys.set(child.count, parent.keys.get(index));
        child.childs.set(child.count + 1, nodoDerecha.childs.get(0));
        child.count++;

        parent.keys.set(index, nodoDerecha.keys.get(0));

        for (int i = 0; i < nodoDerecha.count - 1; i++) {
            nodoDerecha.keys.set(i, nodoDerecha.keys.get(i + 1));
            nodoDerecha.childs.set(i, nodoDerecha.childs.get(i + 1));
        }
        nodoDerecha.childs.set(nodoDerecha.count - 1, nodoDerecha.childs.get(nodoDerecha.count));
        nodoDerecha.count--;
    }
}
