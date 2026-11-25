import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public final class ProjectDemo {
    public static void main(String[] args) throws IOException {
        Path folder = Files.createTempDirectory("contact-demo-");
        Path file = folder.resolve("contacts.txt");
        try {
            check("Case 1 - add Aman",
                    "Contact added: C101 | Aman | 9876543210",
                    action(file, "1", "Aman", "9876543210", ""));

            check("Case 2 - display the only contact",
                    "C101 | Aman | 9876543210", action(file, "2"));

            check("Case 3 - search for a missing phone",
                    "Contact not found.", action(file, "4", "9000000000"));

            String added = action(file, "1", "Riya", "9123456780", "riya@example.com");
            String ascending = action(file, "7", "");
            String descending = action(file, "7", "D");
            check("Case 4 - add Riya and sort",
                    """
                    Contact added: C102 | Riya | 9123456780
                    Ascending (default)
                    1. C101 | Aman
                    2. C102 | Riya
                    Descending
                    1. C102 | Riya
                    2. C101 | Aman""",
                    added + "\nAscending (default)\n" + ascending + "\nDescending\n" + descending);

            System.out.println("4 of 4 cases passed.");
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(folder);
        }
    }

    private static String action(Path file, String... answers) {
        // Run one menu action, then save. Each run uses the same temporary file.
        String keys = String.join("\n", answers) + "\n8\n";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (Scanner input = new Scanner(keys);
             PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            new Main(input, output, file).run();
        }
        List<String> lines = bytes.toString(StandardCharsets.UTF_8).lines().toList();
        if (!lines.contains("Contacts saved.")) {
            throw new AssertionError("Menu session failed: " + lines);
        }
        int start = lines.indexOf("Choice:") + 1;
        if (start == 0) {
            throw new AssertionError("Menu did not open.");
        }
        while (start < lines.size() && lines.get(start).endsWith(":")) {
            start++;
        }
        int end = start;
        while (end < lines.size() && !lines.get(end).isEmpty()) {
            end++;
        }
        return String.join("\n", lines.subList(start, end));
    }

    private static void check(String label, String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + "\nExpected:\n" + expected + "\nActual:\n" + actual);
        }
        System.out.println(label);
        System.out.println(actual);
        System.out.println();
    }
}
