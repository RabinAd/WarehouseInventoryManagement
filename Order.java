

import java.util.*;
import java.io.*;
public class Order implements Serializable {
  private static final long serialVersionUID = 1L;
    
  private double totalCost;
  private Client client;
  private Product product;
  
  private int orderedQuantity;
  
  private String id;
  private static final String ORDER_STRING = "O";
  public Order (Client c, Product p, int quantity) {
    this.client = c;
    this.product = p;
    this.orderedQuantity = quantity;
    this.totalCost = p.getSalesPrice() * quantity;
    id = ORDER_STRING + (OrderIdServer.instance()).getId();
  }

  public String getId(){
      return id;
  }
  
  public double getTotalCost() {
    return totalCost;
  }
  
  public Client getClient() {
    return client;
  }
  
  public Product getProduct() {
    return product;
  }
  
  public int getOrderedQuantity(){
      return orderedQuantity; 
  }
  
  public void setOrderedQuantity(int newQuantity){
      orderedQuantity = newQuantity;
  }
  
  public String toString(){
      String string = "Oid: " + id + " | Ordered By:  " + client.getName() + " (" + client.getId()   
                      + ") | Product (Pid): " + product.getId() + "): " + product.getName() 
                      + " | Quantity: " + orderedQuantity + " | Totalcost: $" + totalCost;
      return string;
  }
}
