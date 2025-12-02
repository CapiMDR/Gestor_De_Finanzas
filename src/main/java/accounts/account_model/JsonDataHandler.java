package accounts.account_model;

import org.json.JSONArray;
import org.json.JSONObject;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import goals.goals_model.Goal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JsonDataHandler {
    private static final String FILE_PATH = "accounts_data.json";
    private static final String CATEGORIES_FILE_PATH = "categories_data.json";

    // SERIALIZATION

    // Account list to JSONArray
    public JSONArray accountsToJson(List<Account> accounts) {
        JSONArray jsonArray = new JSONArray();

        for (Account account : accounts) {
            JSONObject accountJson = new JSONObject();
            accountJson.put("id", account.getId());
            accountJson.put("name", account.getName());
            accountJson.put("type", account.getType());
            accountJson.put("coin", account.getCoin());
            accountJson.put("initialBalance", account.getInitialBalance());

            accountJson.put("movements", movementsToJson(account.getMovements()));
            accountJson.put("goals", goalsToJson(account.getGoals()));

            jsonArray.put(accountJson);
        }
        return jsonArray;
    }

    // movement list to JSONArray
    private JSONArray movementsToJson(List<Movement> movements) {
        JSONArray jsonArray = new JSONArray();
        for (Movement movement : movements) {
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

    // goals list to JSONArray
    private JSONArray goalsToJson(List<Goal> goals){
        JSONArray jsonArray = new JSONArray();

        for (Goal goal : goals) {
            JSONObject goalJson = new JSONObject();
            goalJson.put("name", goal.getName());
            goalJson.put("targetAmount", goal.getTargetAmount());
            goalJson.put("amount", goal.getCurrentAmount());
            goalJson.put("currentAmount", goal.getCurrentAmount());
            goalJson.put("description", goal.getDescription());

            jsonArray.put(goalJson);
        }

        return jsonArray;
    }

    // categories to hashMap
    public JSONArray categoriesToJson(HashMap<String, MovementCategory> categories) {
        JSONArray jsonArray = new JSONArray();

        for (MovementCategory category : categories.values()) {
            JSONObject categoryJson = new JSONObject();
            categoryJson.put("name", category.getName());
            categoryJson.put("type", category.getType().name());
            jsonArray.put(categoryJson);
        }
        return jsonArray;
    }

    // DESERIALIZATION

    // JSONArray to acount list
    public List<Account> jsonToAccounts(JSONArray jsonArray) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
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

            JSONArray goalsJson = accountJson.getJSONArray("goals");
            List<Goal> goals = jsonToGoals(goalsJson);

            account.setMovements(movements);
            account.setGoals(goals);

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

    // JSONArray to movement list
    private List<Movement> jsonToMovements(JSONArray movementsJson, Account account) {
        List<Movement> movements = new ArrayList<>();
        for (int i = 0; i < movementsJson.length(); i++){
            JSONObject movementJson = movementsJson.getJSONObject(i);

            UUID idMovement = UUID.fromString(movementJson.getString("idMovement"));
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

    //JSONArray to goals list
    private List<Goal> jsonToGoals(JSONArray goalsJson){
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < goalsJson.length(); i++){
            JSONObject goalJson = goalsJson.getJSONObject(i);

            String name = goalJson.getString("name");
            
            String targetAmountStr = goalJson.get("targetAmount").toString();
            BigDecimal targetAmount = new BigDecimal(targetAmountStr);

            String description = goalJson.getString("description");

            Goal goal = new Goal(name, targetAmount, description);

            goals.add(goal);
        }
        return goals;
    }
    // HashMap to categories
    public HashMap<String, MovementCategory> jsonToCategories(JSONArray jsonArray) {
        HashMap<String, MovementCategory> categories = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject categoryJson = jsonArray.getJSONObject(i);

            String name = categoryJson.getString("name");
            MovementType type = MovementType.valueOf(categoryJson.getString("type"));

            MovementCategory category = new MovementCategory(name, type);
            categories.put(name, category);
        }
        return categories;
    }

    // file operations
    public void saveAccounts(List<Account> accounts) {
        JSONArray jsonArray = accountsToJson(accounts);
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonArray.toString(2));
            System.out.println("Cuentas guardadas en " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCategories(HashMap<String, MovementCategory> categories) {
        JSONArray jsonArray = categoriesToJson(categories);
        try (FileWriter file = new FileWriter(CATEGORIES_FILE_PATH)) {
            file.write(jsonArray.toString(2));
            System.out.println("Categorías guardadas en " + CATEGORIES_FILE_PATH);
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

    public HashMap<String, MovementCategory> loadCategories() {
        File file = new File(CATEGORIES_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo de categorías no encontrado o vacío. Iniciando con lista vacía.");
            return new HashMap<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(CATEGORIES_FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            return jsonToCategories(jsonArray);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de categorías: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al parsear el JSON de categorías: " + e.getMessage());
        }
        return new HashMap<>();
    }
}