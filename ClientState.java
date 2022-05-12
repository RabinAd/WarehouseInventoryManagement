

import java.util.*;
import java.text.*;
import java.io.*;
public class ClientState extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private boolean running;
  private int exitCode;
  private static ClientState instance;
  private static final int EXIT = 0;
  private static final int CLIENT_DETAILS = 1;
  private static final int PRODUCT_LIST_WITH_SALEPRICE = 2;
  private static final int CLIENT_TRANSACTIONS = 3;
  private static final int SHOPPING_CART = 4;
   private static final int CLIENT_WAITLIST = 5;
    private static final int CLERK_MENU = 6;
  private static final int HELP = 7;
  private ClientState() {
      super();
      warehouse = Warehouse.instance();
      //context = WarehouseContext.instance();
  }

  public static ClientState instance() {
    if (instance == null) {
      instance = new ClientState();
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
    System.out.println("ClientState Menu");
    System.out.println("==============================");
    System.out.println("Enter a number(0-7) as explained below:");
    System.out.println(EXIT + " to Exit");
    System.out.println(CLIENT_DETAILS + " to view client details");
    System.out.println(PRODUCT_LIST_WITH_SALEPRICE + " to list product list with sale price");
    System.out.println(CLIENT_TRANSACTIONS+ " to display client transactions");
    System.out.println(SHOPPING_CART+ " to display SHOPPING CART (New State)");
      System.out.println(CLIENT_WAITLIST+ " to display client waitlist");
      System.out.println(CLERK_MENU+ " to display Cerk Menu");
    System.out.println(HELP + " for help");
  }



public void clientDetils(){
String cid = (WarehouseContext.instance()).getUser();

    Client client = warehouse.searchClient(cid);

    if(client != null)
    {

        System.out.println("==================================");
        System.out.println("Client Info " + client.toString());
        System.out.println("==================================");
      }

}


 public void waitlistOrder(){
String cid = (WarehouseContext.instance()).getUser();

    Client client = warehouse.searchClient(cid);

    if(client != null)
    {


        Iterator allOrders = warehouse.getWaitListedOrdersForClient(cid);

        if(allOrders != null)
        {
            System.out.println("==================================");
            System.out.println("| Waitlisted Orders for:"+ cid);
            System.out.println("==================================");

            while(allOrders.hasNext())
            {
                Order order = (Order)(allOrders.next());
                if(order.getClient().getId().equals(cid))
                {
                    System.out.println(order.toString());
                }
            }
        }
 }
}

 public void productDetails(){
  String cid = (WarehouseContext.instance()).getUser();
  Client client = warehouse.searchClient(cid);
  
  Iterator allProducts = warehouse.getProducts();

  if(allProducts!=null)
  {

        System.out.println("PRODUCT LIST");
        System.out.println("=========================");
        while (allProducts.hasNext())
        {
            Product product = (Product)(allProducts.next());
             
            System.out.println(product.toString());
            }
        }

      }
 



  public void viewAccount() {

    String cid = (WarehouseContext.instance()).getUser();

    Client client = warehouse.searchClient(cid);

    if(client != null)
    {



        Iterator processedOrders = client.getOrders();

        if(processedOrders != null)
        {
            System.out.println("==================================");
            System.out.println("| Client Transactions:"+ cid);
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
      String id = getToken("Enter Product Id (Eg. P1):");

      if (Warehouse.instance().searchProduct(id) != null)
      {
          System.out.println(Warehouse.instance().searchProduct(id));
      }
      else
          System.out.println("Invalid product id.");
  }


public void shoppingMenu(){
    (WarehouseContext.instance()).changeState(4);
  }


  public void clerkMenu(){
    (WarehouseContext.instance()).changeState(2);
  }

  public void setUID_tester(String uID)
  {
      context.setUser(uID);
  }

  public void logout()
  {

    if (WarehouseContext.instance().getLogin() == WarehouseContext.IsClerk || WarehouseContext.instance().getLogin() == WarehouseContext.IsManager)
    {  //stem.out.println(" going to login \n");
        (WarehouseContext.instance()).changeState(2); // exit with a code 2
    }
    else if (WarehouseContext.instance().getLogin() == WarehouseContext.IsClient)
    {  //stem.out.println(" going to login \n");
        (WarehouseContext.instance()).changeState(0); // exit with a code 0
    }
    else
        (WarehouseContext.instance()).changeState(1); // exit code 1, indicates error
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
        case CLIENT_DETAILS : clientDetils();
                                break;


        case PRODUCT_LIST_WITH_SALEPRICE: checkProductPrice();
                                break;
        case CLIENT_TRANSACTIONS :viewAccount();

                                break;
        case SHOPPING_CART: shoppingMenu();

         case CLIENT_WAITLIST: waitlistOrder();

                                break;
        case CLERK_MENU :    clerkMenu();
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
