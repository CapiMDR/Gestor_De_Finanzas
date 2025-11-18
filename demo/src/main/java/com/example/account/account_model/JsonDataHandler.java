package com.example.account.account_model;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.MovementCategory.MovementType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonDataHandler {
    private static final String FILE_PATH = "accounts_data.json";

    //SERIALIZATION

    //Account list to JSONArray
    public JSONArray accountsToJson(List<Account> accounts){
        JSONArray jsonArray = new JSONArray();

        for(Account account : accounts){
            JSONObject accountJson = new JSONObject();
            accountJson.put("id", account.getId());
            accountJson.put("name", account.getName());
            accountJson.put("type", account.getType());
            accountJson.put("coin", account.getCoin());
            accountJson.put("initialBalance", account.getInitialBalance());

            accountJson.put("movements", movementsToJson(account.getMovements()));

            jsonArray.put(accountJson);
        }
        return jsonArray;
    }
    //movement list to JSONArray
    private JSONArray movementsToJson(List<Movement> movements){
        JSONArray jsonArray = new JSONArray();
        for(Movement movement : movements){
            JSONObject movementJson = new JSONObject();
            movementJson.put("idMovement", movement.getIdMovement());
            movementJson.put("description", movement.getDescription());
            movementJson.put("amount", movement.getAmount().toString());
            movementJson.put("date", movement.getDate().toString());
            movementJson.put("categoryName", movement.getCategory().getName());
            movementJson.put("categoryType", movement.getCategory().getType().name());

            jsonArray.put(movementJson);
        }
        return jsonArray;
    }
    //DESERIALIZATION

    //JSONArray to acount list
    public List<Account> jsonToAccounts(JSONArray jsonArray){
        List<Account> accounts = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject accountJson = jsonArray.getJSONObject(i);

            int id = accountJson.getInt("id");
            String name = accountJson.getString("name");
            AccountType type = AccountType.valueOf(accountJson.getString("type"));
            Coin coin = Coin.valueOf(accountJson.getString("coin"));

            String initialBalanceStr = accountJson.get("initialBalance").toString();
            BigDecimal initialBalance = new BigDecimal(initialBalanceStr);


            Account account = new Account(id, name, type, coin, initialBalance);

            JSONArray movementsJson = accountJson.getJSONArray("movements");
            List<Movement> movements = jsonToMovements(movementsJson, account);
            account.setMovements(movements);

            BigDecimal balance = account.getInitialBalance();
            for (Movement mov : movements) {
                if (mov.getCategory().getType() == MovementType.INCOME) {
                    balance = balance.add(mov.getAmount());
                } else {
                    balance = balance.subtract(mov.getAmount());
                }
            }

            account.updateBalance(balance);

            accounts.add(account);
        }
        return accounts;
    }
    //JSONArray to movemet list
    private List<Movement> jsonToMovements(JSONArray movementsJson, Account account) {
        List<Movement> movements = new ArrayList<>();
        for (int i = 0; i < movementsJson.length(); i++) {
            JSONObject movementJson = movementsJson.getJSONObject(i);
            
            int idMovement = movementJson.getInt("idMovement");
            String description = movementJson.getString("description");

            String amountStr = movementJson.get("amount").toString();
            BigDecimal amount = new BigDecimal(amountStr);

            LocalDateTime date = LocalDateTime.parse(movementJson.getString("date"));
            
            String categoryName = movementJson.getString("categoryName");
            MovementType categoryType = MovementType.valueOf(movementJson.getString("categoryType"));
            MovementCategory category = new MovementCategory(categoryName, categoryType);

            Movement movement = new Movement(idMovement, description, amount, category, account, date); 
            
            movements.add(movement);
        }
        return movements;
    }

    //file operations
    public void saveAccounts(List<Account> accounts){
        JSONArray jsonArray = accountsToJson(accounts);
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            // Se guarda la representación como un array de objetos
            file.write(jsonArray.toString(2)); // toString(2) para un formato legible
            System.out.println("Cuentas guardadas en " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Account> loadAccounts() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo de datos no encontrado o vacío. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            return jsonToAccounts(jsonArray);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
             System.err.println("Error al parsear el JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
