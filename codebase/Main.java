import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public final class Main {
    private static final String MENU = """
            1  Add contact             5  Update by phone
            2  Display contacts        6  Delete by phone
            3  Search by name          7  Sort directory by name
            4  Search by phone         8  Save and exit""";
    private static final String MISSING = "Contact not found.";

    private final Scanner input;
    private final PrintStream out;
    private final Path file;
    private ContactDirectory directory;

    Main(Scanner input, PrintStream out, Path file) {
        this.input = input;
        this.out = out;
        this.file = file;
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in, StandardCharsets.UTF_8)) {
            new Main(input, System.out, Path.of("contacts.txt")).run();
        }
    }

    void run() {
        try {
            directory = ContactFile.load(file);
        } catch (IOException ex) {
            out.println("Could not load contacts.txt. " + ex.getMessage());
            return;
        }
        out.println("Contact Manager with Fast Search");
        while (true) {
            out.println();
            out.println(MENU);
            try {
                switch (ask("Choice").strip()) {
                    case "1" -> add();
                    case "2" -> display();
                    case "3" -> searchByName();
                    case "4" -> searchByPhone();
                    case "5" -> update();
                    case "6" -> delete();
                    case "7" -> sort();
                    case "8" -> {
                        if (save()) {
                            return;
                        }
                    }
                    default -> out.println("Choose a number from 1 to 8.");
                }
            } catch (IllegalArgumentException ex) {
                out.println(ex.getMessage());
            } catch (NoSuchElementException ex) {
                // The input ended. Save the operations that finished.
                save();
                return;
            }
        }
    }

    private void add() {
        String name = ask("Name");
        String phone = ask("Phone");
        String email = ask("Email (Enter to skip)");
        Contact contact = directory.add(name, phone, email);
        out.println("Contact added: " + contact.summary());
    }

    private void display() {
        List<Contact> rows = directory.browseByName();
        for (Contact contact : rows) {
            out.println(contact.summary());
        }
        if (rows.isEmpty()) {
            out.println("Directory is empty.");
        }
    }

    private void searchByName() {
        List<Contact> matches = directory.findByName(ask("Full name"));
        if (matches.isEmpty()) {
            out.println(MISSING);
        }
        for (Contact contact : matches) {
            showDetails(contact);
        }
    }

    private void searchByPhone() {
        Contact contact = directory.findByPhone(ask("Phone"));
        if (contact == null) {
            out.println(MISSING);
        } else {
            showDetails(contact);
        }
    }

    private void showDetails(Contact contact) {
        out.println(contact.summary());
        out.println("Email: " + (contact.email().isEmpty() ? "(none)" : contact.email()));
    }

    private void update() {
        String oldPhone = ask("Current phone");
        Contact current = directory.findByPhone(oldPhone);
        if (current == null) {
            out.println(MISSING);
            return;
        }
        String name = ask("New name (Enter to keep)");
        String phone = ask("New phone (Enter to keep)");
        String email = ask("New email (Enter to keep, - to clear)");
        if (email.isEmpty()) {
            email = current.email();
        } else if (email.equals("-")) {
            email = "";
        }
        Contact updated = directory.update(oldPhone, name.isEmpty() ? current.name() : name,
                phone.isEmpty() ? oldPhone : phone, email);
        out.println("Contact updated: " + updated.summary());
    }

    private void delete() {
        Contact removed = directory.delete(ask("Phone to delete"));
        out.println(removed == null ? MISSING : "Contact deleted: " + removed.summary());
    }

    private void sort() {
        String order = ask("Order (A or Enter for ascending, D for descending)").strip();
        boolean descending = order.equalsIgnoreCase("D");
        if (!order.isEmpty() && !order.equalsIgnoreCase("A") && !descending) {
            throw new IllegalArgumentException("Choose A or D.");
        }
        List<Contact> rows = directory.sortedByName(descending);
        for (int i = 0; i < rows.size(); i++) {
            Contact contact = rows.get(i);
            out.println((i + 1) + ". " + contact.id() + " | " + contact.name());
        }
        if (rows.isEmpty()) {
            out.println("Directory is empty.");
        }
    }

    private boolean save() {
        try {
            ContactFile.save(file, directory);
            out.println("Contacts saved.");
            return true;
        } catch (IOException ex) {
            out.println("Save failed. " + ex.getMessage());
            return false;
        }
    }

    private String ask(String prompt) {
        out.println(prompt + ":");
        return input.nextLine();
    }
}
