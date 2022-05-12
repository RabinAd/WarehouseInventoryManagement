


import java.util.*;
import java.text.*;
import java.io.*;
public class Loginstate extends WarehouseState{
  private static final int EXIT = 0;
  private static final int USER_LOGIN = 1;
  private static final int CLERK_LOGIN = 2;
  private static final int MANAGER_LOGIN = 3;
   private static final int SHOPPING_LOGIN = 4;
    private static final int QUERY_LOGIN = 5;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private WarehouseContext context;
  private static Loginstate instance;
  private Security secure;

  private int exitCode;
  private boolean running;

  private Loginstate() {
      super();
      secure = new Security();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new Loginstate();
    }
    return instance;
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" ));
        if (value <= QUERY_LOGIN && value >= EXIT) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
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


  private void user(){
    String userID = getToken("Please input the client id (e.g, C1): ");
    if (secure.validateClerk(userID)){
      (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
      (WarehouseContext.instance()).setUser(userID);
      //Change to ClientState
      (WarehouseContext.instance()).changeState(1);
      //running = false;
      //exitCode = 1;
    }
    else
    {
      System.out.println("Invalid user id.");
    }
  }

  private void clerk(){
    (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
    (WarehouseContext.instance()).changeState(2);

    //running = false;
    //exitCode = 2;
  }

private void shopping(){
    (WarehouseContext.instance()).setLogin(WarehouseContext.IsShopping);
    (WarehouseContext.instance()).changeState(4);

    //running = false;
    //exitCode = 2;
  }

  private void query(){
    (WarehouseContext.instance()).setLogin(WarehouseContext.IsQuery);
    (WarehouseContext.instance()).changeState(5);

    //running = false;
    //exitCode = 2;
  }




  private void manager(){
        (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
        (WarehouseContext.instance()).changeState(3);
        running = false;
        exitCode = 3;
  }


  public void process() {
    int command;
    System.out.println();
    System.out.println("LoginState Menu");
    System.out.println("==============================");
    System.out.println("input 0 to exit\n"+
                         "input 1 to login as CLIENT MENU\n" +
                         "input 2 to login as CLERK MENU\n"+
                         "input 3 to login as MANAGER MENU\n"+
                         "input 4 to login as Shopping Cart\n"+
                         "input 5 to login as Query System\n");
    
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case CLERK_LOGIN:       clerk();
                                break;
        case USER_LOGIN:        user();
                                break;
        case MANAGER_LOGIN:     manager();
                            break;

        case SHOPPING_LOGIN:     shopping();
                            break;
        case QUERY_LOGIN:     query();
                            break;


        default:                System.out.println("Invalid choice");

      }
      System.out.println("input 0 to exit\n"+
                         "input 1 to login as client\n" +
                         "input 2 to login as clerk\n"+
                         "input 3 to login as manager\n"+
                         "input 4 to login as Shopping Cart\n"+
                         "input 5 to login as Query System\n");
    }
    (WarehouseContext.instance()).changeState(0);
  }

  public void run() {
      //running = true;
      process();
  }
}
