package com.berezovskiy.diploma.views.datasets;

import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.security.AuthenticatedUser;
import com.berezovskiy.diploma.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PageTitle("Dataset creation")
@Route(value = "datasets/create", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class CreateDatasetView extends Main implements HasComponents, HasStyle {

    private final TextField title = new TextField("Назва датасету");
    private final TextField description = new TextField("Опис датасету");

    private final Button cancel = new Button("Назад");
    private final Button save = new Button("Зберегти");

    private Binder<Dataset> binder = new Binder<>(Dataset.class);

    public CreateDatasetView(AuthenticatedUser authenticatedUser, DatasetRepository datasetRepository) {
        addClassName("register-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE,
                LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Top.LARGE);

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> cancel
                .getUI()
                .ifPresent(ui -> ui.navigate("datasets"))
        );
        save.addClickListener(e -> {
            var dataset = binder.getBean();
            dataset.setUser(authenticatedUser.get().orElseThrow());
            var persistedDataset = datasetRepository.save(dataset);
            save.getUI()
                    .ifPresent(ui -> ui.navigate("datasets/" + persistedDataset.getId()));
        });
    }

    private void clearForm() {
        binder.setBean(new Dataset());
    }

    private Component createTitle() {
        return new H3("Створення датасету");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(title, description);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }
}