package com.berezovskiy.diploma.views.register;

import com.berezovskiy.diploma.data.repository.UserRepository;
import com.berezovskiy.diploma.import_export.ImportService;
import com.berezovskiy.diploma.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

@AnonymousAllowed
@PageTitle("Реєстрація")
@Route(value = "register")
public class RegistrationView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public RegistrationView(AuthenticatedUser authenticatedUser, UserRepository userRepository,
                            PasswordEncoder passwordEncoder, ImportService importService) {
        this.authenticatedUser = authenticatedUser;
        RegistrationForm registrationForm = new RegistrationForm();

        add(registrationForm);

        RegistrationFormBinder registrationFormBinder = new RegistrationFormBinder(userRepository, registrationForm,
                passwordEncoder, importService);
        registrationFormBinder.addBindingAndValidation();

        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);
        setMargin(true);
        setSizeUndefined();
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            event.forwardTo("/datasets");
        }
    }
}
