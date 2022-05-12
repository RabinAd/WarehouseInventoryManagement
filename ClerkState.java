import java.util.*;
import java.text.*;
import java.io.*;

public class ClerkState extends WarehouseState
{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private boolean running;
    private int exitCode;
    private static ClerkState instance;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int LIST_PRODUCTS_WITH_QUANTITY_AND_PRICE = 2;
    private static final int QUERY_SYSTEM_CLIENT__STATE = 3;
    private static final int LIST_WAITLISTED_ORDERS_FOR_PRODUCT = 4;
    private static final int RECEIVE_SHIPMENT = 5;
    private static final int CLIENT_MENU = 6;
    private static final int HELP = 7;

    /**
     * Constructor for objects of class ClerkState
     */
    private ClerkState()
    {
        super();
        warehouse = Warehouse.instance();
    }

    public static ClerkState instance(){
        if(instance == null){
            instance = new ClerkState();
        }
        return instance;
    }

    public String getToken(String prompt) {
        do{
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
                System.out.println("Please input a number ");
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

    public void help(){
        System.out.println();
        System.out.println("ClerkState Menu");
        System.out.println("==============================");
        System.out.println("Enter a number between 0 and 7 as explained below");
        System.out.println(EXIT + " to Exit");
        System.out.println(ADD_CLIENT + " to add client");
        System.out.println(LIST_PRODUCTS_WITH_QUANTITY_AND_PRICE + " to list products with their quantity");
       
        System.out.println(QUERY_SYSTEM_CLIENT__STATE + " to go to QUERY STATE (New) about clients");
        System.out.println(LIST_WAITLISTED_ORDERS_FOR_PRODUCT + " to list waitlisted order for a product");
         System.out.println(RECEIVE_SHIPMENT + " to receive shipment");
        System.out.println(CLIENT_MENU + " to switch to CLIENT MENU");
        System.out.println(HELP + " for help");
    }

    //Adding a Client
    public void addClient()
    {
        String name = getToken("Enter client name");
        String address = getToken("Enter address");
        String phone = getToken("Enter phone");
        Client result;
        result = warehouse.addClient(name, address, phone);

        if(result == null)
        {
            System.out.println("Could not add client");
        }

        System.out.println(result);
    }

    //Adding a Product
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

    //Receive a shipment
    public void receiveShipment()
    {
        String id = getToken("Enter Shipment Id: (Example: S1)");
        Iterator result = warehouse.getShipment(id);
        if(result != null){
            //List products in shipment
            System.out.println("Shipment ID: " + id + " | Receiving Follwoing Products");
            while(result.hasNext()){
                ManufacturerOrder m_order = (ManufacturerOrder)(result.next());
                System.out.println("   " + m_order.toString());
            }

            //Process the received products
            warehouse.receiveShipment(id);
        }
        else
        {
            System.out.println("NO DATA FOUND FOR: " + id);
        }
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

    //Prints a list of waitlisted orders for a product
    public void listWaitlistedOrdersForProduct()
    {
        String id = getToken("Enter Product Id(e.g P3, P2, P1):");
        Iterator allOrders = warehouse.getWaitListedOrdersForProduct(id);
        if(allOrders == null){
            return;
        }

        System.out.println("Waitlisted Orders for a product: " + id);
        System.out.println("=========================");
        while(allOrders.hasNext()){
            Order order = (Order)(allOrders.next());
            if(order.getProduct().getId().equals(id)){
                System.out.println(order.toString());
            }
        }
    }


public void queryMenu()
{
    (WarehouseContext.instance()).changeState(5);
}
    //Switching to Client Menu
    public void clientMenu(){
        String clientId = getToken("Please input the client id: ");

        if(Warehouse.instance().searchClient(clientId) != null){
            (WarehouseContext.instance()).setUser(clientId);
            (WarehouseContext.instance()).changeState(1);

        }else{
            System.out.println("Invalid user id");
        }
    }

    //Logging out
    public void logout(){
        //Exit with a code 0
        if((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClerk)
        {
            //Logout to LoginState state
            (WarehouseContext.instance()).changeState(0);
        }else if((WarehouseContext.instance()).getLogin() == WarehouseContext.IsManager)
        {
            //Logout to ManagerState
            (WarehouseContext.instance()).changeState(3);
        }
        else
        {
            //Error message
            (WarehouseContext.instance()).changeState(0);
        }
    }


    public void process(){
        int command;
        help();
        while((command = getCommand()) != EXIT){
            switch(command){
                case ADD_CLIENT:                        addClient();
                                                        break;
               case LIST_PRODUCTS_WITH_QUANTITY_AND_PRICE:     listProducts();
                                                      break;
                case QUERY_SYSTEM_CLIENT__STATE:   queryMenu();
                               
                                                        break;

                case RECEIVE_SHIPMENT:                  receiveShipment();
                                                        break;
              case LIST_WAITLISTED_ORDERS_FOR_PRODUCT:    listWaitlistedOrdersForProduct();
                                                        break;

                case CLIENT_MENU:                       clientMenu();
                                                        break;
                case HELP:                              help();
                                                        break;
            }
        }
        logout();
    }

    public void run(){
        process();
    }
}
