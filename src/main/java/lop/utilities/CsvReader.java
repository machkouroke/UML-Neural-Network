package lop.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader {
    private String path;
    private String separator;
    private String[] header;


    public CsvReader(String path, String separator) {
        this.path = path;
    }

    public CsvReader(String path) {
        this.path = path;
        this.separator = ",";
    }

    public Matrix read() throws FileNotFoundException {
        File file = new File(this.path);
        FileReader fr = new FileReader(file);
        List<List<Double>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(fr)) {
            String line;
            header = br.readLine().split(separator);
            while ((line = br.readLine()) != null) {
                data.add(Arrays.stream(line.split(this.separator)).
                        map(Double::parseDouble).
                        collect(Collectors.toList()));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Matrix(data);

    }
}
