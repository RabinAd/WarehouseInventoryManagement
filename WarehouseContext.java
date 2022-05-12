


import java.util.*;
import java.text.*;
import java.io.*;
public class WarehouseContext {
  
  private int currentState;
  private static Warehouse warehouse;
  private static WarehouseContext context;
  private int currentUser;
  private String userID;
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
  public static final int IsClient = 1;
  public static final int IsClerk = 2;
  public static final int IsManager = 3;
  public static final int IsShopping = 4;
  public static final int IsQuery = 5;

  private WarehouseState[] states;
  private int[][] nextState;

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

  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  
  private WarehouseContext() 
  { //constructor
    System.out.println("In WarehouseContext constructor");
    
    if (yesOrNo("Look for saved data and use it?")) 
    {
      retrieve();
    } 
    else
    {
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WarehouseState[6];
    states[0] = Loginstate.instance();
    states[1] = ClientState.instance(); 
    states[2]=  ClerkState.instance();
    states[3]=  Managerstate.instance();
    states[4]=  ShoppingState.instance();
    states[5]=  QueryState.instance();
    nextState = new int[6][6];

nextState[0][0] = -1; 
    nextState[0][1] = 1; 
    nextState[0][2] = 2; 
    nextState[0][3] = 3;
    nextState[0][4] = 4;
    nextState[0][5] = 5;

    nextState[1][0] = 0; 
    nextState[1][1] = -1; 
    nextState[1][2] = 2; 
    nextState[1][3] = -2;
    nextState[1][4] = 4;
    nextState[1][5] = -2;

    nextState[2][0] = 0; 
    nextState[2][1] = 1; 
    nextState[2][2] = -1; 
    nextState[2][3] = -2;
    nextState[2][4] = -2;
    nextState[2][5] = 5;
    
    nextState[3][0] = 0; 
    nextState[3][1] = -2; 
    nextState[3][2] = 2; 
    nextState[3][3] = -1;
    nextState[3][4] = -2;
    nextState[3][5] = -2;
    

    nextState[4][0] = 0; 
    nextState[4][1] = 1; 
    nextState[4][2] = -2; 
    nextState[4][3] = -2;
    nextState[4][4] = -1;
    nextState[4][5] = -2;
    
    nextState[5][0] = 0; 
    nextState[5][1] = -2; 
    nextState[5][2] = 2; 
    nextState[5][3] = -2;
    nextState[5][4] = -2;
    nextState[5][5] = -1;


   
    

    
    
    currentState = 0;
  }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      terminate();
    //System.out.println("current state " + currentState + " \n \n ");
    states[currentState].run();
  }

  public void setLogin(int code)
  {currentUser = code;}
  
  public void setUser(String uID) {
   userID = uID; 
   //System.out.println(userID);
  }

  public int getLogin()
  { return currentUser;}

  public String getUser()
  { return userID;}

  public static WarehouseContext instance() {
    if (context == null) {
       System.out.println("calling constructor");
      context = new WarehouseContext();
    }
    return context;
  }

  public void process(){
    //System.out.println("PROCESSING: Running Current State = " + currentState);
    states[currentState].run();
  }
  
  public static void main (String[] args){
      
    WarehouseContext.instance().process(); 
  }
  
  private void terminate()
  {
   if (yesOrNo("Save data?")) {
      if (Warehouse.save()) {
         System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
       } else {
         System.out.println(" There has been an error in saving \n" );
       }
     }
   System.out.println(" Goodbye \n "); System.exit(0);
  }


}
