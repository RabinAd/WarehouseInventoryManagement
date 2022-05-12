import java.util.*;
import java.io.*;
import java.lang.*;
public class Cart implements Serializable{
	private static final long serialVersionUID = 1L;
	private String productID;
	private int quantity;

	public Cart(String itemID, int quantity){
		productID = itemID;
		this.quantity = quantity;
	}//end TestItem

	public void changeQuantity(int quantity){
		this.quantity = quantity;
	}//end changeQuantity

	public String getProduct(){
		return productID;
	}//end getProduct()

	public int getQuantity(){
		return quantity;
	}//end getQuantity()

	public String toString(){
		return "Product Id: " + productID + " | Quantity: " + quantity + '\n';
	}//end toString
}
