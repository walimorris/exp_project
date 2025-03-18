public class Occupant implements Comparable<Occupant> {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final Address address;

    public Occupant(String firstName, String lastName, int age, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
    }

    /**
     * Compares this occupant with another occupant for ordering.
     *
     * <p>Sorting is done by last name. If the last names are the same,
     * comparison goes to first name ordering.</p>
     * {@code Note: zero returns will not be added to the tree - it's a duplicate!}
     *
     * @param o the {@link Occupant} to compare.
     * @return a negative integer if this occupant comes before the other,
     *         a positive integer if it comes after,
     *         or zero if both have the same last and first names.
     */
    @Override
    public int compareTo(Occupant o) {
        int lastNameCompare = this.lastName.compareTo(o.lastName);
        int firstnameCompare = this.firstName.compareTo(o.firstName);
        if (lastNameCompare != 0) {
            return lastNameCompare;
        }
        return firstnameCompare;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return firstName + ", " +
                lastName + ", " +
                address + ", " +
                age;
    }
}
