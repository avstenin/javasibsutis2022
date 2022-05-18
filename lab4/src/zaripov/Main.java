package zaripov;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Place {
    private String country;
    private String city;

    public Place(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return "Country='" + country + '\'' +
                ", city='" + city + '\'';
    }
}

abstract class Operator {
    protected String operatorName;
    protected Place address;

    public Operator(String operatorName, String country, String city) {
        this.operatorName = operatorName;
        this.address = new Place(country, city);
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Place getAddress() {
        return address;
    }

    public void setAddress(Place address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "operatorName:'" + operatorName + '\'' +
                ", address:" + address +
                '}';
    }
}

class Tariff extends Operator {
    private int internetSpeed4G;
    private int internetSpeed5G;
    private int wireSpeed;

    private int cost;

    public Tariff(String operatorName, String country, String city, int internetSpeed4G,
                  int internetSpeed5G, int wireSpeed, int cost) {
        super(operatorName, country, city);
        this.internetSpeed4G = internetSpeed4G;
        this.internetSpeed5G = internetSpeed5G;
        this.wireSpeed = wireSpeed;
        this.cost = cost;
    }

    public int getInternetSpeed4G() {
        return internetSpeed4G;
    }

    public void setInternetSpeed4G(int internetSpeed4G) {
        this.internetSpeed4G = internetSpeed4G;
    }

    public int getInternetSpeed5G() {
        return internetSpeed5G;
    }

    public void setInternetSpeed5G(int internetSpeed5G) {
        this.internetSpeed5G = internetSpeed5G;
    }

    public int getWireSpeed() {
        return wireSpeed;
    }

    public void setWireSpeed(int wireSpeed) {
        this.wireSpeed = wireSpeed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "operatorName=" + operatorName +
                ", country=" + address.getCountry() +
                ", city=" + address.getCity() +
                ", internetSpeed4G=" + internetSpeed4G +
                ", internetSpeed5G=" + internetSpeed5G +
                ", wireSpeed=" + wireSpeed +
                ", cost=" + cost +
                '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Tariff> tariffs = readTariffs("objects.csv");
        int mode;

        while (true) {
            System.out.println("""
                    Select the mode:\s
                    \t1)Show tariffs by operator
                    \t2)Show tariffs by country
                    \t3)Show tariffs by city
                    \t4)Show tariffs by service
                    \t5)Show available tariffs
                    \t6)Show available operators
                    \t7)Show available countries
                    \t8)Show available cities
                    """);
            mode = scanner.nextInt();
            if (mode < 1 || mode > 8) {
                System.out.println("Wrong mode! Select again.");
            } else {
                break;
            }
        }

        String object;
        switch (mode) {
            case 1 -> {
                System.out.println("Available operators:");
                List<String> operators = availableOperators(tariffs);
                operators.forEach(o -> System.out.println("\t" + o));
                System.out.println("Enter the operator:");
                while (true) {
                    object = scanner.next();
                    if (operators.contains(object)) {
                        break;
                    } else {
                        System.out.println("Wrong operator! Enter again.");
                    }
                }
                getFields(tariffs, object, 1);
            }
            case 2 -> {
                System.out.println("Available countries:");
                List<String> countries = availableCountries(tariffs);
                countries.forEach(o -> System.out.println("\t" + o));
                System.out.println("Enter the country:");
                while (true) {
                    object = scanner.next();
                    if (countries.contains(object)) {
                        break;
                    } else {
                        System.out.println("Wrong country! Enter again.");
                    }
                }
                getFields(tariffs, object, 2);
            }
            case 3 -> {
                System.out.println("Available cities:");
                List<String> cities = availableCities(tariffs);
                cities.forEach(o -> System.out.println("\t" + o));
                System.out.println("Enter the city:");
                while (true) {
                    object = scanner.next();
                    if (cities.contains(object)) {
                        break;
                    } else {
                        System.out.println("Wrong city! Enter again.");
                    }
                }
                getFields(tariffs, object, 3);
            }
            case 4 -> {
                while (true) {
                    System.out.println("""
                            Select the mode:\s
                            \t1)Show tariffs with 4G internet
                            \t2)Show tariffs with 5G internet
                            \t3)Show tariffs with wire internet
                            """);
                    mode = scanner.nextInt();
                    if (mode < 1 || mode > 3) {
                        System.out.println("Wrong mode! Select again.");
                    } else {
                        break;
                    }
                }
                getTariffsByParameter(tariffs, mode);
            }
            case 5 -> availableTariffs(tariffs);
            case 6 -> availableOperators(tariffs).forEach(System.out::println);
            case 7 -> availableCities(tariffs).forEach(System.out::println);
            case 8 -> availableCountries(tariffs).forEach(System.out::println);
        }
        scanner.close();
    }

    private static List<String> availableCities(List<Tariff> tariffs) {
        return tariffs.stream().map(o -> o.getAddress().getCity()).distinct().toList();
    }

    private static List<String> availableCountries(List<Tariff> tariffs) {
        return tariffs.stream().map(o -> o.getAddress().getCountry()).distinct().toList();
    }

    private static List<String> availableOperators(List<Tariff> tariffs) {
        return tariffs.stream().map(Tariff::getOperatorName).distinct().toList();
    }

    private static void getFields(List<Tariff> tariffs, String object, int mode) {
        switch (mode) {
            case 1 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getOperatorName().equals(object))
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
            case 2 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getAddress().getCountry().equals(object))
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
            case 3 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getAddress().getCity().equals(object))
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
        }
    }

    public static void availableTariffs(List<Tariff> tariffs) {
        tariffs.forEach(System.out::println);
    }

    public static void getTariffsByParameter(List<Tariff> tariffs, int mode) {
        switch (mode) {
            case 1 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getInternetSpeed4G() != 0)
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
            case 2 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getInternetSpeed5G() != 0)
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
            case 3 -> availableTariffs(tariffs.stream()
                    .filter(o -> o.getWireSpeed() != 0)
                    .sorted(Comparator.comparingInt(Tariff::getCost)).toList());
        }
    }

    public static File searchFile(File currentDirectory, String filename) {
        File[] files = currentDirectory.listFiles();
        File searchFile = null;
        for (File file : files) {
            if (file.isDirectory()) {
                searchFile = searchFile(file, filename);
                if (searchFile != null) {
                    break;
                }
            } else if (file.getName().equals(filename)) {
                return file;
            }
        }
        return searchFile;
    }

    public static List<Tariff> readTariffs(String filename) {
        List<Tariff> tariffs = new ArrayList<>();
        File beginDirectory = new File(System.getProperty("user.dir"));
        try (FileReader reader = new FileReader(searchFile(beginDirectory, filename));
             BufferedReader br = new BufferedReader(reader)) {

            String line = br.readLine();
            if (line == null)
                throw new IOException("File is empty!");
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                tariffs.add(new Tariff(values[0], values[1], values[2],
                        Integer.parseInt(values[3]), Integer.parseInt(values[4]),
                        Integer.parseInt(values[5]), Integer.parseInt(values[6])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File read error!");
        }
        return tariffs;
    }

}
