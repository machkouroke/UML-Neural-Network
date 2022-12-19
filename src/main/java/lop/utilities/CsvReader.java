package lop.utilities;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvReader {
    private Reader reader;
    private String separator;
    private String[] header;


    public CsvReader(File file, String separator) throws FileNotFoundException {
        this.reader = new FileReader(file);
        this.separator = separator;
    }

    public CsvReader(String content, String separator) {
        this.reader = new StringReader(content);
        this.separator = separator;
    }

    /**
     * Read csv file and return a matrix and a list of header
     *
     * @return Pair<Matrix, List < String>> matrix and header
     */
    public Pair<Matrix, List<String>> read() {
        List<List<Double>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(this.reader)) {
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
        return new Pair<>(new Matrix(data), Stream.of(header).collect(Collectors.toList()));
    }
}
