package pages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderDemoTest {


    @DataProvider(name = "jsonData")
    public Object[][] jsonData() throws IOException {
        String jsonContent = new String(Files.readAllBytes(
                Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData_TC1.json")
        ));
        Type type = new TypeToken<List<HashMap<String, String>>>() {}.getType();
        List<HashMap<String, String>> data = new Gson().fromJson(jsonContent, type);

        Object[][] table = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            table[i][0] = data.get(i);
        }
        return table;
    }

    @Test(dataProvider = "jsonData")
    public void basicDataTest(HashMap<String, String> data) {
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }
}
