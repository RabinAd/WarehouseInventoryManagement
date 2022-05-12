

import java.util.*;

import javax.swing.LookAndFeel;

import java.text.*;
import java.io.*;
public class Managerstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static Managerstate instance;
  private static final int EXIT = 0;
    private static final int ADD_PRODUCTS = 1;
    private static final int ADD_SUPPLIER = 2;
    private static final int LIST_SUPPLIERS= 4;
    private static final int LIST_SUPPLIERS_FOR_PRODUCT = 5;
    private static final int LIST_PRODUCTS_FOR_MANUFACTURER= 6;
    private static final int MODIFY_PRICE = 7;
  private static final int CLERKMENU = 8;
  private static final int HELP = 9;

  private boolean running;
  private int exitCode;

  private Managerstate() {
      super();
      warehouse = Warehouse.instance();
      //context = WarehouseContext.instance();
  }

  public static Managerstate instance() {
    if (instance == null) {
      instance = new Managerstate();
    }
    return instance;
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + "Enter Y or y for yes");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Enter the number: ");
      }
    } while (true);
  }

  public double getFloat(String prompt)
  {
      do {
            try
            {
                String item = getToken(prompt);
                Double decimal = Double.valueOf(item);
                return decimal.doubleValue();
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("Enter the number: ");
            }
      } while (true);
  }

  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Enter the date (mm/dd/yy format)");
      }
    } while (true);
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command: " + HELP + " for help" ));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }


  public void help() {
    System.out.println();
    System.out.println("ManagerState Menu");
    System.out.println("==============================");
    System.out.println("Enter the number to select the menu as explained");
    System.out.println(EXIT + " to exit");
    System.out.println(ADD_PRODUCTS + " to add product");
    System.out.println(ADD_SUPPLIER + " to add Supplier");
    System.out.println(LIST_SUPPLIERS+ " to list Supplier");
    System.out.println(LIST_SUPPLIERS_FOR_PRODUCT+ " to list supplier for a product");
    System.out.println(LIST_PRODUCTS_FOR_MANUFACTURER+ " to list product for a supplier");
    System.out.println(MODIFY_PRICE + " to modify the price");
  
    System.out.println(CLERKMENU + " to switch to the CLERK MENU");
    System.out.println(HELP + " for help");
  }


  //addProduct
      public void addProducts()
      {
          Product result;

          do{
              String name = getToken("Enter product name");
              int quantity = getNumber("Enter in quantity");
              double price = getFloat("Enter in price");
              if(quantity < 0 || price < 0){
                  System.out.println("Invalid input for quantity and/or price: TRY AGAIN");
                  continue;
              }
              result = warehouse.addProduct(name, quantity, price);

              if(result != null)
              {
                  System.out.println(result);
              }
              else
              {
                  System.out.println("Product could not be added");
              }

              if(!yesOrNo("Add more products?"))
              {
                  break;
              }
          } while (true);
      }

      //Prints a list of Manufacturers
    public void listManufacturers()
    {
        Iterator allManufacturers = warehouse.getManufacturers();
        System.out.println("Supplier  List");
        System.out.println("=========================");
        while(allManufacturers.hasNext())
        {
            Manufacturer manufacturer = (Manufacturer)(allManufacturers.next());
            System.out.println(manufacturer.toString());
        }
    }



public void modifyPrice(){
  String pid = getToken("Enter product ID: ");
  Product product = warehouse.searchProduct(pid);
String sid= getToken("Enter supplier ID( example M1): ");
  Manufacturer manufacturer = warehouse.searchManufacturer(sid);

  if(product == null){
      System.out.println("Product does not exists.");
      return;
  }

  double price = getFloat("Enter the new price: ");

  if(warehouse.modifyPrice(pid,sid, price) == false){
      System.out.println("Failed.");
  }
  else
  {
      System.out.println("Updated the new price.");
  }
}

    public void addManufacturer()
    {
        String name = getToken("Enter manufacturer name: " );
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone number: ");
        Manufacturer result;
        result = warehouse.addManufacturer(name, address, phone);

        if(result == null)
        {
            System.out.println("Failed to add.");
        }

        System.out.println(result);
    }
    //listSuppliersForProduct
    public void listSuppliersForProduct()
    {
        String id = getToken("Enter Product Id (Example. P1):");
        Iterator result = warehouse.getSuppliersForProduct(id);
        System.out.println("Suppliers for Product: " + id);
        System.out.println("=========================");

        if(result == null){
            System.out.println("No Data: Exiting Action");
            return;
        }

        while (result.hasNext())
        {
            System.out.println(result.next());
        }
    }

    //Lists all products supplied by a manufacturer
    public void listProductsForManufacturer()
    {
        String id = getToken("Enter Manufacturer Id (Example. M1):");
        Iterator result = warehouse.getProductsFromManufacturer(id);

        if(result == null){
            System.out.println("No Data: Exiting Action");
            return;
        }

        System.out.println("Products supplied by manufacturer: " + id);
        System.out.println("=========================");
        while (result.hasNext())
        {
            Product product = (Product)(result.next());
            System.out.println("ID: " + product.getId() + " | Name: " + product.getName());
        }
    }

  public void clerkmenu() {
    //Login in to Clerk State
    (WarehouseContext.instance()).changeState(2);

    //exitCode = 1;
    //running = false;
  }


  public void logout() {
      //Logout to Login State
      (WarehouseContext.instance()).changeState(0);

      //running = false;
      //exitCode = 0;
  }


  public void terminate(){
        (WarehouseContext.instance()).changeState(exitCode);
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {

        case ADD_PRODUCTS:                   addProducts();
                                                            break;
      case ADD_SUPPLIER:                     addManufacturer();
                                                      break;
        case LIST_SUPPLIERS:                 listManufacturers();

                                                    break;
      case LIST_SUPPLIERS_FOR_PRODUCT:        listSuppliersForProduct();
                                                    break;
      case LIST_PRODUCTS_FOR_MANUFACTURER:   listProductsForManufacturer();
                                                    break;

      case MODIFY_PRICE:                  modifyPrice();
                                            break;

        case CLERKMENU:                     clerkmenu();
                                            break;
        case HELP:                          help();
                                            break;
      }
    }
    logout();
  }


  public void run() {
      //running = true;
      process();
  }
}
