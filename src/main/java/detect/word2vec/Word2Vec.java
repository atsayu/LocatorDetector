package detect.word2vec;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;

public class Word2Vec {


    public static int invokePythonProcess(String jsonString, String size, String pythonFilePath) throws Exception {
        int result = -1;
        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonFilePath, size);
        Map<String, String> env = processBuilder.environment();
        env.put("JSON_DATA", jsonString);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String lines = null;
        while ((lines=reader.readLine()) != null) {
            System.out.println("lines " + lines);
            result = Integer.valueOf(lines);
        }

        while ((lines= readers.readLine()) != null) {
            System.out.println("Error lines" + lines);
        }
        return result;
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
//        String jsonString = convertDataToJsonFormat(describedLocator, describedElements);
        String jsonString = "{\"0\":[\"ranorex\",\"webtest\",\"wordpress\",\"org\",\"powered\",\"by\",\"word\",\"press\"],\"1\":[\"webtest\",\"ranorex\",\"org\",\"wp\",\"login\",\"php\",\"action\",\"lostpassword\",\"password\",\"lost\",\"found\"],\"2\":[\"back\",\"ranorex\",\"webtest\",\"webtest\",\"ranorex\",\"org\",\"are\",\"you\",\"lost\"],\"3\":[\"submit\",\"wp\",\"submit\",\"wp\",\"submit\",\"button\",\"button\",\"primary\",\"button\",\"large\",\"log\",\"in\"],\"-1\":[\"sign\", \"in\"]}";
        System.out.println(jsonString);
        try {
            int result = invokePythonProcess(jsonString, String.valueOf(4), "E:\\LAB UI\\UITestingLocatorDetector\\src\\main\\java\\detect\\word2vec\\findBestElement.py");
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}