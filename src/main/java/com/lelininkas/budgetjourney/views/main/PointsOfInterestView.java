package com.lelininkas.budgetjourney.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import com.lelininkas.budgetjourney.PointOfInterest;
import com.lelininkas.budgetjourney.PointsOfInterestResponse;
import com.lelininkas.budgetjourney.TripsAdvisorService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;

import java.text.NumberFormat;
import java.util.Locale;

@PageTitle("TripOnBudget")
@Route("")
public class PointsOfInterestView extends VerticalLayout {

    private final TripsAdvisorService tripsAdvisorService;
    private final Grid<PointOfInterest> grid;
    private final Binder<SearchCriteria> binder;
    private final Button searchButton;

    public PointsOfInterestView(TripsAdvisorService tripsAdvisorService) {
        this.tripsAdvisorService = tripsAdvisorService;

        UI.getCurrent().getElement().setAttribute("theme", Lumo.DARK);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setWidthFull();
        logoLayout.setAlignItems(Alignment.BASELINE);

        Label title = new Label("BudgetJourney");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "20px");

        Image logo = new Image("images/trip_on_budget.png", "");
        logo.setMaxWidth("55px");

        logoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        logoLayout.add(logo, title);

        // Create the binder for the search criteria
        binder = new Binder<>(SearchCriteria.class);

        HorizontalLayout userInputLayout = new HorizontalLayout();
        userInputLayout.setDefaultVerticalComponentAlignment(Alignment.END);

        // Create the text fields for the search criteria
        TextField cityField = new TextField("Your next destination:");
        cityField.setWidth("300px");
        cityField.getStyle().set("margin-right", "10px");
        cityField.setPlaceholder("city name");

        binder.forField(cityField)
                .asRequired("City name is required")
                .bind(SearchCriteria::getCity, SearchCriteria::setCity);

        IntegerField budgetField = new IntegerField("Your budget:");
        budgetField.setWidth("300px");
        budgetField.getStyle().set("margin-right", "10px");
        budgetField.setPlaceholder("dollars");

        binder.forField(budgetField)
                .asRequired("Budget is required")
                .withValidator(budget -> budget > 0, "Budget must be greater than zero")
                .bind(SearchCriteria::getBudget, SearchCriteria::setBudget);

        IntegerField maxDistanceField = new IntegerField("Max distance:");
        maxDistanceField.setWidth("300px");
        maxDistanceField.getStyle().set("margin-right", "10px");
        maxDistanceField.setPlaceholder("km");
        maxDistanceField.setValue(30);

        binder.forField(maxDistanceField)
                .asRequired("Max distance is required")
                .withValidator(budget -> budget > 0, "Max distance must be greater than zero")
                .bind(SearchCriteria::getMaxDistance, SearchCriteria::setMaxDistance);

        // Create the search button
        searchButton = new Button("Go!");
        searchButton.getStyle().set("margin-top", "10px");
        searchButton.addClickListener(e -> searchPointsOfInterest());
        searchButton.setDisableOnClick(true);

        userInputLayout.add(cityField, budgetField, maxDistanceField, searchButton);

        add(logoLayout, userInputLayout);

        // Create the grid to display the points of interest
        grid = new Grid<>();
        grid.addColumn(PointOfInterest::getName).setHeader("Place").setFlexGrow(2);
        grid.addColumn(PointOfInterest::getInfo).setHeader("Info").setFlexGrow(4);
        grid.addColumn(this::renderCost).setHeader("Price").setFlexGrow(0);
        grid.addColumn(PointOfInterest::getDistance).setHeader("Distance").setFlexGrow(0);
        grid.addComponentColumn(item ->  {
            Anchor anchor = new Anchor();
            anchor.getElement().setAttribute("target", "_blank");
            anchor.setText("Show on map");
            anchor.setHref(item.getMapUrl());
            return anchor;
        }).setHeader("Location").setFlexGrow(1);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        add(grid);
    }

    private String renderCost(PointOfInterest pointOfInterest) {
        String formattedCost = NumberFormat.getCurrencyInstance(Locale.US).format(pointOfInterest.getCost());
        return formattedCost.replaceAll("\\.00", "");
    }

    private void searchPointsOfInterest() {
        // Bind the search criteria to the binder
        SearchCriteria searchCriteria = new SearchCriteria();

        if (binder.writeBeanIfValid(searchCriteria)) {
            // Call the suggestPointsOfInterest method and update the grid with the results

            PointsOfInterestResponse response = tripsAdvisorService
                    .suggestPointsOfInterest(searchCriteria.getCity(),
                            searchCriteria.getBudget(),
                            searchCriteria.getMaxDistance());

            if (response.getError() != null) {
                showErrorMessage(String.format("Failed loading data from OpenAI GPT: %n%s", response.getError()));
            } else {
                grid.setItems(response.getPointsOfInterest());
            }

            searchButton.setEnabled(true);
        }
    }

    private static class SearchCriteria {
        private String city;
        private int budget;

        private int maxDistance;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getBudget() {
            return budget;
        }

        public void setBudget(int budget) {
            this.budget = budget;
        }

        public int getMaxDistance() {
            return maxDistance;
        }

        public void setMaxDistance(int maxDistance) {
            this.maxDistance = maxDistance;
        }
    }

    private void showErrorMessage(String errorMessage) {
        Notification notification = new Notification();
        notification.setText(errorMessage);
        notification.setDuration(10_000); // Set the duration to 10 seconds
        notification.setPosition(Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

}

