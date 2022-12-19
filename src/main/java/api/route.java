package api;


import lop.exception.DimensionMismatchException;
import java.io.File;
import lop.neural.MLPerceptron;
import lop.utilities.CsvReader;
import lop.utilities.Matrix;
import lop.utilities.Pair;
import lop.utilities.SerializationUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static spark.Spark.*;


public class route {
    /**
     *
     * @param request
     * @return
     */
    public static Pair<Matrix, List<String>> loadMatrix(Request request) {
        JSONObject body = new JSONObject(request.body());

        String data = body.getString("data");
        CsvReader csvReader = new CsvReader(data, ",");
        return csvReader.read();
    }

    /**
     *
     * @param request
     * @param nFeatures
     * @return
     */
    public static MLPerceptron createNeural(Request request, int nFeatures) {
        JSONObject body = new JSONObject(request.body());
        String hiddenLayer = body.getString("hidden-layer") + " 1";
        int[] layer = Stream.concat(
                        Stream.of(nFeatures),
                        Stream.of(hiddenLayer.split(" ")
                                ).
                                map(Integer::parseInt)).
                mapToInt(Integer::intValue).
                toArray();
        return new MLPerceptron(layer.length, layer);
    }

    /**
     * @param request
     * @param neuron
     * @param data
     * @throws DimensionMismatchException
     * @throws IOException
     */
    public static void fitNeuron(Request request,
                                 MLPerceptron neuron,
                                 Pair<Matrix, List<String>> data) throws
            DimensionMismatchException,
            IOException {
        JSONObject body = new JSONObject(request.body());
        String targetColumn = (String) body.get("target");
        int targetColumnIndex = data.getValue().indexOf(targetColumn);
        Matrix y = data.getKey().getColumn(targetColumnIndex);
        Matrix xData = data.getKey().dropColumn(targetColumnIndex);
        neuron.fit(Matrix.transpose(xData), Matrix.transpose(y), body.getInt("epoch"));
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("features", data.getValue().stream().filter(s -> !s.equals(targetColumn)).collect(Collectors.toList()));
        headers.put("label", Collections.singletonList(targetColumn));

        Path userModelPath = Paths.get("model/" + body.get("user"));
        Files.createDirectories(userModelPath);


        SerializationUtil.serialize(
                neuron,
                userModelPath.toAbsolutePath().toString() + "/" + body.get("model-name") + ".model"
        );
        SerializationUtil.serialize(
                headers,
                userModelPath.toAbsolutePath().toString() + "/" + body.get("model-name") + ".header"
        );
    }

    public static void main(String[] args) {
        port(8000);

        /**
         * Affiche la liste de tous les modèles d'un utilisateur données
         * @apiNote GET /model/:userid
         */
        get("/models/:userid", (request, response) -> {
            response.type("application/json");
            File directoryPath = new File("model/" + request.params(":userid"));
            JSONArray modelList = new JSONArray(Objects.requireNonNull(directoryPath.list()));
            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("data", modelList);
            return res.toString();
        });

        /**
         * Crée un nouveau modèle pour un utilisateur donné
         * @apiNote POST /model/:userid
         */
        post("/models/:userid", (request, response) -> {
            Pair<Matrix, List<String>> data = loadMatrix(request);
            MLPerceptron neural = createNeural(request, data.getKey().getData()[0].length - 1);
            fitNeuron(request, neural, data);

            response.type("application/json");
            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("message", "model created");
            return res.toString();
        });

        /**
         * Obtient les différents champs des réseaux de neurones
         * @apiNote GET /model/:modelid/fields
         */
        get("/model/:modelid/fields", (request, response) -> {
            response.type("application/json");
            JSONObject body = new JSONObject(request.body());
            Map<String, List<String>> headers = (Map<String, List<String>>) (SerializationUtil.deserialize(
                    "model/" + body.get("userid") + "/" + request.params("modelid") + ".header"
            ));


            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("data", headers);
            return res.toString();
        });
        /**
         * Prédit les valeurs à base d'un modèle
         * @apiNote GET /model/:modelid
         */
        get("/model/:modelid", (request, response) -> {
            response.type("application/json");
            JSONObject body = new JSONObject(request.body());
            MLPerceptron neural = (MLPerceptron) (SerializationUtil.deserialize(
                    "model/" + body.get("userid") + "/" + request.params("modelid") + ".model"
            ));

            double[] data = body.getJSONArray("data").toList().
                    stream().
                    mapToDouble(x -> Double.parseDouble(x.toString())).
                    toArray();
            Matrix answer = neural.predict(Matrix.fromArray(data));
            JSONObject res = new JSONObject();
            res.put("statut", "success");
            res.put("data", Arrays.stream(answer.data).flatMapToDouble(Arrays::stream).toArray());
            return res.toString();
        });
    }
}
