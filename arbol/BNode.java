package arbol;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;
    private static int nextId = 0;
    protected int idNode;

    public BNode(int n) {
        this.count = 0;
        this.keys = new ArrayList<>();
        this.childs = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            this.keys.add(null);
        }
        for (int i = 0; i < n; i++) {
            this.childs.add(null);
        }
        this.idNode = nextId++;
    }

    public boolean nodeFull() {
        return count == keys.size();
    }

    public boolean nodeEmpty() {
        return count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        int i = 0;
        while (i < count && key.compareTo(keys.get(i)) > 0) {
            i++;
        }
        if (i < count && key.compareTo(keys.get(i)) == 0) {
            pos[0] = i;
            return true;
        } else {
            pos[0] = i;
            return false;
        }
    }

    public int getIdNode() {
        return idNode;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Node ").append(idNode).append(": [");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i));
            if (i < count - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
