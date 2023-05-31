package com.berezovskiy.diploma.utils;

import com.berezovskiy.diploma.analysis.Model;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class FileUtils {

    private FileUtils() {}

    @SneakyThrows
    public static String writeAccidentSeverityModelsToArffFile(String location, List<Model> models, Long datasetId){
        String filename = location + "dataset_" + datasetId + "_accident_severity.arff";
        File file = new File(Thread.currentThread().getContextClassLoader()
                .getResource("arff_template_predict_accident_severity.txt").getFile());
        String arffTemplate = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(arffTemplate);
            for (int i = 0; i < models.size() - 1; i++) {
                fileWriter.write(models.get(i).toString() + System.lineSeparator());
            }
            fileWriter.write(models.get(models.size()-1).toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filename;
    }

    @SneakyThrows
    public static String writeCasualtiesNumberModelsToArffFile(String location, List<Model> models, Long datasetId){
        String filename = location + "dataset_" + datasetId + "_casualties_number.arff";
        File file = new File(Thread.currentThread().getContextClassLoader()
                .getResource("arff_template_predict_casualties_number.txt").getFile());
        String arffTemplate = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(arffTemplate);
            for (int i = 0; i < models.size() - 1; i++) {
                fileWriter.write(models.get(i).toStringWithoutAccidentSeverity() + System.lineSeparator());
            }
            fileWriter.write(models.get(models.size()-1).toStringWithoutAccidentSeverity());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filename;
    }
}