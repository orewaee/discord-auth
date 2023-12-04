package dev.orewaee.utils;

import java.io.FileWriter;
import java.io.Writer;

import com.google.gson.Gson;

import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.TomlConfig;

public class FileHelper {
    public static void update() {
        try {
            String fileName = TomlConfig.getInstance().accountsFileName();
            Writer writer = new FileWriter(fileName);

            Gson gson = new Gson();

            gson.toJson(JsonAccountManager.getInstance().getAccounts(), writer);

            writer.close();
        } catch (Exception exception) {
            System.out.println("Error 2");
        }
    }
}
