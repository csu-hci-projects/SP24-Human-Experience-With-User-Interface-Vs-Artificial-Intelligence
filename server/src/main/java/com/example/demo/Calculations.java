package com.example.demo;

import java.lang.Character;

public class Calculations {

    public int userCals;
    public int maintanence;
    public int grow;
    public int deficit;
    public double protein;
    public double carbs;
    public double fat;
    
    public Calculations(int age, String height, int weight, int desiredWeight, String sex, String activity, int body) {

        double activityFactor = 0;
        double bodyFat = 0;
        double RMR = 0;
        double TDEE = 0;
        int realHeight = 0;

        boolean first = true;
        for (int i = 0; i < height.length(); i++) {
            char curr = height.charAt(i);
            if (Character.isDigit(curr)) {
                if (first) {
                    realHeight += Character.getNumericValue(curr) * 12;
                    first = false;
                } else {
                    realHeight += Character.getNumericValue(curr);
                }
            }
        }

        if (activity.contains("1-2")) {
            activityFactor = 1.375;
        } else if (activity.contains("3-4")) {
            activityFactor = 1.55;
        } else {
            activityFactor = 1.725;
        }

        if (body == 1) {
            bodyFat = .10;
        } else if (body == 2) {
            bodyFat = .16;
        } else {
            bodyFat = .22;
        }

        double fatMass = (weight / 2.2) * bodyFat;
        double lbMass = (weight / 2.2) - fatMass;
 
        if (sex.contains("Male")) {
            RMR = (9.99* (weight / 2.2)) + (6.25 * (realHeight * 2.54)) - (4.92 * age) + 5; //Mifflin-St.Jeor Men
        } else {
            RMR = (9.99* (weight / 2.2)) + (6.25 * (realHeight * 2.54)) - (4.92 * age) - 161; //Mifflin-St.Jeor Women
        }
        
        TDEE = RMR * activityFactor;
        maintanence = (int) TDEE;

        double wtLoss = (.5 * (weight / 2.2)) / 100;
        double fromBF = wtLoss * 0.713;
        double lipidBF = (fromBF * 1000) * 0.87;
        double defBF = lipidBF * 9;

        double fromLBM = wtLoss * 0.287;
        double defLBM = fromLBM *0.3*1000 * 4;

        double totalDeficit = defLBM + defBF;

        double avgDeficit = Math.round(TDEE - (totalDeficit / 7));
        double growth = Math.round(TDEE + 500);

        grow = (int) growth;
        deficit = (int) avgDeficit;

        //System.out.println("LOSE: "+deficit+" | MAINTAIN: "+maintanence+" | GROW: " + grow);


        if (sex.contains("Male")) {

            if (desiredWeight > weight) {
                protein = 3.2 * lbMass;
                carbs = 3.5 * (weight / 2.2);
                double kFat = grow - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            } else if (desiredWeight < weight) {
                protein = 2.2 * lbMass;
                carbs = 2.2 * (weight / 2.2);
                double kFat = deficit - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            } else {
                protein = 2.5 * lbMass;
                carbs = 2.8 * (weight / 2.2);
                double kFat = maintanence - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            }

            double kFat = deficit - (protein * 4) - (carbs * 4);
            fat = Math.round(kFat / 9);

        } else {

            if (desiredWeight > weight) {
                protein = 4 * lbMass;
                carbs = 4.8 * (weight / 2.2);
                double kFat = grow - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            } else if (desiredWeight < weight) {
                protein = 3.2 * lbMass;
                carbs = 1.5 * (weight / 2.2);
                double kFat = deficit - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            } else {
                protein = 3.2 * lbMass;
                carbs = 2.5 * (weight / 2.2);
                double kFat = maintanence - (protein * 4) - (carbs * 4);
                fat = Math.round(kFat / 9);
            }

        }

        if (desiredWeight > weight) {
            userCals = grow;
        } else if (desiredWeight < weight) {
            userCals = deficit;
        } else {
            userCals = maintanence;
        }


    }

}
