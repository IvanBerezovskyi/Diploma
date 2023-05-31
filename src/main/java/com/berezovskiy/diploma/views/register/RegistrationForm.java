package com.berezovskiy.diploma.views.register;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public class RegistrationForm extends FormLayout {

    private final H3 title = new H3("Реєстрація");
    private final TextField username = new TextField("Імʼя користувача");
    private final EmailField email = new EmailField("Електронна адреса");
    private final PasswordField password = new PasswordField("Пароль");
    private final PasswordField passwordConfirm = new PasswordField("Повторіть пароль");
    private final Span errorMessageField = new Span();
    private final Button submitButton = new Button("Зареєструватись");

    public RegistrationForm() {
        setRequiredIndicatorVisible(username, email, password, passwordConfirm);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        errorMessageField.setHeight("20px");

        add(title, username, email, password, passwordConfirm, errorMessageField, submitButton);

        setMaxWidth("500px");
        setResponsiveSteps(
                new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
                new ResponsiveStep("470px", 2, ResponsiveStep.LabelsPosition.TOP));
        setColspan(title, 2);
        setColspan(username, 2);
        setColspan(email, 2);
        setColspan(errorMessageField, 2);
        setColspan(submitButton, 2);
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }
}