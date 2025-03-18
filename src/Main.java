import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String fileInput;

        if (args.length > 0) {
            fileInput = args[0];
        } else {
            System.out.print("Enter input file path: ");
            Scanner scanner = new Scanner(System.in);
            fileInput = scanner.nextLine().trim();
            scanner.close();
        }
        List<String> input = readInput(fileInput);

        // each map key is an Address with a TreeSet value containing Occupants
        HashMap<Address, TreeSet<Occupant>> output = processInput(input);
        showOutput(output);
    }

    /**
     * Reads in an input file and processes line by line.
     *
     * @param fileName input file
     *
     * @return {@link List<String>} containing each individual input line
     */
    private static List<String> readInput(String fileName) {
        File file = new File(fileName);
        List<String> lines = new ArrayList<>();
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }
        if (file.canRead()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException ioException) {
                throw new RuntimeException(ioException.getMessage());
            }
        }
        return lines;
    }

    /**
     * Processes input, breaks it into parts, standardizes and normalizes the parts. Initializes
     * {@link Address} and {@link Occupant} objects based on these input parts to store for later
     * retrieval and processing.
     *
     * @param input {@link List} of each line in input
     *
     * @return {@link HashMap}
     */
    private static HashMap<Address, TreeSet<Occupant>> processInput(List<String> input) {
        HashMap<Address, TreeSet<Occupant>> output = new HashMap<>();
        for (String line : input) {
            String[] dataParts = getDataParts(line);
            normalize(dataParts);

            // data parts return [firstName, lastName, streetNumberName, city, State, age]
            Address address = new Address(dataParts[2], dataParts[3], dataParts[4]);
            Occupant occupant = new Occupant(dataParts[0], dataParts[1], Integer.parseInt(dataParts[5]), address);

            // query is by address, so we store unique address and map to occupant
            if (output.containsKey(address)) {
                output.get(address).add(occupant);
            } else {
                output.put(address, new TreeSet<>(Set.of(occupant)));
            }
        }
        return output;
    }

    /**
     * Iterates on the output and prints to the console each household and number of occupants,
     * followed by each firstname, lastname, address and age sorted by lastname then firstname
     * where the occupant(s) is older > 18.
     * <p>
     * {@code Note: Each address will be printed regardless and the following {@link Occupant} data
     * may not follow if it does not meet the requirement: {@link Occupant#getAge()} > 18}
     *
     * @param output {@link HashMap}
     *
     * @see Occupant
     * @see Address
     */
    private static void showOutput(HashMap<Address, TreeSet<Occupant>> output) {
        for (Map.Entry<Address, TreeSet<Occupant>> entry : output.entrySet()) {
            Address address = entry.getKey();
            TreeSet<Occupant> occupants = entry.getValue();

            System.out.println("Address: " + address + " - Occupants: " + occupants.size());

            for (Occupant occupant : occupants) {
                if (occupant.getAge() > 18) {
                    System.out.println(occupant);
                }
            }
            System.out.println();
        }
    }

    /**
     * Extracts data fields from a given input line by matching quoted values that represents
     * an individual field on the line.
     *
     * <p>Input strings are similar to CSV files where each value/field is wrapped in double quotes.
     * These values are extracted and returned in a String[]</p>
     *
     * <p>Example:
     * <pre>
     *   String line = '\"Eve","Smith","234 2nd Ave.","Tacoma","WA","25"\';
     *   String[] parts = getDataParts(line);
     *   // Output: ["Eve", "Smith", "234 2nd Ave.", "Tacoma", "WA", "25"]
     * </pre>
     * </p>
     *
     * @param line the input string
     *             .
     * @return String[] array of extracted fields
     */

    private static String[] getDataParts(String line) {
        List<String> parts = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            parts.add(matcher.group(1));
        }
        return parts.toArray(new String[0]);
    }

    /**
     * Normalize and standardize parts from the input data.
     * <p>
     * Processes include:
     * <ul>
     *     <li>Standardize address</li>
     *     <li>Capitalize Names, Cities, Addresses</li>
     *     <li>Uppercase State</li>
     *     <li>Trim whitespace or trailing characters</li>
     * </ul>
     * </p>
     *
     * @param parts String[]
     */
    private static void normalize(String[] parts) {
        parts[0] = capitalize(parts[0]).trim();
        parts[1] = capitalize(parts[1]).trim();
        parts[2] = capitalizeEachAddressPart(parts[2])
                // standardize addresses here, remove any trailing commas or periods
                // standardize address marks. Blvd., is accounted for, but what about
                // other types with second address
                .replace(",", "")
                .replace(".", "")
                .replaceAll("\\b(St|st|street)\\b", "St.")
                .replaceAll("\\b(Blvd|blvd|boulevard)\\b", "Blvd.,")
                .replaceAll("\\b(Ave|ave|avenue)\\b", "Ave.")
                .replaceAll("\\b(Apt|apt|apartment)\\b", "Apt.")
                .trim();
        parts[3] = capitalize(parts[3]).trim();
        parts[4] = parts[4].toUpperCase().trim();
    }

    /**
     * Capitalizes each word in the given address part.
     *
     * <p>Input string in split by spaces, capitalizes each word,
     * and joins them back into a string.</p>
     *
     * @param input the {@link String} containing multiple address parts
     *
     * @return {@link String} with each part capitalized
     */
    private static String capitalizeEachAddressPart(String input) {
        String[] parts = input.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = capitalize(parts[i]);
        }
        return String.join(" ", parts);
    }

    /**
     * Capitalize the given string.
     *
     * @param str {@link String}
     *
     * @return {@link String}
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}