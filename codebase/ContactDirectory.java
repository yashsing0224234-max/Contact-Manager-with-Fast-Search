import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

final class ContactDirectory {
    private static final Pattern PHONE = Pattern.compile("[0-9]{10}");
    private static final Pattern EMAIL = Pattern.compile("[^\\s@]+@[^\\s@.]+(?:\\.[^\\s@.]+)+");

    private final ContactList contacts = new ContactList();
    private final NameBST names = new NameBST();
    private final HashMap<String, Contact> phones = new HashMap<>();
    private int highestId = 100;

    Contact add(String name, String phone, String email) {
        if (highestId == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No contact IDs are available.");
        }
        return addWithId(highestId + 1, name, phone, email);
    }

    // The file loader checks saved IDs before calling this.
    Contact addWithId(int id, String name, String phone, String email) {
        name = cleanName(name);
        email = cleanEmail(email);
        checkPhone(phone);
        checkAvailable(phone, null);
        Contact contact = new Contact(id, name, phone, email);
        contacts.append(contact);
        names.insert(contact);
        phones.put(phone, contact);
        highestId = Math.max(highestId, id);
        return contact;
    }

    Contact findByPhone(String phone) {
        checkPhone(phone);
        return phones.get(phone);
    }

    List<Contact> findByName(String name) {
        return names.search(cleanName(name));
    }

    Contact update(String oldPhone, String name, String phone, String email) {
        Contact contact = findByPhone(oldPhone);
        if (contact == null) {
            return null;
        }
        name = cleanName(name);
        email = cleanEmail(email);
        checkPhone(phone);
        checkAvailable(phone, contact);

        boolean nameChanged = !contact.name().equals(name);
        boolean phoneChanged = !oldPhone.equals(phone);
        if (nameChanged) {
            names.delete(contact);
        }
        if (phoneChanged) {
            phones.remove(oldPhone);
        }
        contact.setDetails(name, phone, email);
        if (nameChanged) {
            names.insert(contact);
        }
        if (phoneChanged) {
            phones.put(phone, contact);
        }
        return contact;
    }

    Contact delete(String phone) {
        Contact contact = findByPhone(phone);
        if (contact != null) {
            contacts.remove(contact);
            names.delete(contact);
            phones.remove(phone);
        }
        return contact;
    }

    List<Contact> browseByName() {
        return names.inorder();
    }

    List<Contact> inStorageOrder() {
        return contacts.snapshot();
    }

    List<Contact> sortedByName(boolean descending) {
        List<Contact> rows = contacts.snapshot();
        // Insert each row into the sorted part on its left.
        for (int i = 1; i < rows.size(); i++) {
            Contact next = rows.get(i);
            int j = i - 1;
            while (j >= 0) {
                int order = Contact.compareByName(rows.get(j), next);
                if (descending ? order >= 0 : order <= 0) {
                    break;
                }
                rows.set(j + 1, rows.get(j));
                j--;
            }
            rows.set(j + 1, next);
        }
        return rows;
    }

    private static String cleanName(String name) {
        name = name.strip();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (name.indexOf('\n') >= 0 || name.indexOf('\r') >= 0) {
            throw new IllegalArgumentException("Enter the name on one line.");
        }
        return name;
    }

    private static String cleanEmail(String email) {
        email = email.strip();
        if (!email.isEmpty() && !EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Enter an email like name@example.com.");
        }
        return email;
    }

    private static void checkPhone(String phone) {
        if (!PHONE.matcher(phone).matches()) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        }
    }

    private void checkAvailable(String phone, Contact owner) {
        Contact existing = phones.get(phone);
        if (existing != null && existing != owner) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
    }
}
