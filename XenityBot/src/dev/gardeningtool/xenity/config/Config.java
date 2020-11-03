package dev.gardeningtool.xenity.config;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

@Getter
public class Config {

    private String botStatus;
    private String botPrefix;
    private String botToken;
    private String mysqlUrl;
    private String mysqlUsername;
    private String mysqlPassword;
    private String mysqlDatabase;
    private int mysqlPort;

    public Config() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
            botStatus = (String) jsonObject.get("Status");
            botPrefix = (String) jsonObject.get("Prefix");
            botToken = (String) jsonObject.get("Token");
            mysqlUrl = (String) jsonObject.get("URL");
            mysqlUsername = (String) jsonObject.get("Username");
            mysqlPassword = (String) jsonObject.get("Password");
            mysqlDatabase = (String) jsonObject.get("Database");
            mysqlPort = (int) (long) jsonObject.get("Port");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
