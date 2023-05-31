package com.berezovskiy.diploma.views.register;

import com.berezovskiy.diploma.data.entity.User;
import com.berezovskiy.diploma.data.repository.UserRepository;
import com.berezovskiy.diploma.import_export.ImportService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

public class RegistrationFormBinder {

    private final UserRepository userRepository;
    private final RegistrationForm registrationForm;
    private final PasswordEncoder passwordEncoder;
    private final ImportService importService;
    private boolean enablePasswordValidation;

    public RegistrationFormBinder(UserRepository userRepository, RegistrationForm registrationForm,
                                  PasswordEncoder passwordEncoder, ImportService importService) {
        this.userRepository = userRepository;
        this.registrationForm = registrationForm;
        this.passwordEncoder = passwordEncoder;
        this.importService = importService;
    }

    public void addBindingAndValidation() {
        BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(registrationForm);

        binder.forField(registrationForm.getPassword())
                .withValidator(this::passwordValidator).bind("password");

        registrationForm.getPasswordConfirm().addValueChangeListener(e -> {
            enablePasswordValidation = true;
            binder.validate();
        });

        binder.setStatusLabel(registrationForm.getErrorMessageField());
        registrationForm.getSubmitButton().addClickListener(event -> {
            try {
                User user = new User();
                binder.writeBean(user);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User savedUser = userRepository.save(user);
                UI.getCurrent().access(() -> {
                    try {
                        importService.importDefaultDataset(savedUser);
                    } catch (IOException e) {
                        Notification.show("Не вдалось зберегти вбудований датасет");
                    }
                });
                UI.getCurrent().navigate("/datasets");
            } catch (ValidationException ignored) {}
        });
    }
    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {

        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }

        if (!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = registrationForm.getPasswordConfirm().getValue();

        if (pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }
}