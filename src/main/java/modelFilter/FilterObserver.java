/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelFilter;

import java.util.List;
import modelReport.Movement;

/**
 *
 * @author villa
 */

public interface FilterObserver {
    void update(List<Movement> filtered);
}
