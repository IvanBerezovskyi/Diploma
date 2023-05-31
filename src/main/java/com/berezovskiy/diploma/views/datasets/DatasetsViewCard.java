package com.berezovskiy.diploma.views.datasets;

import com.berezovskiy.diploma.analysis.MachineLearningService;
import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.import_export.ExportService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.vaadin.olli.FileDownloadWrapper;

public class DatasetsViewCard extends ListItem {

    private final DatasetRepository datasetRepository;
    private final MachineLearningService machineLearningService;
    private final ExportService exportService;

    public DatasetsViewCard(MachineLearningService machineLearningService, DatasetRepository datasetRepository,
                            ExportService exportService, Dataset dataset, Integer cardNumber) {
        this.machineLearningService = machineLearningService;
        this.datasetRepository = datasetRepository;
        this.exportService = exportService;
        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("160px");

        Image image = new Image();
        image.setWidth("100%");
        image.setSrc("images/datasets_" + cardNumber + ".jpg");
        image.setAlt(dataset.getTitle());

        div.add(image);

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(dataset.getTitle());

        Paragraph description = new Paragraph(dataset.getDescription());
        description.addClassName(Margin.Vertical.MEDIUM);

        add(div, header, description);

        createButtonLayout(dataset);
    }

    private void createButtonLayout(Dataset dataset) {
        Long datasetId = dataset.getId();
        HorizontalLayout upperButtonLayout = new HorizontalLayout();
        HorizontalLayout lowerButtonLayout = new HorizontalLayout();
        upperButtonLayout.addClassName("button-layout");
        lowerButtonLayout.addClassName("button-layout");

        Button details = configureButton("Подробиці", e -> UI.getCurrent().navigate("/datasets/" + datasetId));
        Button train = configureButton("Тренувати", e -> {
            UI.getCurrent().access(() -> {
                Notification.show("Розпочато тренування датасету.");
                machineLearningService.train(datasetId);
                Notification.show("Датасет '" + dataset.getTitle() + "' натреновано.");
            });
        });
        Button download = configureButton("В .csv", e -> {});
        Button delete = configureButton("Видалити", e -> {
            datasetRepository.deleteById(datasetId);
            UI.getCurrent().navigate("/datasets");
        });

        String fileContent = exportService.export(datasetId);
        FileDownloadWrapper downloadWrapper = new FileDownloadWrapper(String.format("predictions_%s.csv", datasetId), fileContent::getBytes);
        downloadWrapper.wrapComponent(download);

        upperButtonLayout.add(details, train);
        lowerButtonLayout.add(downloadWrapper, delete);

        add(upperButtonLayout, lowerButtonLayout);
    }

    private Button configureButton(String title, ComponentEventListener<ClickEvent<Button>> eventListener) {
        Button button = new Button(title);
        button.addClickListener(eventListener);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setWidth("140px");
        return button;
    }
}