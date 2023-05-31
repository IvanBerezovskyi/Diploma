package com.berezovskiy.diploma.import_export;

import com.berezovskiy.diploma.data.entity.Accident;
import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.entity.User;
import com.berezovskiy.diploma.data.entity.exception.NoTargetVariableProvidedException;
import com.berezovskiy.diploma.data.repository.AccidentRepository;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportService {

    private final AccidentRepository accidentRepository;
    private final DatasetRepository datasetRepository;

    public void importAccidents(Long datasetId, InputStream fileData) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileData));
        List<Accident> accidents = new LinkedList<>();
        var dataset = datasetRepository.findById(datasetId).get();
        int rowsSkipped = 0;
        while(reader.ready()) {
            try {
                String accidentString = reader.readLine();
                var accident = new Accident(accidentString);
                accident.setDataset(dataset);
                accidents.add(accident);
            } catch (NoTargetVariableProvidedException e) {
                rowsSkipped++;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Rows skipped: " + rowsSkipped);
        reader.close();
        accidentRepository.saveAll(accidents);
    }

    public void importDefaultDataset(User user) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("embedded_dataset.csv")) {
            Dataset dataset = Dataset.builder()
                    .title("Вбудований датасет")
                    .description("Складається з 10000+ аварій")
                    .user(user)
                    .build();
            Dataset savedDataset = datasetRepository.save(dataset);
            importAccidents(savedDataset.getId(), is);
        }
    }
}
