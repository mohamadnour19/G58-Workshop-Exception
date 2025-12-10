package se.lexicon.exceptions.workshop.data_access;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.lexicon.exceptions.workshop.domain.Gender;
import se.lexicon.exceptions.workshop.domain.Person;
import se.lexicon.exceptions.workshop.fileIO.CSVReader_Writer;
import se.lexicon.exceptions.workshop.DuplicateNameException;

public class NameService {

    private List<String> maleFirstNames;
    private List<String> femaleFirstNames;
    private List<String> lastNames;
    private static Random random = new Random();

    public NameService(List<String> maleFirstNames, List<String> femaleFirstNames, List<String> lastNames) {
        if (maleFirstNames == null || femaleFirstNames == null || lastNames == null) {
            throw new IllegalArgumentException("Name lists cannot be null");
        }
        this.maleFirstNames = maleFirstNames;
        this.femaleFirstNames = femaleFirstNames;
        this.lastNames = lastNames;
    }

    public Person getNewRandomPerson() {
        Gender gender = getRandomGender();
        Person person = null;
        switch (gender) {
            case MALE:
                person = new Person(getRandomMaleFirstName(), getRandomLastName(), gender);
                break;
            case FEMALE:
                person = new Person(getRandomFemaleFirstName(), getRandomLastName(), gender);
                break;
        }
        return person;
    }

    public String getRandomFemaleFirstName() {
        if (femaleFirstNames.isEmpty()) {
            throw new IllegalStateException("No female first names available");
        }
        return femaleFirstNames.get(random.nextInt(femaleFirstNames.size()));
    }

    public String getRandomMaleFirstName() {
        if (maleFirstNames.isEmpty()) {
            throw new IllegalStateException("No male first names available");
        }
        return maleFirstNames.get(random.nextInt(maleFirstNames.size()));
    }

    public String getRandomLastName() {
        if (lastNames.isEmpty()) {
            throw new IllegalStateException("No last names available");
        }
        return lastNames.get(random.nextInt(lastNames.size()));
    }

    public Gender getRandomGender() {
        return random.nextInt(100) > 50 ? Gender.FEMALE : Gender.MALE;
    }

    /**
     * Here you need to check if List<String> femaleFirstNames already contains the name
     * If name already exists throw a new custom exception you will have to create called
     * DuplicateNameException.
     * @param name
     */
    public void addFemaleFirstName(String name) throws DuplicateNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String trimmedName = name.trim();
        if (femaleFirstNames.contains(trimmedName)) {
            throw new DuplicateNameException("Female first name '" + trimmedName + "' already exists!");
        }

        femaleFirstNames.add(trimmedName);
        try {
            CSVReader_Writer.saveFemaleNames(femaleFirstNames);
            System.out.println("Added female name: " + trimmedName);
        } catch (Exception e) {
            femaleFirstNames.remove(trimmedName);
            throw new RuntimeException("Failed to save female names to file", e);
        }
    }

    /**
     * Here you need to check if List<String> maleFirstNames already contains the name
     * If name already exists throw a new custom exception you will have to create called
     * DuplicateNameException.
     * @param name
     */
    public void addMaleFirstName(String name) throws DuplicateNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String trimmedName = name.trim();
        if (maleFirstNames.contains(trimmedName)) {
            throw new DuplicateNameException("Male first name '" + trimmedName + "' already exists!");
        }

        maleFirstNames.add(trimmedName);
        try {
            CSVReader_Writer.saveMaleNames(maleFirstNames);
            System.out.println("Added male name: " + trimmedName);
        } catch (Exception e) {
            maleFirstNames.remove(trimmedName);
            throw new RuntimeException("Failed to save male names to file", e);
        }
    }

    /**
     * Here you need to check if List<String> lastNames already contains the name
     * If name already exists throw a new custom exception you will have to create called
     * DuplicateNameException.
     * @param lastName
     */
    public void addLastName(String lastName) throws DuplicateNameException {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        String trimmedName = lastName.trim();
        if (lastNames.contains(trimmedName)) {
            throw new DuplicateNameException("Last name '" + trimmedName + "' already exists!");
        }

        lastNames.add(trimmedName);
        try {
            CSVReader_Writer.saveLastNames(lastNames);
            System.out.println("Added last name: " + trimmedName);
        } catch (Exception e) {
            lastNames.remove(trimmedName);
            throw new RuntimeException("Failed to save last names to file", e);
        }
    }

    public List<String> getMaleFirstNames() {
        return new ArrayList<>(maleFirstNames);
    }

    public List<String> getFemaleFirstNames() {
        return new ArrayList<>(femaleFirstNames);
    }

    public List<String> getLastNames() {
        return new ArrayList<>(lastNames);
    }

    public void addMultipleFemaleNames(List<String> names) throws DuplicateNameException {
        for (String name : names) {
            addFemaleFirstName(name);
        }
    }

    public void addMultipleMaleNames(List<String> names) throws DuplicateNameException {
        for (String name : names) {
            addMaleFirstName(name);
        }
    }

    public void addMultipleLastNames(List<String> names) throws DuplicateNameException {
        for (String name : names) {
            addLastName(name);
        }
    }
}