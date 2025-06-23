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
}
