package accounts.account_model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Class in charge of serializing and deserializing data related to accounts,
 * movements, goals and categories to and from JSON files.
 *
 * @author Martín Jesús Pool Chuc
 */
public class JsonDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataHandler.class);

    /** Path of the file where account data is saved. */
    private static final String FILE_PATH = "accounts_data.json";

    /** Path of the file where category data is saved. */
    private static final String CATEGORIES_FILE_PATH = "categories_data.json";

    // SERIALIZATION

    /**
     * Converts a list of accounts into a JSONArray.
     *
     * @param accounts list of accounts to convert
     * @return JSONArray representing the accounts
     */
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

    /**
     * Converts a list of movements into a JSONArray.
     *
     * @param movements list of movements
     * @return JSONArray representing movements
     */
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

    /**
     * Converts a list of goals into a JSONArray.
     *
     * @param goals list of goals
     * @return JSONArray representing goals
     */
    private JSONArray goalsToJson(List<Goal> goals) {
        JSONArray jsonArray = new JSONArray();

        for (Goal goal : goals) {
            JSONObject goalJson = new JSONObject();
            goalJson.put("name", goal.getName());
            goalJson.put("targetAmount", goal.getTargetAmount());
            // Removed the put of the current amount, since it is not necessary
            goalJson.put("currentAmount", goal.getCurrentAmount());
            goalJson.put("description", goal.getDescription());

            jsonArray.put(goalJson);
        }

        return jsonArray;
    }

    /**
     * Converts a map of categories into a JSONArray.
     *
     * @param categories map of categories
     * @return JSONArray representing categories
     */
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

    /**
     * Converts a JSONArray into a list of accounts.
     *
     * @param jsonArray JSONArray with account data
     * @return list of deserialized accounts
     */
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

    /**
     * Converts a JSONArray into a list of movements.
     *
     * @param movementsJson JSONArray of movements
     * @param account account associated with the movement
     * @return list of deserialized movements
     */
    private List<Movement> jsonToMovements(JSONArray movementsJson, Account account) {
        List<Movement> movements = new ArrayList<>();
        for (int i = 0; i < movementsJson.length(); i++) {
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

    /**
     * Converts a JSONArray into a list of goals.
     *
     * @param goalsJson JSONArray with goals
     * @return list of deserialized goals
     */
    private List<Goal> jsonToGoals(JSONArray goalsJson) {
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < goalsJson.length(); i++) {
            JSONObject goalJson = goalsJson.getJSONObject(i);

            String name = goalJson.getString("name");

            String targetAmountStr = goalJson.get("targetAmount").toString();
            BigDecimal targetAmount = new BigDecimal(targetAmountStr);

            String currentAmountStr = goalJson.optString("currentAmount", "0");
            String description = goalJson.getString("description");

            Goal goal = new Goal(name, targetAmount, description);
            goal.setCurrentAmount(new BigDecimal(currentAmountStr));

            goals.add(goal);
        }
        return goals;
    }

    /**
     * Converts a JSONArray into a map of categories.
     *
     * @param jsonArray JSONArray with categories
     * @return map of categories
     */
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

    /**
     * Saves the list of accounts into a JSON file.
     *
     * @param accounts list of accounts to save
     */
    public void saveAccounts(List<Account> accounts) {
        JSONArray jsonArray = accountsToJson(accounts);
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonArray.toString(2));
            logger.info("Accounts saved successfully — {} account(s).", accounts.size());
        } catch (IOException e) {
            logger.error("Error saving accounts to file: {}", e.getMessage(), e);
        }
    }

    /**
     * Saves the categories into a JSON file.
     *
     * @param categories map of categories
     */
    public void saveCategories(HashMap<String, MovementCategory> categories) {
        JSONArray jsonArray = categoriesToJson(categories);
        try (FileWriter file = new FileWriter(CATEGORIES_FILE_PATH)) {
            file.write(jsonArray.toString(2));
            logger.info("Categories saved successfully — {} category/categories.", categories.size());
        } catch (IOException e) {
            logger.error("Error saving categories to file: {}", e.getMessage(), e);
        }
    }

    /**
     * Loads the accounts from the JSON file.
     *
     * @return list of loaded accounts or an empty list in case of error
     */
    public List<Account> loadAccounts() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            List<Account> loaded = jsonToAccounts(jsonArray);
            logger.info("Accounts loaded successfully — {} account(s).", loaded.size());
            return loaded;
        } catch (IOException e) {
            logger.error("Error reading accounts file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error parsing accounts JSON: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * Loads the categories from the JSON file.
     *
     * @return map of loaded categories or empty map in case of error
     */
    public HashMap<String, MovementCategory> loadCategories() {
        File file = new File(CATEGORIES_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(CATEGORIES_FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            HashMap<String, MovementCategory> cats = jsonToCategories(jsonArray);
            logger.info("Categories loaded successfully — {} category/categories.", cats.size());
            return cats;
        } catch (IOException e) {
            logger.error("Error reading categories file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error parsing categories JSON: {}", e.getMessage(), e);
        }
        return new HashMap<>();
    }
}