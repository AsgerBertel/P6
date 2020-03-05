package TweetCleaner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.JsonObject;

public class JsonCleaner {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("customer.json"));

            // create parser
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);

            // read customer details
            BigDecimal id = (BigDecimal) parser.get("id");
          //  String name = (String) parser.get("cr");
         //   String email = (String) parser.get("email");
            BigDecimal age = (BigDecimal) parser.get("age");

            System.out.println(id);
        //    System.out.println(name);
        //    System.out.println(email);
            System.out.println(age);

            // read address
            Map<Object, Object> address = (Map<Object, Object>) parser.get("address");
            address.forEach((key, value) -> System.out.println(key + ": " + value));

            // read payment method
            JsonArray paymentMethods = (JsonArray) parser.get("paymentMethods");
            paymentMethods.forEach(System.out::println);

            // read projects
            JsonArray projects = (JsonArray) parser.get("projects");
            projects.forEach(entry -> {
                JsonObject project = (JsonObject) entry;
                System.out.println(project.get("title"));
                System.out.println(project.get("budget"));
            });

            //close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
    Structure of a regular tweet:
    {
        "created_at": "Thu May 10 15:24:15 +0000 2018",
            "id_str": "850006245121695744",
            "text": "Here is the Tweet message.",
            "user": {
    },
        "place": {
    },
        "entities": {
    },
        "extended_entities": {
    }
    }

     */

}
