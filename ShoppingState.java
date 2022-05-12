import java.util.*;
import java.text.*;
import java.io.*;
public class ShoppingState extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private boolean running;
  private int exitCode;
  private static ShoppingState instance;
  private static final int EXIT = 0;
  private static final int SHOW_CART_ITEMS = 1;
  private static final int ADD_PRODUCT= 2;
  private static final int REMOVE_PRODUCT = 3;
  private static final int MODIFY_QUANTITY = 4;
  private static final int CLIENT_MENU = 5;
  private static final int HELP = 13;
  private ShoppingState() {
      super();
      warehouse = Warehouse.instance();
      //context = WarehouseContext.instance();
  }

  public static ShoppingState instance() {
    if (instance == null) {
      instance = new ShoppingState();
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
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
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
        System.out.println("Please input a number ");
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
        System.out.println("Please input a date as mm/dd/yy");
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



  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
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
    System.out.println("ShoppingState Menu");
    System.out.println("==============================");
    System.out.println("Enter a number as explained below:");
    System.out.println(EXIT + " to Exit");
    System.out.println(SHOW_CART_ITEMS + " to view cart items");
    System.out.println(ADD_PRODUCT + " to add a product");
  System.out.println(REMOVE_PRODUCT + " to remove product");
  System.out.println(MODIFY_QUANTITY + " to modify quantity");
  System.out.println(CLIENT_MENU + " to switch to CLIENT MENU");



    System.out.println(HELP + " for help");
  }


  public void viewCart() {

    String cid = (WarehouseContext.instance()).getUser();

    Client client = warehouse.searchClient(cid);

    if(client != null)
    {

       

        Iterator processedOrders = client.getOrders();

        if(processedOrders != null)
        {
            System.out.println("==================================");
            System.out.println("| Cart Items :");
            System.out.println("==================================");

            while(processedOrders.hasNext())
            {
                Order order = (Order)(processedOrders.next());
                System.out.println(order.toString());
            }
        }



    }
  }

  public void placeOrder() {
      String cid = (WarehouseContext.instance()).getUser();

      Client client = warehouse.searchClient(cid);

      if(client != null)
      {
          String pid = getToken("Enter Product Id or 0 to stop");

          while(!(pid.equals("0"))){
              int quantity = getNumber("Enter quantity");
              if(quantity <= 0){
                  System.out.println("Invalid Quantity: Try Again");
                  continue;
              }
              boolean result = warehouse.addAndProcessOrder(cid, pid, quantity);
              pid = getToken("Enter Product Id or 0 to stop");
          }
      }
  }

  public void checkProductPrice()
  {
      String id = getToken("Enter Product Id (Example. P1):");

      if (Warehouse.instance().searchProduct(id) != null)
      {
          System.out.println(Warehouse.instance().searchProduct(id));
      }
      else
          System.out.println("Invalid product id.");
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

//Prints a list of Products
    public void listProducts()
    {
        Iterator allProducts = warehouse.getProducts();
        System.out.println("PRODUCT LIST");
        System.out.println("=========================");
        while (allProducts.hasNext())
        {
            Product product = (Product)(allProducts.next());
            System.out.println(product.toString());
        }
    }




        //Adds and process an order by a client
    public void addProcessOrder()
    {
        System.out.println("Modify quantity for a client");
        System.out.println("=========================");
        String cid = getToken("Enter Client Id (Example. C1):");
        
        Client client = warehouse.searchClient(cid);
        if(client != null)
        {
            listProducts();
            String pid = getToken("Enter Product Id or 0 to stop");
            
            while(!(pid.equals("0"))){
                int quantity = getNumber("Enter quantity");
                if(quantity <= 0){
                    System.out.println("Invalid Quantity: Try Again");
                    continue;
                }
                
                boolean result = warehouse.addAndProcessOrder(cid, pid, quantity);
                if(result == false){
                    System.out.println("Invalid Product: " + pid);
                }
                pid = getToken("Enter another Product Id or 0 to stop");
            }
        }
        else
        {
            System.out.println("No Data For: (" + cid + "); EXITING ACTION");
        }
    }


    public void clientMenu(){
      (WarehouseContext.instance()).changeState(1);
    }

  public void setUID_tester(String uID)
  {
      context.setUser(uID);
  }

  public void logout()
  {

    
    
        (WarehouseContext.instance()).changeState(0); // exit code 1, indicates error
  }

  public void terminate()
  {
    context.changeState(exitCode);
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case SHOW_CART_ITEMS : viewCart();
                                break;


        case ADD_PRODUCT: addProducts();
                                break;
        case REMOVE_PRODUCT :

                                break;
        case MODIFY_QUANTITY: addProcessOrder();

                                break;
        case CLIENT_MENU :   clientMenu();
                              break;

        case HELP:              help();
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
