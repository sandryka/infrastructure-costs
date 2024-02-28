package com.example.infrastructurecosts;

import android.icu.text.NumberFormat;

import java.util.Locale;

public class InfrastructureCalculator {
    private double mileage;
    private String selectedMode;
    private double totalCost;

    // Variables for breakdown costs
    private double concreteCost;
    private double asphaltCost;
    private double streetlightCost;
    private double trafficSignCost;
    private double crosswalkCost;
    private double energyConsumptionCost;
    private double signalsCost;

    // Constants for Traditional Street
    private static final double STREET_CONCRETE_COST_PER_MILE = 500000;
    private static final double STREET_ASPHALT_COST_PER_MILE = 125000;
    private static final double STREET_STREETLIGHT_COST_PER_MILE = 87500;
    private static final double STREET_TRAFFIC_SIGN_COST_PER_MILE = 3000;
    private static final double STREET_CROSSWALK_COST_PER_MILE = 132000;
    private static final double STREET_ENERGY_CONSUMPTION_COST_PER_MILE = 316000;
    private static final double STREET_SIGNALS_COST_PER_MILE = 600000;

    // Constants for UAM Vertiport
    private static final double VERTIPORT_BUILDING_COST = 300000;
    private static final double VERTIPORT_CONCRETE_COST_PER_SQ_FT = 16;
    private static final double VERTIPORT_ASPHALT_COST_PER_MILE = 10000;
    private static final double VERTIPORT_LIGHTING_COST = 100000;
    private static final double VERTIPORT_SIGNAGE_COST = 100000;
    private static final double VERTIPORT_ENERGY_CONSUMPTION_COST = 316000;
    private static final double VERTIPORT_ATC_SERVICES_COST = 175000;
    private static final double VERTIPORT_FATO_SQ_FT = 2000;

    // Constructor
    public InfrastructureCalculator(double mileage, String selectedMode) {
        this.mileage = mileage;
        this.selectedMode = selectedMode;

    }

    public InfrastructureCalculator(double mileage) {
    }

    // Method to compute costs based on the mode selected
    public void computeCosts() {
        if ("Street".equalsIgnoreCase(selectedMode)) { // Changed from "Traditional" to "Street"
            concreteCost = STREET_CONCRETE_COST_PER_MILE * mileage;
            asphaltCost = STREET_ASPHALT_COST_PER_MILE * mileage;
            streetlightCost = STREET_STREETLIGHT_COST_PER_MILE * mileage;
            trafficSignCost = STREET_TRAFFIC_SIGN_COST_PER_MILE * mileage;
            crosswalkCost = STREET_CROSSWALK_COST_PER_MILE * mileage;
            energyConsumptionCost = STREET_ENERGY_CONSUMPTION_COST_PER_MILE * mileage;
            signalsCost = STREET_SIGNALS_COST_PER_MILE * mileage;

            totalCost = concreteCost + asphaltCost + streetlightCost + trafficSignCost +
                    crosswalkCost + energyConsumptionCost + signalsCost;
        } else if ("UAM".equalsIgnoreCase(selectedMode)) {
            concreteCost = VERTIPORT_CONCRETE_COST_PER_SQ_FT * VERTIPORT_FATO_SQ_FT;
            asphaltCost = VERTIPORT_ASPHALT_COST_PER_MILE * mileage;
            totalCost = VERTIPORT_BUILDING_COST + concreteCost + asphaltCost +
                    VERTIPORT_LIGHTING_COST + VERTIPORT_SIGNAGE_COST +
                    VERTIPORT_ENERGY_CONSUMPTION_COST + VERTIPORT_ATC_SERVICES_COST;
        }
    }

    // Get the total cost as a formatted string
    public String getTotalCost() {
        return formatCost(totalCost);
    }

    // Get the breakdown as a formatted string
// Get the breakdown as a formatted string
    public String getBreakdown() {
        StringBuilder breakdown = new StringBuilder();
        String modeDescription = "Street".equalsIgnoreCase(selectedMode) ? "Street mode" : "UAM mode";

        breakdown.append("The total cost for ").append(modeDescription).append(" is: ").append(formatCost(totalCost)).append("\n\nBreakdown of Costs:\n");

        if ("Street".equalsIgnoreCase(selectedMode)) {
            breakdown.append("Concrete: ").append(formatCost(concreteCost)).append("\n");
            breakdown.append("Asphalt: ").append(formatCost(asphaltCost)).append("\n");
            breakdown.append("Streetlight: ").append(formatCost(streetlightCost)).append("\n");
            breakdown.append("Traffic Sign: ").append(formatCost(trafficSignCost)).append("\n");
            breakdown.append("Crosswalk: ").append(formatCost(crosswalkCost)).append("\n");
            breakdown.append("Energy Consumption: ").append(formatCost(energyConsumptionCost)).append("\n");
            breakdown.append("Signals: ").append(formatCost(signalsCost)).append("\n");
        } else if ("UAM".equalsIgnoreCase(selectedMode)) {
            breakdown.append("Vertiport Building: ").append(formatCost(VERTIPORT_BUILDING_COST)).append("\n");
            breakdown.append("Vertiport Concrete: ").append(formatCost(concreteCost)).append("\n");
            breakdown.append("Vertiport Asphalt: ").append(formatCost(asphaltCost)).append("\n");
            breakdown.append("Vertiport Lighting: ").append(formatCost(VERTIPORT_LIGHTING_COST)).append("\n");
            breakdown.append("Vertiport Signage: ").append(formatCost(VERTIPORT_SIGNAGE_COST)).append("\n");
            breakdown.append("Energy Consumption: ").append(formatCost(VERTIPORT_ENERGY_CONSUMPTION_COST)).append("\n");
            breakdown.append("ATC Services: ").append(formatCost(VERTIPORT_ATC_SERVICES_COST)).append("\n");
        }
        return breakdown.toString();
    }


    // Helper method to format cost with commas and two decimal places
    private String formatCost(double cost) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(cost);
    }

    // Getters and Setters
    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    }
}
