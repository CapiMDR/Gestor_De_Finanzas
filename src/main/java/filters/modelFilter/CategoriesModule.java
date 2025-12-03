package filters.modelFilter;

import java.util.List;
import javax.swing.JFrame;
import accounts.account_model.Account;
import filters.controllerFilter.FilterController;
import filters.viewFilter.CategoriesView;

public class CategoriesModule {
    
    public static void initCategories(Account selectedAccount) {
        CategoriesView categoriesView = new CategoriesView();
        
        JFrame frame = new JFrame("Categorías");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(categoriesView);
        frame.setLocationRelativeTo(null);
        
        // Crear controlador y pasar la cuenta seleccionada
        FilterController controller = new FilterController();
        if (selectedAccount != null) {
            controller.setViewModule(categoriesView, selectedAccount);
        } else {
            System.out.println("No se seleccionó cuenta");
        }
        
        frame.setVisible(true);
    }
}