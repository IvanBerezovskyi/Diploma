package com.berezovskiy.diploma.views.predictions;

import com.berezovskiy.diploma.analysis.MachineLearningService;
import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.entity.Prediction;
import com.berezovskiy.diploma.data.entity.enums.*;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.data.repository.PredictionRepository;
import com.berezovskiy.diploma.security.AuthenticatedUser;
import com.berezovskiy.diploma.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@PageTitle("Передбачення")
@Route(value = "predictions", layout = MainLayout.class)
@PermitAll
public class PredictionsView extends VerticalLayout {

    private final Grid<Prediction> grid = new Grid<>(Prediction.class, false);

    private final TextField accidentHour = new TextField("Час ДТП");
    private final Select<DayOfWeek> dayOfWeek = new Select<>();
    private final Select<DriverAgeGroup> driverAgeGroup = new Select<>();
    private final Select<DriverGender> driverGender = new Select<>();
    private final Select<DriverExperience> driverExperience = new Select<>();
    private final Select<VehicleType> vehicleType = new Select<>();
    private final Checkbox vehicleDefected = new Checkbox("Автівка попередньо пошкоджена");
    private final Select<RoadAlignment> roadAlignment = new Select<>();
    private final Select<RoadSurfaceType> roadSurfaceType = new Select<>();
    private final Select<RoadSurfaceCondition> roadSurfaceCondition = new Select<>();
    private final Select<LightCondition> lightCondition = new Select<>();
    private final Select<WeatherCondition> weatherCondition = new Select<>();
    private final Select<CollisionType> collisionType = new Select<>();
    private final TextField vehiclesNumber = new TextField("Кількість автівок в ДТП (1-10)");
    private final Select<VehicleMovement> vehicleMovement = new Select<>();
    private final Select<CollisionCause> collisionCause = new Select<>();
    private final Select<Dataset> availableDatasets = new Select<>();

    private final Button cancel = new Button("Назад");
    private final Button save = new Button("Зберегти");

    private BeanValidationBinder<Prediction> binder;

    private Prediction prediction;

    private final PredictionRepository predictionRepository;
    private final DatasetRepository datasetRepository;
    private final MachineLearningService machineLearningService;
    private final AuthenticatedUser authenticatedUser;

    public PredictionsView(PredictionRepository predictionRepository, DatasetRepository datasetRepository,
                           MachineLearningService machineLearningService, AuthenticatedUser authenticatedUser) {
        this.predictionRepository = predictionRepository;
        this.datasetRepository = datasetRepository;
        this.machineLearningService = machineLearningService;
        this.authenticatedUser = authenticatedUser;
        addClassNames("single-dataset-view");
        constructUI();
    }

    private void constructUI() {
        var availableDatasetsList = datasetRepository.findByIsTrainedOnAndUserId(true,
                authenticatedUser.get().get().getId());
        if (availableDatasetsList.isEmpty()) {
            createEmptyUI();
            return;
        }
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(40);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout, availableDatasetsList);

        add(splitLayout);

        grid.addComponentColumn(prediction -> {
            Button deleteButton = new Button(VaadinIcon.CLOSE.create());
            deleteButton.addClickListener(event -> predictionRepository.deleteById(prediction.getId()));
            return deleteButton;
        }).setHeader("Видалити").setAutoWidth(true);
        grid.addColumn(Prediction::getId).setHeader("ДТП id").setAutoWidth(true);
        grid.addColumn(Prediction::getCasualtiesNumber).setHeader("Кількість постраждалих").setAutoWidth(true);
        grid.addColumn(Prediction::getAccidentSeverity).setHeader("Важкість ДТП").setAutoWidth(true);

        grid.setItems(query -> predictionRepository.findAll(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            populateForm(event.getValue());
        });

        binder = new BeanValidationBinder<>(Prediction.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                prediction = Prediction.builder()
                        .dataset(availableDatasets.getValue())
                        .accidentHour(Integer.parseInt(accidentHour.getValue()))
                        .dayOfWeek(dayOfWeek.getValue())
                        .driverAgeGroup(driverAgeGroup.getValue())
                        .driverGender(driverGender.getValue())
                        .driverExperience(driverExperience.getValue())
                        .vehicleType(vehicleType.getValue())
                        .vehicleDefected(vehicleDefected.getValue())
                        .roadAlignment(roadAlignment.getValue())
                        .roadSurfaceType(roadSurfaceType.getValue())
                        .roadSurfaceCondition(roadSurfaceCondition.getValue())
                        .lightCondition(lightCondition.getValue())
                        .weatherCondition(weatherCondition.getValue())
                        .collisionType(collisionType.getValue())
                        .vehiclesNumber(Integer.parseInt(vehiclesNumber.getValue()))
                        .vehicleMovement(vehicleMovement.getValue())
                        .collisionCause(collisionCause.getValue())
                        .build();
                var predictedPrediction = machineLearningService.predict(prediction);
                predictionRepository.save(predictedPrediction);

                Notification.show("Data updated");
                UI.getCurrent().navigate("predictions");
            } catch (Exception exception) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

        grid.setHeight("100%");
    }

    private void createEditorLayout(SplitLayout splitLayout, List<Dataset> availableDatasetsList) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        dayOfWeek.setLabel("День тижня");
        driverAgeGroup.setLabel("Возраст водія");
        driverGender.setLabel("Пол водія");
        driverExperience.setLabel("Досвід водія");
        vehicleType.setLabel("Тип автівки");
        roadAlignment.setLabel("Вирівнювання дороги");
        roadSurfaceType.setLabel("Тип дороги");
        roadSurfaceCondition.setLabel("Дорожні умови");
        lightCondition.setLabel("Освітлення");
        weatherCondition.setLabel("Погодні умови");
        collisionType.setLabel("Тип зіштовхнення");
        vehicleMovement.setLabel("Рух транспортного засобу перед зіткненням");
        collisionCause.setLabel("Причина зіштовхнення");
        availableDatasets.setLabel("Доступні датасети");

        dayOfWeek.setItems(DayOfWeek.values());
        driverAgeGroup.setItems(DriverAgeGroup.values());
        driverGender.setItems(DriverGender.values());
        driverExperience.setItems(DriverExperience.values());
        vehicleType.setItems(VehicleType.values());
        roadAlignment.setItems(RoadAlignment.values());
        roadSurfaceType.setItems(RoadSurfaceType.values());
        roadSurfaceCondition.setItems(RoadSurfaceCondition.values());
        lightCondition.setItems(LightCondition.values());
        weatherCondition.setItems(WeatherCondition.values());
        collisionType.setItems(CollisionType.values());
        vehicleMovement.setItems(VehicleMovement.values());
        collisionCause.setItems(CollisionCause.values());
        availableDatasets.setItems(availableDatasetsList);

        FormLayout formLayout = new FormLayout();
        formLayout.add(availableDatasets, accidentHour, dayOfWeek, driverAgeGroup, driverGender,
                driverExperience, vehicleType, vehicleDefected, roadAlignment, roadSurfaceType,
                roadSurfaceCondition, lightCondition, weatherCondition, collisionType,
                vehiclesNumber, vehicleMovement, collisionCause);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createEmptyUI() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("Немає натренованих датасетів");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Спершу створіть та заповніть датасет 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Prediction prediction) {
        this.prediction = prediction;
        binder.readBean(this.prediction);

    }
}
