package motorDeInstrucciones;
import java.util.ArrayList;
import java.util.List;

public class Broker {
   private List<Order> orderList = new ArrayList<Order>(); 
   
   public void takeOrder(Order order) {
	   orderList.add(order);
   }
   
   public void placeOrders() {
	   for(Order order : orderList) {
		   System.out.println(order.execute());
	   }
	   orderList.clear();
   }	   
}
