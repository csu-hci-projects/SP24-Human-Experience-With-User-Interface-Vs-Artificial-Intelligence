package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Caller {
    private Connection connection;

    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<Integer> calories = new ArrayList<>();
    public static ArrayList<Double> protein = new ArrayList<>();
    public static ArrayList<Double> carbs = new ArrayList<>();
    public static ArrayList<Double> fat = new ArrayList<>();
    public static ArrayList<String> servingSize = new ArrayList<>();
    public static ArrayList<String> type = new ArrayList<>();
    Calculations calc;
    
    public Caller() {
        try {
            String url = "jdbc:sqlite:/Users/bbgabel/smarteats-db";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String useData(Calculations calculator) {
        calc = calculator;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data");


            while (resultSet.next()) {
                names.add(resultSet.getString("Name"));
                calories.add(resultSet.getInt("Calories"));
                protein.add(resultSet.getDouble("Protein"));
                carbs.add(resultSet.getDouble("Carbs"));
                fat.add(resultSet.getDouble("Fat"));
                servingSize.add(resultSet.getString("Serving"));
                type.add(resultSet.getString("Type"));

            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GenerateMeals gen = new GenerateMeals();
        List<String> bf;
        List<String> l;
        List<String> d;

            bf = gen.allMeals("B", calculator);
            l = gen.allMeals("L", calculator);
            d = gen.allMeals("D", calculator);

            System.out.println("Attempting: " + calculator.userCals + " Calories, " + Math.round(calculator.protein) + "g Protein, " + Math.round(calculator.carbs) + "g Carbs, and " + Math.round(calculator.fat) + "g Fat.");
            System.out.println("________________________________________________________\n");

            int test = 0;
            while (!checkMargin(bf)) {
                bf = gen.allMeals("B", calculator);
                test++;
            }
            System.out.println("(breakfast) Algorithm completed with [" + test + "] attemps!");
            System.out.println(bf);
            System.out.println("________________________________________________________\n");

            
            test = 0;

            while (!checkMargin(l)) {
                l = gen.allMeals("L", calculator);
                test++;
            }
            System.out.println("(lunch) Algorithm completed with [" + test + "] attemps!");
            System.out.println(l);
            System.out.println("________________________________________________________\n");

            test = 0;
            
            while (!checkMargin(d)) {
                d = gen.allMeals("D", calculator);
                test++;      
            }
            System.out.println("(dinner) Algorithm completed with [" + test + "] attemps!");
            System.out.println(d);
            System.out.println("________________________________________________________\n");



            Map<String, List<?>> breakfast = generateResults(bf);
            Map<String, List<?>> lunch = generateResults(l);
            Map<String, List<?>> dinner = generateResults(d);
            Map<String, Map<String, List<?>>> Meals = new HashMap<>();
            Meals.put("breakfast", breakfast);
            Meals.put("lunch", lunch);
            Meals.put("dinner", dinner);

            System.out.println("\nSuccess!");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";

            try {
                // Serialize the map to a JSON string
                json = objectMapper.writeValueAsString(Meals);
    
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
            }

            return json;
            
            

            
    }

    public boolean checkMargin(List<String> meal) {

        int totalCal = 0;
        int totalP = 0;
        int totalC = 0;
        int totalF = 0;
        boolean validCal = false;
        boolean validP = false;
        boolean validC = false;
        boolean validF = false;
        double allocatedCal = calc.userCals / 3;

        for (String i : meal) {
            int index = names.indexOf(i);
            totalCal += calories.get(index);
            totalP += protein.get(index);
            totalC += carbs.get(index);
            totalF += fat.get(index);
        }
        //System.out.println("Goal: " + calc.userCals / 3 + " | " + calc.protein / 3  + " | " + calc.carbs / 3 + " | " + calc.fat / 3);
        //System.out.println("Got: " + totalCal + " | " + totalP + " | " + totalC + " | " + totalF);
        //System.out.println(meal + "\n");
        //System.out.println("________________________________________________________");

        if (((totalCal / allocatedCal) < 1.2) && ((totalCal / allocatedCal) > .8)) {
            validCal = true;
        }
        if ((totalP / (calc.protein / 3)) < 1.5 && (totalP / (calc.protein / 3)) > .5) {
            validP = true;
        }
        if ((totalC / (calc.carbs / 3)) < 1.5 && (totalC / (calc.carbs / 3)) > .5) {
            validC = true;
        }
        if ((totalF / (calc.fat / 3)) < 1.5 && (totalF / (calc.fat / 3)) > .5) {
            validF = true;
        }

        if (validCal && validP && validC && validF) {
            System.out.println("Calories acheived: " + totalCal + " | desired: " + Math.round(calc.userCals / 3));
            System.out.println("Protein acheived: " + totalP + " | desired: " + Math.round(calc.protein / 3));
            System.out.println("Carbs acheived: " + totalC + " | desired: " + Math.round(calc.carbs / 3));
            System.out.println("Fat acheived: " + totalF + " | desired: " + Math.round(calc.fat / 3));
            return true;
        } else {
            return false;
        }

    }

    public Map<String, List<?>> generateResults(List<String> meal) {

        Map<String, List<?>> result = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<Integer> totalCal = new ArrayList<>();
        List<Double> totalP = new ArrayList<>();
        List<Double> totalC = new ArrayList<>();
        List<Double> totalF = new ArrayList<>();
        List<String> serving = new ArrayList<>();
        
        for (String i : meal) {
            int index = Caller.names.indexOf(i);
            names.add(i);
            totalCal.add(Caller.calories.get(index));
            totalP.add(protein.get(index));
            totalC.add(carbs.get(index));
            totalF.add(fat.get(index));
            serving.add(servingSize.get(index));
        }

        result.put("Names", names);
        result.put("Calories", totalCal);
        result.put("Protein", totalP);
        result.put("Carbs", totalC);
        result.put("Fat", totalF);
        result.put("Serving", serving);

        return result;
    }

    

}
