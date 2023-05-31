package com.berezovskiy.diploma.views.visualisation;

import com.berezovskiy.diploma.data.entity.Dataset;
import com.berezovskiy.diploma.data.entity.enums.*;
import com.berezovskiy.diploma.data.repository.AccidentRepository;
import com.berezovskiy.diploma.data.repository.DatasetRepository;
import com.berezovskiy.diploma.security.AuthenticatedUser;
import com.berezovskiy.diploma.views.MainLayout;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Chart;
import com.github.appreciated.apexcharts.config.PlotOptions;
import com.github.appreciated.apexcharts.config.Responsive;
import com.github.appreciated.apexcharts.config.XAxis;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.List;

@PageTitle("Візуалізація")
@Route(value = "graphics", layout = MainLayout.class)
@PermitAll
public class VisualisationView extends VerticalLayout {

    private ApexCharts chart = new ApexCharts();
    private final AccidentRepository accidentRepository;

    public VisualisationView(DatasetRepository datasetRepository, AuthenticatedUser authenticatedUser,
                             AccidentRepository accidentRepository) {
        this.accidentRepository = accidentRepository;
        var datasets = datasetRepository
                .findByIsTrainedOnAndUserId(true, authenticatedUser.get().get().getId());

        if (datasets.isEmpty()) {
            setSpacing(false);

            Image img = new Image("images/empty-plant.png", "placeholder plant");
            img.setWidth("200px");
            add(img);

            H2 header = new H2("Немає натренованих датасетів");
            header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
            add(header);
            add(new Paragraph("Спершу створіть та заповніть датасет 🤗"));

            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            getStyle().set("text-align", "center");
        } else {
            constructUI(datasets);
        }
    }

    private void constructUI(List<Dataset> datasets) {
        addClassNames("datasets-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        Select<Dataset> selectDataset = new Select<>();
        selectDataset.setLabel("Оберіть датасет");
        selectDataset.setItems(datasets);
        Select<AccidentFeature> selectAttribute = new Select<>();
        selectAttribute.setLabel("Оберіть атрибут");
        selectAttribute.setItems(AccidentFeature.values());
        selectAttribute.setVisible(false);

        selectDataset.addValueChangeListener(event -> selectAttribute.setVisible(true));
        selectAttribute.addValueChangeListener(event -> createChart(event, selectDataset.getValue()));

        container.add(selectDataset, selectAttribute);
        add(container);
    }

    private void createChart(AbstractField.ComponentValueChangeEvent<Select<AccidentFeature>, AccidentFeature> event, Dataset dataset) {
        AccidentFeature attribute = event.getValue();
        Series [] series = findSeries(attribute, dataset.getId());

        Chart chartConfig = ChartBuilder.get()
                .withType(Type.BAR)
                .withHeight("500px")
                .withStacked(true)
                .withZoom(ZoomBuilder.get().withEnabled(true).build())
                .withToolbar(ToolbarBuilder.get().withShow(true).build())
                .build();

        XAxis xAxis = XAxisBuilder.get()
                .withTitle(TitleBuilder.get().withText("Ступінь тяжкості аварії").build())
                .withCategories("Невеликий", "Серйозний", "Фатальний")
                .build();

        Responsive responsive = ResponsiveBuilder.get()
                .withBreakpoint(480.0)
                .withOptions(OptionsBuilder.get()
                        .withLegend(LegendBuilder.get()
                                .withPosition(Position.BOTTOM)
                                .withOffsetX(-10.0)
                                .withOffsetY(0.0)
                                .build())
                        .build())
                .build();

        PlotOptions plotOptions = PlotOptionsBuilder.get()
                .withBar(BarBuilder.get()
                        .withHorizontal(false)
                        .build())
                .build();

        remove(chart);

        chart = ApexChartsBuilder.get()
                .withSeries(series)
                .withChart(chartConfig)
                .withXaxis(xAxis)
                .withResponsive(responsive)
                .withLegend(LegendBuilder.get().withPosition(Position.RIGHT).withOffsetY(40.0).build())
                .withFill(FillBuilder.get().withOpacity(1.0).build())
                .withPlotOptions(plotOptions).build();

        add(chart);
    }

    private Series[] findSeries(AccidentFeature attribute, Long datasetId) {
        switch (attribute) {
            case VEHICLE_TYPE -> {
                return Arrays.stream(VehicleType.values())
                        .map(vehicleType -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByVehicleType(datasetId,
                                                    accidentSeverity.ordinal(), vehicleType.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(vehicleType.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
            case WEATHER_CONDITION -> {
                return Arrays.stream(WeatherCondition.values())
                        .map(weatherCondition -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByWeatherCondition(datasetId,
                                                    accidentSeverity.ordinal(), weatherCondition.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(weatherCondition.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
            case LIGHT_CONDITION -> {
                return Arrays.stream(LightCondition.values())
                        .map(lightCondition -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByLightCondition(datasetId,
                                                    accidentSeverity.ordinal(), lightCondition.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(lightCondition.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
            case ROAD_CONDITION -> {
                return Arrays.stream(RoadSurfaceType.values())
                        .map(roadCondition -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByRoadCondition(datasetId,
                                                    accidentSeverity.ordinal(), roadCondition.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(roadCondition.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
            case COLLISION_TYPE -> {
                return Arrays.stream(CollisionType.values())
                        .map(collisionType -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByCollisionType(datasetId,
                                                    accidentSeverity.ordinal(), collisionType.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(collisionType.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
            case COLLISION_CAUSE -> {
                return Arrays.stream(CollisionCause.values())
                        .map(collisionCause -> {
                            Integer [] seriesData = Arrays.stream(AccidentSeverity.values())
                                    .map(accidentSeverity -> accidentRepository
                                            .countByCollisionCause(datasetId,
                                                    accidentSeverity.ordinal(), collisionCause.ordinal()))
                                    .toArray(Integer[]::new);
                            Series<Integer> series = new Series<>();
                            series.setData(seriesData);
                            series.setName(collisionCause.getTitle());
                            return series;
                        })
                        .toArray(Series[]::new);
            }
        }
        throw new UnsupportedOperationException();
    }

}
