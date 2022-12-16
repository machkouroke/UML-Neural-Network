package api;

import lop.exception.DimensionMismatchException;
import java.io.File;

import lop.utilities.CsvReader;
import lop.utilities.Matrix;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static spark.Spark.*;


public class route {
    public static void main(String[] args) throws DimensionMismatchException, IOException {
        port(8080);
//        double[][] x_train = {
//                {0, 0},
//                {1, 0},
//                {0, 1},
//                {1, 1}
//        };
//        double[][] y = {
//                {0}, {1}, {1}, {0}
//        };
//        MLPerceptron nn = new MLPerceptron(3, new int[]{2, 2, 1});  // 2 inputs, 2 hidden layers, 1 output
//        nn.fit(Matrix.transpose(new Matrix(x_train)), Matrix.transpose(new Matrix(y)), 50000);
//        SerializationUtil.serialize(
//                nn,
//                "model/machkour.model"
//        );
//        JSONObject neural = new JSONObject(nn);
        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist


        get("/list-model/:userid", (request, response) -> {
            response.type("application/json");
            File directoryPath = new File("model/" + request.params(":userid"));
            JSONArray modelList = new JSONArray(Objects.requireNonNull(directoryPath.list()));
            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("data", modelList);
            return res.toString();
        });

        post("/create-model/:userid", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream input = request.raw().getPart("data").getInputStream()) {
                Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                CsvReader csvReader = new CsvReader(tempFile.toString());
                Matrix x_train = csvReader.read();
                System.out.println(x_train);
            }
            String modelName = request.queryParams("model-name");
            response.type("application/json");
            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("data", "model created");
            return res.toString();
        });
    }
}
