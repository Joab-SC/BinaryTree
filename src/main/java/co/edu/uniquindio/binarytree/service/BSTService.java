package co.edu.uniquindio.binarytree.service;

import co.edu.uniquindio.binarytree.model.Node;
import co.edu.uniquindio.binarytree.model.Step;
import co.edu.uniquindio.binarytree.model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BSTService {


    private Node root = null;


    public List<Step> insert(int value) {
        List<Step> steps = new ArrayList<>();

        if (root == null) {
            // Árbol vacío: insertar directamente como raíz
            root = new Node(value);
            steps.add(new Step(
                "inserted",
                value,
                "El árbol estaba vacío. " + value + " es ahora la raíz.",
                TreeNode.fromNode(root)
            ));
            return steps;
        }

        // Revisar si el valor ya existe
        if (contains(root, value)) {
            steps.add(new Step(
                "duplicate",
                value,
                "El valor " + value + " ya existe en el árbol. No se insertan duplicados.",
                TreeNode.fromNode(root)
            ));
            return steps;
        }

        // Insertar con pasos
        root = insertWithSteps(root, value, steps);
        return steps;
    }

    /**
     * Inserta recursivamente y va guardando cada decisión como un Step.
     */
    private Node insertWithSteps(Node current, int value, List<Step> steps) {
        if (current == null) {
            // Llegamos al lugar correcto: insertar aquí
            Node newNode = new Node(value);
            steps.add(new Step(
                "inserted",
                value,
                "Posición encontrada. Insertando " + value + " aquí.",
                TreeNode.fromNode(root)  // snapshot ANTES de insertar visualmente
            ));
            return newNode;
        }

        // Paso: estamos comparando con este nodo
        steps.add(new Step(
            "comparing",
            current.value,
            "Comparando " + value + " con " + current.value + "...",
            TreeNode.fromNode(root)
        ));

        if (value < current.value) {
            // Va a la izquierda
            steps.add(new Step(
                "go_left",
                current.value,
                value + " < " + current.value + " → vamos a la izquierda.",
                TreeNode.fromNode(root)
            ));
            current.left = insertWithSteps(current.left, value, steps);

        } else {
            // Va a la derecha
            steps.add(new Step(
                "go_right",
                current.value,
                value + " > " + current.value + " → vamos a la derecha.",
                TreeNode.fromNode(root)
            ));
            current.right = insertWithSteps(current.right, value, steps);
        }

        return current;
    }

    // =========================================================
    // ELIMINACIÓN
    // =========================================================

    /**
     * Elimina un valor del BST y retorna la lista de pasos para animar.
     * La eliminación tiene 3 casos:
     *   1. Nodo hoja (sin hijos) → simplemente se elimina
     *   2. Nodo con un hijo → se reemplaza con ese hijo
     *   3. Nodo con dos hijos → se reemplaza con el sucesor inorden (mínimo del subárbol derecho)
     */
    public List<Step> delete(int value) {
        List<Step> steps = new ArrayList<>();

        if (root == null) {
            steps.add(new Step(
                "not_found",
                null,
                "El árbol está vacío. No hay nada que eliminar.",
                null
            ));
            return steps;
        }

        if (!contains(root, value)) {
            steps.add(new Step(
                "not_found",
                null,
                "El valor " + value + " no existe en el árbol.",
                TreeNode.fromNode(root)
            ));
            return steps;
        }

        // Primer paso: buscar el nodo
        steps.add(new Step(
            "searching",
            null,
            "Buscando el nodo con valor " + value + " para eliminarlo...",
            TreeNode.fromNode(root)
        ));

        root = deleteWithSteps(root, value, steps);

        // Paso final: árbol actualizado
        steps.add(new Step(
            "deleted",
            value,
            "Eliminación completada. Árbol actualizado.",
            TreeNode.fromNode(root)
        ));

        return steps;
    }



    private Node deleteWithSteps(Node current, int value, List<Step> steps) {
        if (current == null) return null;

        // Paso: comparando con nodo actual
        steps.add(new Step(
            "comparing",
            current.value,
            "Comparando con " + current.value + "...",
            TreeNode.fromNode(root)
        ));

        if (value < current.value) {
            steps.add(new Step(
                "go_left",
                current.value,
                value + " < " + current.value + " → buscamos a la izquierda.",
                TreeNode.fromNode(root)
            ));
            current.left = deleteWithSteps(current.left, value, steps);

        } else if (value > current.value) {
            steps.add(new Step(
                "go_right",
                current.value,
                value + " > " + current.value + " → buscamos a la derecha.",
                TreeNode.fromNode(root)
            ));
            current.right = deleteWithSteps(current.right, value, steps);

        } else {
            // ¡Encontramos el nodo a eliminar!
            steps.add(new Step(
                "found",
                current.value,
                "¡Nodo " + current.value + " encontrado! Determinando caso de eliminación...",
                TreeNode.fromNode(root)
            ));

            // CASO 1: Nodo hoja (sin hijos)
            if (current.left == null && current.right == null) {
                steps.add(new Step(
                    "case_leaf",
                    current.value,
                    "Caso 1: El nodo " + current.value + " es una hoja (sin hijos). Se elimina directamente.",
                    TreeNode.fromNode(root)
                ));
                return null;
            }

            // CASO 2a: Solo tiene hijo derecho
            if (current.left == null) {
                steps.add(new Step(
                    "case_one_child",
                    current.value,
                    "Caso 2: El nodo solo tiene hijo derecho (" + current.right.value + "). Se reemplaza con él.",
                    TreeNode.fromNode(root)
                ));
                return current.right;
            }

            // CASO 2b: Solo tiene hijo izquierdo
            if (current.right == null) {
                steps.add(new Step(
                    "case_one_child",
                    current.value,
                    "Caso 2: El nodo solo tiene hijo izquierdo (" + current.left.value + "). Se reemplaza con él.",
                    TreeNode.fromNode(root)
                ));
                return current.left;
            }

            // CASO 3: Tiene dos hijos → buscar el sucesor inorden
            // (el nodo más pequeño del subárbol derecho)
            Node successor = findMin(current.right);
            steps.add(new Step(
                "case_two_children",
                current.value,
                "Caso 3: El nodo tiene dos hijos. El sucesor inorden es " + successor.value + ". Se reemplaza el valor y se elimina el sucesor.",
                TreeNode.fromNode(root)
            ));


            current.value = successor.value;


            current.right = deleteWithSteps(current.right, successor.value, steps);
        }

        return current;
    }



    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    private boolean contains(Node node, int value) {
        if (node == null) return false;
        if (node.value == value) return true;
        if (value < node.value) return contains(node.left, value);
        return contains(node.right, value);
    }


    public TreeNode getTree() {
        return TreeNode.fromNode(root);
    }

    /**
     * Limpia el árbol completamente.
     */
    public void reset() {
        root = null;
    }
}
