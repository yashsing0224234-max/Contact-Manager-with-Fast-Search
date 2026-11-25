public final class Contact {
    private final int id;
    private String name;
    private String phone;
    private String email;

    Contact(int id, String name, String phone, String email) {
        this.id = id;
        setDetails(name, phone, email);
    }

    public String id() {
        return "C" + id;
    }

    public String name() {
        return name;
    }

    public String phone() {
        return phone;
    }

    public String email() {
        return email;
    }

    // The directory calls this after checking the new details.
    void setDetails(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    static int compareByName(Contact first, Contact second) {
        int order = first.name.compareToIgnoreCase(second.name);
        if (order != 0) {
            return order;
        }
        return Integer.compare(first.id, second.id);
    }

    public String summary() {
        return id() + " | " + name + " | " + phone;
    }
}
