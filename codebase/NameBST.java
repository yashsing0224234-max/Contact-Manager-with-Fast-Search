import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

final class NameBST {
    private static final class Node {
        Contact contact;
        Node left;
        Node right;

        Node(Contact contact) {
            this.contact = contact;
        }
    }

    private Node root;

    void insert(Contact contact) {
        Node parent = null;
        Node current = root;
        int order = 0;
        while (current != null) {
            parent = current;
            order = Contact.compareByName(contact, current.contact);
            current = order < 0 ? current.left : current.right;
        }
        Node node = new Node(contact);
        if (parent == null) {
            root = node;
        } else if (order < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
    }

    void delete(Contact contact) {
        Node parent = null;
        Node current = root;
        while (current != null) {
            int order = Contact.compareByName(contact, current.contact);
            if (order == 0) {
                break;
            }
            parent = current;
            current = order < 0 ? current.left : current.right;
        }
        if (current == null) {
            return;
        }

        // With two children, take the smallest key from the right side.
        if (current.left != null && current.right != null) {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.contact = successor.contact;
            parent = successorParent;
            current = successor;
        }
        Node child = current.left != null ? current.left : current.right;
        if (parent == null) {
            root = child;
        } else if (parent.left == current) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    List<Contact> search(String name) {
        Deque<Node> path = new ArrayDeque<>();
        Node current = root;
        // Start at the first possible match, even when several names are equal.
        while (current != null) {
            if (current.contact.name().compareToIgnoreCase(name) < 0) {
                current = current.right;
            } else {
                path.push(current);
                current = current.left;
            }
        }
        return readInOrder(path, name);
    }

    List<Contact> inorder() {
        Deque<Node> path = new ArrayDeque<>();
        pushLeft(root, path);
        return readInOrder(path, null);
    }

    private static List<Contact> readInOrder(Deque<Node> path, String wantedName) {
        List<Contact> rows = new ArrayList<>();
        while (!path.isEmpty()) {
            Node node = path.pop();
            if (wantedName != null && !node.contact.name().equalsIgnoreCase(wantedName)) {
                break;
            }
            rows.add(node.contact);
            pushLeft(node.right, path);
        }
        return rows;
    }

    private static void pushLeft(Node node, Deque<Node> path) {
        while (node != null) {
            path.push(node);
            node = node.left;
        }
    }
}
