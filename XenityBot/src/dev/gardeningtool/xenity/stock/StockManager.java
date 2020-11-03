package dev.gardeningtool.xenity.stock;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class StockManager {

    private HashMap<AccountType, Queue<String>> accounts;

    public StockManager() {
        accounts = new HashMap<>();
        File directory = new File("stock" + File.separator);
        if (!directory.exists()) {
            directory.mkdir();
            throw new RuntimeException("No account stock found!");
        }
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            AccountType accountType = AccountType.getTypeFromName(file.getName().replace(".txt", ""));
            if (accountType == null) continue;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "";
                Queue<String> accounts = new LinkedList<>();
                while ((line = reader.readLine()) != null) {
                    accounts.add(line);
                }
                this.accounts.put(accountType, accounts);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    public String getAlt(AccountType accountType) {
        return accounts.get(accountType).poll();
    }

    public enum AccountType {

        MINECRAFT("Minecraft");

        public static AccountType getTypeFromName(String name) {
            for (AccountType accountType : values()) {
                if (accountType.getName().equalsIgnoreCase(name))
                    return accountType;
            }
            return null;
        }

        @Getter
        private String name;

        AccountType(String name) {
            this.name = name;
        }
    }
}
