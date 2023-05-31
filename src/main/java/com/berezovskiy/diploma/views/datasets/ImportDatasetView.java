package com.berezovskiy.diploma.views.datasets;

import com.berezovskiy.diploma.import_export.ExportService;
import com.berezovskiy.diploma.import_export.ImportService;
import com.berezovskiy.diploma.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.IOException;
import java.io.InputStream;

@PageTitle("Завантаження датасету")
@Route(value = "datasets/import/:datasetID", layout = MainLayout.class)
@PermitAll
public class ImportDatasetView extends VerticalLayout implements BeforeEnterObserver {

    private final ImportService importService;
    private final ExportService exportService;

    public ImportDatasetView(ImportService importService, ExportService exportService) {
        this.importService = importService;
        this.exportService = exportService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long datasetId = event.getRouteParameters()
                .get("datasetID")
                .map(Long::parseLong).get();
        constructDownloadUI();
        constructUploadUI(datasetId);
    }

    private void constructDownloadUI() {
        Button download = new Button("Правила створення датасету");
        download.setWidth("300px");
        download.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        String accidentImportRules = exportService.exportAccidentImportRules();
        FileDownloadWrapper downloadWrapper = new FileDownloadWrapper("importRules.txt", accidentImportRules::getBytes);
        downloadWrapper.wrapComponent(download);
        add(downloadWrapper);
    }

    private void constructUploadUI(Long datasetId) {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.addSucceededListener(event -> {
            InputStream fileData = memoryBuffer.getInputStream();
            try {
                importService.importAccidents(datasetId, fileData);
            } catch (IOException e) {
                Notification.show(
                        "Error occured during import of " + memoryBuffer.getFileName(), 3000,
                        Notification.Position.BOTTOM_START);
                e.printStackTrace();
            }
            UI.getCurrent().navigate("/");
        });
        add(singleFileUpload);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.EVENLY);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}