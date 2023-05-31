package com.berezovskiy.diploma.views.datasets;

import com.berezovskiy.diploma.data.entity.Accident;
import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.entity.enums.*;
import com.berezovskiy.diploma.data.repository.AccidentRepository;
import com.berezovskiy.diploma.views.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Дорожньо-транспортні пригоди")
@Route(value = "datasets/:datasetID", layout = MainLayout.class)
@Tag("single-dataset-view")
@Uses(Icon.class)
@PermitAll
public class SingleDatasetView extends Div implements BeforeEnterObserver {

    private final String DATASET_ID = "datasetID";

    private final Grid<Accident> grid = new Grid<>(Accident.class, false);

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
    private final TextField casualtiesNumber = new TextField("Кількість постраждалих (0-10)");
    private final Select<VehicleMovement> vehicleMovement = new Select<>();
    private final Select<CollisionCause> collisionCause = new Select<>();
    private final Select<AccidentSeverity> accidentSeverity = new Select<>();
    private final TextField accidentsNumber = new TextField("Кількість схожих аварій (<= 50, включаючи цей)");

    private final Button cancel = new Button("Назад");
    private final Button save = new Button("Зберегти");
    private final Button getFromCSV = new Button("Завантажити з .csv");

    private BeanValidationBinder<Accident> binder;

    private Accident accident;

    private final AccidentRepository accidentRepository;

    public SingleDatasetView(AccidentRepository accidentRepository) {
        this.accidentRepository = accidentRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long datasetId = event.getRouteParameters().get(DATASET_ID).map(Long::parseLong).get();
        constructUI(datasetId);
    }

    private void constructUI(Long datasetId) {
        addClassNames("single-dataset-view");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSplitterPosition(40);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout, datasetId);

        add(splitLayout);

        LitRenderer<Accident> importantRenderer = LitRenderer.<Accident> of(
                        "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", important -> important.isVehicleDefected() ? "check" : "minus").withProperty("color",
                        important -> important.isVehicleDefected()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");
        grid.addComponentColumn(accident -> {
            Button deleteButton = new Button(VaadinIcon.CLOSE.create());
            deleteButton.addClickListener(event -> accidentRepository.deleteById(accident.getId()));
            return deleteButton;
        }).setHeader("Видалити").setAutoWidth(true);
        grid.addColumn(Accident::getAccidentHour).setHeader("Час ДТП").setAutoWidth(true);
        grid.addColumn(Accident::getDriverExperience).setHeader("Досвід водія").setAutoWidth(true);
        grid.addColumn(Accident::getVehicleType).setHeader("Тип автівки").setAutoWidth(true);
        grid.addColumn(importantRenderer).setHeader("Автівка попередньо пошкоджена").setAutoWidth(true);
        grid.addColumn(Accident::getRoadSurfaceType).setHeader("Стан дороги").setAutoWidth(true);
        grid.addColumn(Accident::getWeatherCondition).setHeader("Погодні умови").setAutoWidth(true);
        grid.addColumn(Accident::getLightCondition).setHeader("Освітлення").setAutoWidth(true);
        grid.addColumn(Accident::getVehiclesNumber).setHeader("Кількість автівок в ДТП").setAutoWidth(true);
        grid.addColumn(Accident::getCasualtiesNumber).setHeader("Кількість постраждалих").setAutoWidth(true);
        grid.addColumn(Accident::getCollisionType).setHeader("Тип зіштовхнення").setAutoWidth(true);
        grid.addColumn(Accident::getCollisionCause).setHeader("Причина зіштовхнення").setAutoWidth(true);
        grid.addColumn(Accident::getAccidentSeverity).setHeader("Важкість ДТП").setAutoWidth(true);
        grid.addColumn(Accident::getAccidentsNumber).setHeader("Кількість схожих аварій").setAutoWidth(true);

        grid.setItems(query -> {
            var accidents = accidentRepository.findByDatasetId(datasetId,
                            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)));
            System.out.println(accidents.size());
            return accidents.stream();
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            populateForm(event.getValue());
        });

        binder = new BeanValidationBinder<>(Accident.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.accident == null) {
                    this.accident = new Accident();
                }
                this.accident.setDataset(new Dataset(datasetId));
                binder.writeBean(this.accident);
                accidentRepository.save(this.accident);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate("datasets/" + datasetId);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception exception) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

        grid.setHeight("100%");
    }

    private void createEditorLayout(SplitLayout splitLayout, Long datasetId) {
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
        accidentSeverity.setLabel("Важкість ДТП");

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
        accidentSeverity.setItems(AccidentSeverity.values());

        FormLayout formLayout = new FormLayout();
        formLayout.add(accidentHour, dayOfWeek, driverAgeGroup, driverGender, driverExperience,
                vehicleType, vehicleDefected, roadAlignment, roadSurfaceType, roadSurfaceCondition,
                lightCondition, weatherCondition, collisionType, vehiclesNumber, casualtiesNumber,
                vehicleMovement, collisionCause, accidentSeverity, accidentsNumber);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv, datasetId);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv, Long datasetId) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        getFromCSV.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getFromCSV.addClickListener(event -> UI.getCurrent().navigate("datasets/import/" + datasetId));
        buttonLayout.add(save, getFromCSV, cancel);
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

    private void populateForm(Accident accident) {
        this.accident = accident;
        binder.readBean(this.accident);

    }
}
