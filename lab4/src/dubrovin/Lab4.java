
import java.io.*;
import java.lang.management.GarbageCollectorMXBean;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Lab4 {

    private enum CommunicationType {
        WIRED,
        MOBILE,
        ANY
    }

    private class Service {

        private String _name;
        private int _price;
        private CommunicationType _communicationType;

        public Service(String name, int price, CommunicationType communicationType)
        {
            _name = name;
            _price = price;
            _communicationType = communicationType;
        }

        public String getName() {
            return _name;
        }

        public int getPrice() {
            return _price;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof String)
                return _name == obj;
            else if (obj instanceof Service)
            {
                if (((Service) obj)._communicationType == CommunicationType.ANY ||
                    _communicationType == CommunicationType.ANY)
                    return _name.equals(((Service) obj)._name);
                return _name.equals(((Service) obj)._name) &&
                        _communicationType == ((Service) obj)._communicationType;
            }
            return false;
        }
    }

    private class Operator {

        private String _name;
        private ArrayList<Service> _services;

        public Operator(
                String name,
                ArrayList<Service> services)
        {
            _name = name;
            _services = new ArrayList<Service>(services);
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(_name + " : ");
            for (Service service : _services)
                sb.append(service.getName())
                        .append("(")
                        .append(service.getPrice())
                        .append("), ");
            if (sb.charAt(sb.length() - 2) == ',')
                sb.delete(sb.length() - 2, sb.length());
            return sb.toString();
        }

        public int getServicePrice(String serviceName) {
            for (Service service : _services) {
                if (service.getName().equals(serviceName))
                    return service.getPrice();
            }
            return -1;
        }
    }

    private String config_filename = "config.csv";
    private ArrayList<Operator> operators = new ArrayList<Operator>();
    private ArrayList<Service> services = new ArrayList<Service>();

    public void run() {
        deserialize();
        menu();
    }

    private void menu() {
        var choose = 1;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\n");
            System.out.println("Watch full list (1)");
            System.out.println("Price of service (2)");
            System.out.println("List of operators with service (3)");
            System.out.println("Operator's services (4)");
            System.out.println("Exit (0)");
            choose = in.nextInt();

            if (choose == 0) {
                return;
            } else if (choose == 1) {
                for (Operator operator : operators)
                    System.out.println(operator.toString());
            } else if (choose == 2) {
                System.out.print("Enter service name : ");
                String serviceName = in.next();
                ArrayList<Operator> operators1 = new ArrayList<>();
                for (Operator operator : operators)
                {
                    if (operator.getServicePrice(serviceName) != -1)
                        operators1.add(operator);
                }
                operators1.sort(Comparator.comparingInt(o -> o.getServicePrice(serviceName)));
                for (Operator operator : operators1)
                    System.out.println(operator._name + " : " + operator.getServicePrice(serviceName));
            } else if (choose == 3) {
                System.out.print("Enter service name : ");
                String serviceName = in.next();
                for (Operator operator : operators)
                    if (operator.getServicePrice(serviceName) != -1)
                        System.out.println(operator._name);
            } else if (choose == 4) {
                System.out.print("Enter operator name : ");
                String operatorName = in.next();
                for (Operator operator : operators)
                    if (operator._name.equals(operatorName))
                        System.out.println(operator.toString());
            }
        }
    }

    private void deserialize() {
        try (BufferedReader input = new BufferedReader(new FileReader(config_filename))) {
            String name;
            ArrayList<Service> servicesRead = new ArrayList<>();
            List<String> lines = Files.readAllLines(Path.of(config_filename));
            for (String line : lines)
            {
                servicesRead.clear();
                var spl = line.split(";");
                if (spl.length < 4) continue;
                for (int i = 1; i < spl.length; i += 3)
                {
                    var serviceName = spl[i];
                    CommunicationType communicationType =
                            spl[i + 1].equals("w") ? CommunicationType.WIRED : CommunicationType.MOBILE;
                    var price = Integer.valueOf(spl[i + 2]);
                    servicesRead.add(new Service(serviceName, price, communicationType));
                }
                operators.add(new Operator(spl[0], servicesRead));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}