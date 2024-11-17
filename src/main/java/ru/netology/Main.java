package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvBeanIntrospectionException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.writeString;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age" };
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        writeString(list);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .withThrowExceptions(false) // Продолжать парсинг при ошибках
                    .build();

            employees = csvToBean.parse();

            // Лог ошибок
            csvToBean.getCapturedExceptions().forEach(exception -> {
                System.err.println("Ошибка в строке: " + exception.getLineNumber() + " - " + exception.getMessage());
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }


    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(List<Employee> list) throws CsvBeanIntrospectionException {
        String json = listToJson(list);
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(String.valueOf(json));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
