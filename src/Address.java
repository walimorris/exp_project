import java.util.Objects;

public class Address {
    private final String streetNumberName;
    private final String state;
    private final String city;

    public Address(String streetNumberName, String state, String city) {
        this.streetNumberName = streetNumberName;
        this.state = state;
        this.city = city;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return Objects.equals(this.streetNumberName, address.streetNumberName) &&
                Objects.equals(this.state, address.state) &&
                Objects.equals(this.city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.streetNumberName, this.state, this.city);
    }

    @Override
    public String toString() {
        return  streetNumberName + ", " +
                state + ", " +
                city;
    }
}
