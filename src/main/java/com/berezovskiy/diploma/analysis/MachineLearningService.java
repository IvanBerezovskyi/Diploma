package com.berezovskiy.diploma.analysis;

import com.berezovskiy.diploma.data.entity.Accident;
import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.entity.Prediction;
import com.berezovskiy.diploma.data.entity.enums.AccidentSeverity;
import com.berezovskiy.diploma.data.repository.AccidentRepository;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.utils.FileUtils;
import com.berezovskiy.diploma.utils.MappingUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static com.berezovskiy.diploma.utils.FileUtils.writeAccidentSeverityModelsToArffFile;
import static com.berezovskiy.diploma.utils.FileUtils.writeCasualtiesNumberModelsToArffFile;

@Service
@AllArgsConstructor
public class MachineLearningService {

    private static final int ACCIDENT_SEVERITY_INDEX = 17;
    private static final int CASUALTIES_NUMBER_INDEX = 14;

    private AccidentRepository accidentRepository;
    private DatasetRepository datasetRepository;

    private static final String DATASET_LOCATION = System.getProperty("user.dir") + "/output/datasets/";
    private static final String PREDICTION_LOCATION = System.getProperty("user.dir") + "/output/predictions/";

    @SneakyThrows
    public void train(Long datasetId) {
        List<Accident> datasetAccidents = accidentRepository.findByDatasetId(datasetId);
        List<Model> datasetModels = MappingUtils.accidentsToModelList(datasetAccidents);

        byte [] accidentSecurityClassifierBytes = trainAndGetPredictionClassifier(datasetId, datasetModels, ACCIDENT_SEVERITY_INDEX);
        byte [] casualtiesNumberClassifierBytes = trainAndGetPredictionClassifier(datasetId, datasetModels, CASUALTIES_NUMBER_INDEX);

        Dataset dataset = datasetRepository.findById(datasetId).get();
        dataset.setTrainedOn(true);
        dataset.setAccidentSeverityPredictionClassifier(accidentSecurityClassifierBytes);
        dataset.setCasualtiesNumberPredictionClassifier(casualtiesNumberClassifierBytes);
        datasetRepository.save(dataset);
    }

    @SneakyThrows
    public Prediction predict(Prediction prediction) {
        Dataset dataset = prediction.getDataset();

        double casualtiesNumber = predictTargetClass(prediction, CASUALTIES_NUMBER_INDEX,
                dataset.getCasualtiesNumberPredictionClassifier());
        prediction.setCasualtiesNumber((int) Math.round(casualtiesNumber));
        double accidentSeverity = predictTargetClass(prediction, ACCIDENT_SEVERITY_INDEX,
                dataset.getAccidentSeverityPredictionClassifier());
        AccidentSeverity accidentSeverityClass = AccidentSeverity.values()[(int) Math.round(accidentSeverity)];
        prediction.setAccidentSeverity(accidentSeverityClass);

        System.out.println("Casualties number: " + casualtiesNumber);
        System.out.println("Accident severity: " + accidentSeverity);
        System.out.println("Accident severity class: " + prediction.getAccidentSeverity());

        return prediction;
    }

    private byte [] trainAndGetPredictionClassifier(Long datasetId, List<Model> models, int targetIndex) throws Exception {
        String arffFilename = targetIndex == ACCIDENT_SEVERITY_INDEX ?
                writeAccidentSeverityModelsToArffFile(DATASET_LOCATION, models, datasetId) :
                writeCasualtiesNumberModelsToArffFile(DATASET_LOCATION, models, datasetId);
        Instances dataSet = getDataSet(arffFilename, targetIndex);
        Classifier classifier = new NaiveBayes();
        classifier.buildClassifier(dataSet);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationHelper.write(outputStream, classifier);
        byte [] classifierByteArray = outputStream.toByteArray();
        outputStream.close();

        return classifierByteArray;
    }

    private double predictTargetClass(Prediction prediction, int targetIndex, byte [] classifierBytes) throws Exception {
        Long datasetId = prediction.getDataset().getId();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(classifierBytes);
        Classifier classifier = (Classifier) SerializationHelper.read(inputStream);

        Model model = MappingUtils.predictionToModel(prediction);
        String arffFilename = targetIndex == ACCIDENT_SEVERITY_INDEX ?
                FileUtils.writeAccidentSeverityModelsToArffFile(PREDICTION_LOCATION, List.of(model), datasetId) :
                FileUtils.writeCasualtiesNumberModelsToArffFile(PREDICTION_LOCATION, List.of(model), datasetId);
        Instance instance = getDataSet(arffFilename, targetIndex).firstInstance();
        return classifier.classifyInstance(instance);
    }

    private Instances getDataSet(String filename, int targetIndex) throws Exception {
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(filename));
        Instances dataSet = loader.getDataSet();
        dataSet.setClassIndex(targetIndex);
        return dataSet;
    }
}
