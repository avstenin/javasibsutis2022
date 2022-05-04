package telecom;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLI
{
    private static FilterCtl fltr = new FilterCtl(new YAMLReader("assets").getTariffs());

    public static String[] extractListElements(String list)
    {
        String[] matches = Pattern.compile("[\\(;](.+?)(?=[\\);])")
                                  .matcher(list)
                                  .results()
                                  .map(match -> match.group(1))
                                  .toArray(String[]::new);

        return matches;
    }

    private static void parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("ilb", "inet-min", true, "Минимальная квота интернета (в ГБ) в тарифе");
        options.addOption("clb", "calls-min", true, "Минимальное число бесплатных звонков в тарифе");
        options.addOption("slb", "sms-min", true, "Минимальное число бесплатных СМС в тарифе");
        options.addOption("plb", "price-min", true, "Минимальная ежемесячная стоимость тарифа");
        options.addOption("pub", "price-max", true, "Максимальная ежемесячная стоимость тарифа");
        options.addOption("op", "operators", true, "Фильтр операторов; например, \"(МТС;Тинькофф Мобайл)\"");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Telecom-CLI", options);
            System.exit(1);
        }

        if (cmd.hasOption("ilb"))
            fltr.setInternetLowerBound(Double.parseDouble(cmd.getOptionValue("ilb")));
        if (cmd.hasOption("clb"))
            fltr.setCallsLowerBound(Double.parseDouble(cmd.getOptionValue("clb")));
        if (cmd.hasOption("slb"))
            fltr.setSmsLowerBound(Double.parseDouble(cmd.getOptionValue("slb")));
        if (cmd.hasOption("plb"))
            fltr.setCostLowerBound(Double.parseDouble(cmd.getOptionValue("plb")));
        if (cmd.hasOption("pub"))
            fltr.setCostUpperBound(Double.parseDouble(cmd.getOptionValue("pub")));
        if (cmd.hasOption("op"))
            fltr.setOperatorList(Arrays.asList(extractListElements(cmd.getOptionValue("op"))));
    }
    public static void main(String[] args) {
        parseArgs(args);

        for (Tariff tariff : fltr.get(Comparator.comparing(Tariff::getMonthRub))) {
            System.out.println(tariff.toString() + "\n");
        }
    }
}