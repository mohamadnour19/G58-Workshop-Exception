package se.lexicon.exceptions.workshop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import se.lexicon.exceptions.workshop.data_access.NameService;
import se.lexicon.exceptions.workshop.domain.Person;
import se.lexicon.exceptions.workshop.DuplicateNameException;
import se.lexicon.exceptions.workshop.fileIO.CSVReader_Writer;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("=== Starting Name Service Application ===\n");

            System.out.println("1. Reading names from files...");
            List<String> maleFirstNames = CSVReader_Writer.getMaleFirstNames();
            List<String> femaleFirstNames = CSVReader_Writer.getFemaleFirstNames();

            List<String> lastNames = null;
            try {
                lastNames = CSVReader_Writer.getLastNames();
            } catch (IOException e) {
                System.err.println("Failed to read last names: " + e.getMessage());
                lastNames = Arrays.asList("Andersson", "Johansson", "Karlsson");
            }

            System.out.println("   Male names found: " + maleFirstNames.size());
            System.out.println("   Female names found: " + femaleFirstNames.size());
            System.out.println("   Last names found: " + lastNames.size() + "\n");

            NameService nameService = new NameService(maleFirstNames, femaleFirstNames, lastNames);

            System.out.println("2. Creating random persons...");
            for (int i = 0; i < 5; i++) {
                Person randomPerson = nameService.getNewRandomPerson();
                System.out.println("   Person " + (i + 1) + ": " + randomPerson);
            }
            System.out.println();

            System.out.println("3. Testing name addition...");

            try {
                if (!lastNames.isEmpty()) {
                    nameService.addLastName(lastNames.get(0));
                }
            } catch (DuplicateNameException e) {
                System.out.println("   ✓ Test 1 passed: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("   ✗ Test 1 failed: " + e.getMessage());
            }

            try {
                nameService.addFemaleFirstName("Sofia");
                System.out.println("   ✓ Test 2 passed: Added new female name");
            } catch (DuplicateNameException e) {
                System.out.println("   Test 2: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("   ✗ Test 2 failed: " + e.getMessage());
            }

            try {
                nameService.addFemaleFirstName("Sofia");
            } catch (DuplicateNameException e) {
                System.out.println("   ✓ Test 3 passed: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("   ✗ Test 3 failed: " + e.getMessage());
            }

            try {
                nameService.addMaleFirstName("");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ Test 4 passed: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("   ✗ Test 4 failed: " + e.getMessage());
            }

            System.out.println("\n4. Creating more random persons with new names...");
            for (int i = 0; i < 3; i++) {
                Person randomPerson = nameService.getNewRandomPerson();
                System.out.println("   Person " + (i + 1) + ": " + randomPerson);
            }

            System.out.println("\n=== Final Status ===");
            System.out.println("Male names: " + nameService.getMaleFirstNames().size());
            System.out.println("Female names: " + nameService.getFemaleFirstNames().size());
            System.out.println("Last names: " + nameService.getLastNames().size());
            System.out.println("Application completed successfully!");

        } catch (Exception e) {
            System.err.println("\n=== CRITICAL ERROR ===");
            System.err.println("The application encountered an unexpected error:");
            e.printStackTrace();
            System.err.println("\nApplication terminated.");
        }
    }
}