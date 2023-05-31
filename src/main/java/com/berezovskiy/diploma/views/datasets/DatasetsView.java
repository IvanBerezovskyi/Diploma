package com.berezovskiy.diploma.views.datasets;

import com.berezovskiy.diploma.analysis.MachineLearningService;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.import_export.ExportService;
import com.berezovskiy.diploma.security.AuthenticatedUser;
import com.berezovskiy.diploma.views.MainLayout;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.PermitAll;

import java.util.concurrent.atomic.AtomicInteger;

@PageTitle("Датасети")
@Route(value = "datasets", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DatasetsView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    public DatasetsView(AuthenticatedUser user, DatasetRepository datasetRepository,
                        MachineLearningService machineLearningService, ExportService exportService) {
        constructUI();

        AtomicInteger counter = new AtomicInteger(1);
        datasetRepository.findByUserId(user.get().orElseThrow().getId())
                .forEach(dataset -> imageContainer
                        .add(new DatasetsViewCard(machineLearningService, datasetRepository,
                                exportService, dataset, counter.getAndIncrement())));
    }

    private void constructUI() {
        addClassNames("datasets-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Датасети аварій");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Перегляньте наявний датасет або створіть новий");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button createDatasetButton = new Button("Створити датасет");
        createDatasetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createDatasetButton.addClickListener(e -> UI.getCurrent().navigate("datasets/create"));

        imageContainer = new OrderedList();
        imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

        container.add(headerContainer, createDatasetButton);
        add(container, imageContainer);

    }
}
