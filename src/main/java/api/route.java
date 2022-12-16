package api;

import lop.exception.DimensionMismatchException;
import lop.neural.MLPerceptron;
import lop.utilities.Matrix;
import org.json.JSONArray;
import org.json.JSONObject;

import static spark.Spark.get;
import static spark.Spark.port;


public class route {
    public static void main(String[] args) throws DimensionMismatchException {
        port(8080);
        double[][] x_train = {
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1}
        };
        double[][] y = {
                {0}, {1}, {1}, {0}
        };
        MLPerceptron nn = new MLPerceptron(3, new int[]{2, 2, 1});  // 2 inputs, 2 hidden layers, 1 output
        nn.fit(Matrix.transpose(new Matrix(x_train)), Matrix.transpose(new Matrix(y)), 50000);
        JSONObject neural = new JSONObject(nn);
        System.out.println(neural);
        JSONObject jo = new JSONObject();
        jo.put("statut", "success");
        JSONArray ja = new JSONArray();
        ja.put("Chien et Chat");
        ja.put("Pokemon");
        jo.put("data", ja);
        get("/list-model", (request, response) -> {
            response.type("application/json");
            return jo.toString();
        });
    }
}
