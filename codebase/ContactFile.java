import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class ContactFile {
    static ContactDirectory load(Path file) throws IOException {
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
        ContactDirectory directory = new ContactDirectory();
        Set<Integer> savedIds = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    List<String> fields = readFields(line);
                    if (fields.size() != 4) {
                        throw new IllegalArgumentException("Expected id,name,phone,email.");
                    }
                    int id = readId(fields.get(0));
                    if (!savedIds.add(id)) {
                        throw new IllegalArgumentException("Repeated ID " + fields.get(0) + ".");
                    }
                    directory.addWithId(id, fields.get(1), fields.get(2), fields.get(3));
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Line " + lineNumber + ": " + ex.getMessage(), ex);
                }
            }
        }
        return directory;
    }

    private static int readId(String text) {
        if (text.matches("C[1-9][0-9]*")) {
            try {
                int id = Integer.parseInt(text.substring(1));
                if (id >= 101) {
                    return id;
                }
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("ID is too large.");
            }
        }
        throw new IllegalArgumentException("Expected an ID such as C101.");
    }

    static void save(Path file, ContactDirectory directory) throws IOException {
        Path target = file.toAbsolutePath();
        Path temp = Files.createTempFile(target.getParent(), "contacts-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                for (Contact contact : directory.inStorageOrder()) {
                    writer.write(String.join(",", contact.id(), quote(contact.name()),
                            contact.phone(), quote(contact.email())));
                    writer.newLine();
                }
            }
            // Finish the new file before replacing the old one.
            try {
                Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private static String quote(String value) {
        if (value.indexOf(',') < 0 && value.indexOf('"') < 0) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static List<String> readFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        boolean closedQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch != '"') {
                    field.append(ch);
                } else if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = false;
                    closedQuote = true;
                }
            } else if (ch == ',') {
                fields.add(field.toString());
                field.setLength(0);
                closedQuote = false;
            } else if (ch == '"' && field.length() == 0 && !closedQuote) {
                inQuotes = true;
            } else if (ch == '"' || closedQuote) {
                throw new IllegalArgumentException("Unexpected text around a quote.");
            } else {
                field.append(ch);
            }
        }
        if (inQuotes) {
            throw new IllegalArgumentException("Missing closing quote.");
        }
        fields.add(field.toString());
        return fields;
    }
}
