package detect.word2vec;

//import org.json.simple.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;

public class Word2Vec {


    public static void invokePythonProcess(String jsonString, String size, String pythonFilePath) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonFilePath, jsonString, size);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String lines = null;
        while ((lines=reader.readLine()) != null) {
            System.out.println("lines " + lines);
        }

        while ((lines= readers.readLine()) != null) {
            System.out.println("Error lines" + lines);
        }


//        List<String> results = readProcessOutput(process.getInputStream());
    }

    public static String convertDataToJsonFormat(List<String> describedLocator, List<List<String>> describedElements) {
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray(describedLocator);
        object.put(String.valueOf(-1), jsonArray);
        int len = describedElements.size();
        for (int i = 0; i < len; i++) {
            object.put(String.valueOf(i), new JSONArray(describedElements.get(i)));
        }
        String objectToSTring = object.toString();
        return objectToSTring;
    }


    public static void main(String[] args) {
        List<String> describedLocator = Arrays.asList("sign", "in");
        List<String> login = Arrays.asList("login", "button", "login", "login", "submit");
        List<String> username = Arrays.asList("username", "username", "user", "name");
        List<String> password = Arrays.asList("password", "password");
        List<List<String>> describedElements = Arrays.asList(username, password, login);
        String jsonString = convertDataToJsonFormat(describedLocator, describedElements);
        System.out.println(jsonString);
        try {
            invokePythonProcess(jsonString, String.valueOf(3), "E:\\LAB UI\\UITestingLocatorDetector\\src\\main\\java\\detect\\word2vec\\findBestElement.py");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
