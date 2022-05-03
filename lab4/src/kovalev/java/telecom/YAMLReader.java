package telecom;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YAMLReader {

    private Path yamlFolder;
    private final static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public YAMLReader(String path) {
        yamlFolder = FileSystems.getDefault().getPath(path);
    }

    private static YAMLOperator readYaml(File yml) {
        try {
            return mapper.readValue(yml, YAMLOperator.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private YAMLOperator[] readYamlFolder() {
        ArrayList<YAMLOperator> opList = new ArrayList<YAMLOperator>();
        try {
            Files.walk(yamlFolder)
                 .filter(name -> name.toString().endsWith(".csv"))
                 .forEach(t -> {
                    try {
                        opList.add(readYaml(t.toFile()));
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                });
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return opList.toArray(YAMLOperator[]::new);
    }

    public Tariff[] getTariffs() {
        ArrayList<Tariff> tariffs = new ArrayList<Tariff>();

        YAMLOperator[] yamlOps = readYamlFolder();
        for (YAMLOperator yamlOp : yamlOps) {
            tariffs.addAll(Arrays.asList(yamlOp.toTariffs()));
        }

        return tariffs.toArray(Tariff[]::new);
    }

}
