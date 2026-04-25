package co.edu.uniquindio.binarytree.model;


import lombok.Getter;

import java.util.LinkedList;

public class Arbol<T extends Comparable<T>> {

    @Getter
    private NodeTree<T> nodoRaiz;
    private int peso;

    public Arbol() {
        nodoRaiz = null;
        peso = 0;
    }

    public boolean estaVacio() {
        return nodoRaiz == null;
    }

    public void add(T info) {
        if (estaVacio()) {
            nodoRaiz = new NodeTree<>(info);
            peso++;
            return;
        }
        add(nodoRaiz, info);
    }

    private void add(NodeTree<T> root, T info) {
        int cmp = root.getInfo().compareTo(info);

        if (cmp == 0) {
            throw new RuntimeException("Ya existe el elemento: " + info);
        }

        if (cmp > 0) {                          // va a la izquierda
            if (root.getLeftChild() == null) {
                NodeTree<T> nuevo = new NodeTree<>(info);
                nuevo.setParent(root);
                root.setLeftChild(nuevo);
                peso++;
            } else {
                add(root.getLeftChild(), info);
            }
        } else {                                // va a la derecha
            if (root.getRightChild() == null) {
                NodeTree<T> nuevo = new NodeTree<>(info);
                nuevo.setParent(root);
                root.setRightChild(nuevo);
                peso++;
            } else {
                add(root.getRightChild(), info);
            }
        }
    }

    public void inOrden(NodeTree<T> nodo) {
        if (nodo == null) return;
        inOrden(nodo.getLeftChild());
        System.out.print(nodo.getInfo() + " ");
        inOrden(nodo.getRightChild());
    }

    public void preOrden(NodeTree<T> nodo) {
        if (nodo == null) return;
        System.out.print(nodo.getInfo() + " ");
        preOrden(nodo.getLeftChild());
        preOrden(nodo.getRightChild());
    }

    public void postOrden(NodeTree<T> nodo) {
        if (nodo == null) return;
        postOrden(nodo.getLeftChild());
        postOrden(nodo.getRightChild());
        System.out.print(nodo.getInfo() + " ");
    }


    public boolean existeDato(T elemento) {
        return search(nodoRaiz, elemento);
    }

    private boolean search(NodeTree<T> raiz, T elemento) {
        if (raiz == null) return false;

        int cmp = raiz.getInfo().compareTo(elemento);
        if (cmp == 0)  return true;
        if (cmp < 0)   return search(raiz.getRightChild(), elemento);
        return             search(raiz.getLeftChild(), elemento);
    }

    public int obtenerPeso() {
        return peso;
    }

    public int obtenerAltura() {
        return obtenerAltura(nodoRaiz);
    }

    private int obtenerAltura(NodeTree<T> nodo) {
        if (nodo == null) return -1;            // árbol vacío → -1
        int altIzq = obtenerAltura(nodo.getLeftChild());
        int altDer = obtenerAltura(nodo.getRightChild());
        return 1 + Math.max(altIzq, altDer);
    }

    public int obtenerNivel(T elemento) {
        return obtenerNivel(nodoRaiz, elemento, 0);
    }

    private int obtenerNivel(NodeTree<T> nodo, T elemento, int nivel) {
        if (nodo == null) return -1;

        int cmp = nodo.getInfo().compareTo(elemento);
        if (cmp == 0) return nivel;
        if (cmp < 0)  return obtenerNivel(nodo.getRightChild(), elemento, nivel + 1);
        return              obtenerNivel(nodo.getLeftChild(),  elemento, nivel + 1);
    }

    public int contarHojas() {
        return contarHojas(nodoRaiz);
    }

    private int contarHojas(NodeTree<T> nodo) {
        if (nodo == null) return 0;
        if (nodo.getLeftChild() == null && nodo.getRightChild() == null) return 1;
        return contarHojas(nodo.getLeftChild()) + contarHojas(nodo.getRightChild());
    }

    public T obtenerMenor() {
        if (estaVacio()) throw new RuntimeException("El árbol está vacío");
        return obtenerNodoMenor(nodoRaiz).getInfo();
    }

    private NodeTree<T> obtenerNodoMenor(NodeTree<T> nodo) {
        if (nodo.getLeftChild() == null) return nodo;
        return obtenerNodoMenor(nodo.getLeftChild());
    }


    public T obtenerMayor() {
        if (estaVacio()) throw new RuntimeException("El árbol está vacío");
        return obtenerNodoMayor(nodoRaiz).getInfo();
    }

    private NodeTree<T> obtenerNodoMayor(NodeTree<T> nodo) {
        if (nodo.getRightChild() == null) return nodo;
        return obtenerNodoMayor(nodo.getRightChild());
    }


    public void imprimirAmplitud() {
        if (estaVacio()) return;
        LinkedList<NodeTree<T>> lista = new LinkedList<>();
        lista.add(nodoRaiz);
        amplitud(lista);
        System.out.println();
    }

    private void amplitud(LinkedList<NodeTree<T>> parents) {
        if (parents.isEmpty()) return;

        LinkedList<NodeTree<T>> children = new LinkedList<>();
        while (!parents.isEmpty()) {
            NodeTree<T> aux = parents.removeFirst();
            System.out.print(aux.getInfo() + " ");
            if (aux.getLeftChild()  != null) children.add(aux.getLeftChild());
            if (aux.getRightChild() != null) children.add(aux.getRightChild());
        }
        amplitud(children);
    }

    public void eliminar(T elemento) {
        if (!existeDato(elemento))
            throw new RuntimeException("El elemento no existe: " + elemento);
        nodoRaiz = eliminar(nodoRaiz, elemento);
        peso--;
    }

    private NodeTree<T> eliminar(NodeTree<T> nodo, T elemento) {
        if (nodo == null) return null;

        int cmp = nodo.getInfo().compareTo(elemento);

        if (cmp > 0) {
            nodo.setLeftChild(eliminar(nodo.getLeftChild(), elemento));
        } else if (cmp < 0) {
            nodo.setRightChild(eliminar(nodo.getRightChild(), elemento));
        } else {

            if (nodo.getLeftChild() == null && nodo.getRightChild() == null) {
                return null;
            }

            if (nodo.getLeftChild() == null) {
                nodo.getRightChild().setParent(nodo.getParent());
                return nodo.getRightChild();
            }

            if (nodo.getRightChild() == null) {
                nodo.getLeftChild().setParent(nodo.getParent());
                return nodo.getLeftChild();
            }

            NodeTree<T> sucesor = obtenerNodoMenor(nodo.getRightChild());
            nodo.setInfo(sucesor.getInfo());
            nodo.setRightChild(eliminar(nodo.getRightChild(), sucesor.getInfo()));
        }

        return nodo;
    }


    public void borrarArbol() {
        nodoRaiz = null;
        peso = 0;
    }

}
