/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author villa
 */
public class ReportGenerator {

    private ReportSubject reportSubject;
    private JSONControllerPersistence persistence;

    public ReportGenerator(ReportSubject subject, JSONControllerPersistence persistence) {
        this.reportSubject = subject;
        this.persistence = persistence;
    }

    public void today() {
        
        LocalDate today = LocalDate.now();
        List<Movement> movements = persistence.loadAllMovements().stream()
                .filter(m-> 
                    m.getDate().toLocalDate().isEqual(today))
                .collect(Collectors.toList());
        double total = amountTotal(movements);

        ReportData data = new ReportData(
            "Today",
            movements,
            total,
            today.toString()
        );
       reportSubject.notifyObservers(data);
     
    }

    public void weekAgo() {
        LocalDate start = LocalDate.now().minusDays(7);
        System.out.println(start);
        LocalDate end = LocalDate.now();
       List<Movement> movements = persistence.loadAllMovements().stream()
            .filter(m -> {
                LocalDate date = m.getDate().toLocalDate();
                return ( !date.isBefore(start) && !date.isAfter(end) );
            })
            .collect(Collectors.toList());

    double total = amountTotal(movements);

    ReportData data = new ReportData(
        "Week Ago",
        movements,
        total,
        start.toString() + "-" + end.toString()
    );
    
       reportSubject.notifyObservers(data);
    }

    public void selectPeriod(LocalDate start, LocalDate end) {

    List<Movement> movements = persistence.loadAllMovements().stream()
            .filter(m -> {
                LocalDate date = m.getDate().toLocalDate();
                return ( !date.isBefore(start) && !date.isAfter(end) );
            })
            .collect(Collectors.toList());

    double total = amountTotal(movements);

    ReportData data = new ReportData(
        "Selected Period",
        movements,
        total,
        start.toString() + "-" + end.toString()
    );

    reportSubject.notifyObservers(data);
}
    
    private double amountTotal(List<Movement> movements){ double total = 0; for(Movement m : movements){ total = total + m.getAmount(); }
         
    return total;
    }
   
    public void addObserver(ReportObserver obs) {
        reportSubject.add(obs);
    }
}   