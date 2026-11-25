import java.util.ArrayList;
import java.util.List;

final class ContactList {
    private static final class Node {
        final Contact contact;
        Node next;

        Node(Contact contact) {
            this.contact = contact;
        }
    }

    private Node head;
    private Node tail;

    void append(Contact contact) {
        Node node = new Node(contact);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    void remove(Contact contact) {
        Node previous = null;
        Node current = head;
        while (current != null && current.contact != contact) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            return;
        }
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
        if (current == tail) {
            tail = previous;
        }
    }

    List<Contact> snapshot() {
        List<Contact> rows = new ArrayList<>();
        for (Node node = head; node != null; node = node.next) {
            rows.add(node.contact);
        }
        return rows;
    }
}
