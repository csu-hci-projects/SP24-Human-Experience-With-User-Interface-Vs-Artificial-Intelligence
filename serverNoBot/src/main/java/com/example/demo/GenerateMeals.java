package com.example.demo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateMeals {

    //public static List<String> used = new ArrayList<>();

    
    public List<String> allMeals(String meal, Calculations calculator) {

        List<String> bf = new ArrayList<>();
        List<Integer> elgible = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < Caller.type.size(); i++) {
            if (Caller.type.get(i).contains(meal)) {
                elgible.add(i);
            }
        }

        int allocatedCal = calculator.userCals / 3;
        int allocatedProtein = (int) calculator.protein / 3;
        int allocatedCarbs = (int) calculator.carbs / 3;
        int allocatedFat = (int) calculator.fat / 3;
        
        double calPercent = 1.0;
        double proteinPercent = 1.0;
        double carbPercent = 1.0;
        double fatPercent = 1.0;
        boolean done = false;
        int target = 0;

        while (!done) {

            boolean macrosDone = false;
            int index = 0;

            if (target == 0) { //calories

                index = rand.nextInt(elgible.size());
                
                bf.add(Caller.names.get(elgible.get(index)));
                //used.add(Caller.names.get(index));
                
                //System.out.println("ADDED: " + Caller.names.get(index));

            } else if (target == 1) {  //protein

                int i = 0;

                while (!macrosDone) {
                    i++;
                    index = rand.nextInt(elgible.size());

                    double pCount = Caller.protein.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double cCount = Caller.carbs.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double fCount = Caller.fat.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));

                    if ((pCount > cCount) && (pCount > fCount) && (Caller.calories.get(elgible.get(index)) < (calPercent * allocatedCal))) {
                        macrosDone = true;
                    }
                    if (i == elgible.size()) {
                        return bf;
                    }
                }
                bf.add(Caller.names.get(elgible.get(index)));
                //used.add(Caller.names.get(elgible.get(index)));
                
                //System.out.println("ADDED PROTEIN: " + Caller.names.get(elgible.get(index)) + " with " + (calPercent * allocatedCal) + " remining");
                //System.out.println("Food calories: " + Caller.protein.get(elgible.get(index)));

            } else if (target == 2) {  //carbs

                int i = 0;

                while (!macrosDone) {
                    i++;
                    index = rand.nextInt(elgible.size());

                    double pCount = Caller.protein.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double cCount = Caller.carbs.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double fCount = Caller.fat.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));

                    if ((cCount > pCount) && (cCount > fCount) && (Caller.calories.get(elgible.get(index)) < (calPercent * allocatedCal))) {
                        macrosDone = true;
                    }
                    if (i == elgible.size()) {
                        return bf;
                    }
                }
                bf.add(Caller.names.get(elgible.get(index)));
                //used.add(Caller.names.get(elgible.get(index)));
                //System.out.println("ADDED CARB: " + Caller.names.get(index) + " with " + (calPercent * allocatedCal) + " remining");

              } else if (target == 3) {  //fat

                int i = 0;

                while (!macrosDone) {
                    i++;
                    index = rand.nextInt(elgible.size());

                    double pCount = Caller.protein.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double cCount = Caller.carbs.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));
                    double fCount = Caller.fat.get(elgible.get(index)) / Caller.calories.get(elgible.get(index));

                    if ((fCount > pCount) && (fCount > cCount) && (Caller.calories.get(elgible.get(index)) < (calPercent * allocatedCal))) {
                        macrosDone = true;
                    }
                    if (i == elgible.size()) {
                        return bf;
                    }
                }
                bf.add(Caller.names.get(elgible.get(index)));
                //used.add(Caller.names.get(elgible.get(index)));
                //System.out.println("ADDED FAT: " + Caller.names.get(index) + " with " + (calPercent * allocatedCal) + " remining");

            } else {
                done = true;
            }
            
            calPercent = (((calPercent * allocatedCal) - Caller.calories.get(elgible.get(index))) / allocatedCal);
            proteinPercent = (((proteinPercent * allocatedProtein) - Caller.protein.get(elgible.get(index))) / allocatedProtein);
            carbPercent = (((carbPercent * allocatedCarbs) - Caller.carbs.get(elgible.get(index))) / allocatedCarbs);
            fatPercent = (((fatPercent * allocatedFat) - Caller.fat.get(elgible.get(index))) / allocatedFat);

            //System.out.println("cal:" + calPercent + " protein:" + proteinPercent + " carb:" + carbPercent + " fat:" + fatPercent);
            target = getTarget(calPercent, proteinPercent, carbPercent, fatPercent);

        }




        return bf;
    }

    public int getTarget(double cal, double protein, double carb, double fat) {

            double max = Math.max(Math.max(protein, carb), fat);
            if (max < .05) {
                return -1;
            }

            if (max == protein) {
                return 1;
            } else if (max == carb) {
                return 2;
            } else if (max == fat) {
                return 3;
            }
            return -1;
        }






}
