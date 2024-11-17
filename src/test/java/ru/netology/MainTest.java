package ru.netology;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void parseCSV() throws Exception {
        Path tempFile = Files.createTempFile("test", ".csv");
        Files.writeString(tempFile, "1,John,Smith,USA,25\n, 2,Inav,Petrov,RU,23\n");

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age" };

        List<Employee> employees = Main.parseCSV(columnMapping, tempFile.toString());
        assertNotNull(employees);

        Employee firstEmployee = employees.get(0);
        assertEquals(1, firstEmployee.id);
        assertEquals("John", firstEmployee.firstName);
        assertEquals("Smith", firstEmployee.lastName);
        assertEquals("USA", firstEmployee.country);
        assertEquals(25, firstEmployee.age);
    }

    @Test
    void listToJson() {
        List<Employee> employees = List.of(
                new Employee(1L, "John", "Smith", "USA", 25),
                new Employee(2L, "Ivan", "Petrov", "RU", 23)
        );
        String json = Main.listToJson(employees);

        String expectedJson = "[" +
                "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                "{\"id\":2,\"firstName\":\"Ivan\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}" +
                "]";

        assertNotNull(json); // Убедимся, что JSON не null
        assertEquals(expectedJson, json); // Проверяем соответствие
    }

    @Test
    void writeString() throws Exception {
        List<Employee> employees = List.of(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Ivan", "Petrov", "RU", 23)
        );

        Path tempJson = Files.createTempFile("test-employees", ".json");

        Main.writeString(employees);

        String actualJson = Files.readString(Path.of("data.json"));
        assertTrue(actualJson.contains("\"firstName\":\"John\""));
        assertTrue(actualJson.contains("\"country\":\"RU\""));

        Files.deleteIfExists(tempJson);
    }
}