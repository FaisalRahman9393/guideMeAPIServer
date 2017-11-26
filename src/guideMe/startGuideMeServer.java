package guideMe;

import java.rmi.registry.Registry;
import java.rmi.Naming;


public class startGuideMeServer {
    public startGuideMeServer(){
        System.out.println(">>>>>>>>Starting guide Me server<<<<<<<<<\n\n");
        try{
            guideMeServerInt iServ = new guideMeServerIMP();
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099); //Creating a registry for the server
            Naming.rebind("rmi://localhost/guideMeServerAPI", iServ);
            System.out.println("Server now running...");
        } catch (Exception e){
            System.out.println(e);
            System.exit(1);
        }

    }

}







