

import java.util.*;
import java.text.*;
import java.io.*;

public class QueryState extends WarehouseState
{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private boolean running;
    private int exitCode;
    private static QueryState instance;
    private static final int EXIT = 0;
    private static final int CLIENT_LIST = 1;
    private static final int OUTSTANDING_BALANCES = 2;
    private static final int CLIENT_WITH_NO_TRANSACTION= 4;
    private static final int CLERK_MENU = 5;
    private static final int HELP = 7;

    /**
     * Constructor for objects of class QueryState
     */
    private QueryState()
    {
        super();
        warehouse = Warehouse.instance();
    }

    public static QueryState instance(){
        if(instance == null){
            instance = new QueryState();
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
        System.out.println("QueryState Menu");
        System.out.println("==============================");
        System.out.println("Enter a number between 0 and 12 as explained below");
        System.out.println(EXIT + " to Exit");
        System.out.println(CLIENT_LIST + " to display client list");
        System.out.println(OUTSTANDING_BALANCES + " to display client with outstanding balances");
        System.out.println(CLIENT_WITH_NO_TRANSACTION + " to display client with no transactions");
        System.out.println(CLERK_MENU + " to switch to clerk menu");
        System.out.println(HELP + " for help");
    }

   
     //Prints a list of clients with an outstanding balance
    public void listClientsWithOutstandingBalance()
    {
        Iterator allClients = warehouse.getClients();
        System.out.println("CLIENTS WITH OUTSTANDING BALANCE");
        System.out.println("=========================");
        while (allClients.hasNext())
        {
            Client client = (Client)(allClients.next());
            if(client.getBalance() > 0.0){
                System.out.println(client.toString());
            }
        }
    }

    //Prints a list of Clients
    public void listClients()
    {
        Iterator allClients = warehouse.getClients();
        System.out.println("CLIENT LIST");
        System.out.println("=========================");
        while (allClients.hasNext())
        {
            Client client = (Client)(allClients.next());
            System.out.println(client.toString());
        }
    }

     public void listClientNoTransactions()
     {
        Iterator allClients = warehouse.getClients();
        System.out.println("CLIENTS ");
        System.out.println("=========================");
        while (allClients.hasNext())
        {
            Client client = (Client)(allClients.next());
            {
            if(client.getBalance() == 0.0){

                System.out.println(client.toString());
}
            else 
           {       System.out.println("Clients with no transaction is empty. ");
               
          }
            }
        }
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
     public void clerkMenu(){

       ( WarehouseContext.instance()).changeState(2);
     }



    //Logging out
    public void logout(){
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
                case  CLIENT_LIST   :             listClients();      
                                                        break;
               case OUTSTANDING_BALANCES:           listClientsWithOutstandingBalance();
                                                      break;
                case CLIENT_WITH_NO_TRANSACTION:   listClientNoTransactions();
                               
                                                        break;
                case CLERK_MENU:                       clerkMenu();
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
