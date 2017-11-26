
package guideMe;

import java.security.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.json.*;

public class guideMeServerIMP extends java.rmi.server.UnicastRemoteObject implements guideMeServerInt {
    KeyChain keychain = new KeyChain();
    String currentClient = "client";
    boolean isVerified = false;
    Connection con;
    java.sql.Statement st = null;
    ResultSet rs;
    JSONObject js;
    int ranNum;
    String emailEntered = null;

    /**
     * Gets the result from the calling client and verifies it to authenticate the client
     * @param sO - A SignedObject by the client
     */
    public void clientChallengeResult(SignedObject sO) {
        SignedObject gottenClientObject;
        gottenClientObject = sO;
        System.out.println("Challenge result received from client: " + gottenClientObject);
        System.out.println("Verifying result.....");
        // System.out.println(keychain.getPubKey(currentClient));

        boolean b = false;

        b = verifyObject(sO, keychain.getPubKey("client"));
        this.isVerified = b;

        System.out.print("Verification status: "+b);

    }

    /**
     * For authenticating the client to the server
     * @return a int as a challenge to the calling client
     */
    public int receiveChallengeForClient() {
        //Create a 6 dig number to send...
        Random randGen = new Random();
        int x = randGen.nextInt(999999) + 1;
        return x;
    }

    /**
     * For authenticating the server to the client
     * @param challenge - An int, received from the client
     * @return - a signed object returned back to the client
     */
    public SignedObject challengeForServer (int challenge, String client){
        try{
            currentClient = client;
            //Signing
            //KeyChain k = new KeyChain(); //public key in the keychain used to sign
            int x = challenge; //message to be signed
            return signThisObject(x,keychain.getPriKey("Server")); //create a new signed object and place it in signed
        }
        catch(Exception e){
        }
        return null;
    }

    public guideMeServerIMP() throws java.rmi.RemoteException{
        super();
    }
    /**
     * @param challenge int that will be signed
     * @param sender PrivateKey from the server that will be used to sign the challenge
     * @return A signed object will be returned
     */
    SignedObject signThisObject(int challenge, PrivateKey sender){
        try {
            //Choosing the correct digital signature instance so that we can sign the incoming message
            Signature signature = Signature.getInstance(sender.getAlgorithm(),"SUN");
            //Time to sign the challenge using the private key, and the type of algorithm used to sign it
            SignedObject signedObject = new SignedObject(challenge, sender, signature);
            //.....All done
            System.out.println("The following message was signed: "+signedObject.getObject());
            return signedObject;
        }
        catch(Exception e){
            System.out.print(e);
        }
        return null;
    }

    /**
     * Verifies the object using a public key.
     *
     * @param sx - The object that needs verifying
     * @param senderKey - We get the public key of the sender from a keychain
     * @return Boolean - True if verified, false otherwise
     */
    Boolean verifyObject(SignedObject sx, PublicKey senderKey){
        try {
            //Type of algorithm used e.g. DES
            Signature s = Signature.getInstance(senderKey.getAlgorithm(), "SUN");
            //Now get the public key of the sender for verification
            sx.verify(senderKey,s);
            //All done
            //System.out.println(sO.verify(k.serverPubKey(),s));
            return sx.verify(senderKey,s);
        }
        catch(Exception e){
            System.out.print(e);
        }
        return false;
    }



    public String addANewFacility(String name, String location, String hours, String description){
        mySqlINIT();
        try {
            Statement statement = con.createStatement ();
            statement.executeUpdate("INSERT  INTO facilities (Name, Location, OpeningHours, Information) " +
                    "VALUES ('"+name+"', '"+location+"', '"+hours+"', '"+description+"');");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Record added";
    }

    public String returnATableFac(String tableName) throws Exception{
            mySqlINIT();
            JSONArray thisJSONList = new JSONArray();

            String query = "SELECT * FROM " + tableName + ";";
            rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                thisJSONList.put(name);
            }
            return thisJSONList.toString();
    }

    public String returnATableWhereFac(String tableName, String column, String search) throws Exception{
        mySqlINIT();
        String query = "SELECT * FROM "+tableName+" WHERE "+column+" = '"+search+"';";
        rs = st.executeQuery(query);
        JSONObject thisJSONRec = new JSONObject();

        if(rs.next()) {
            String nameOfFac = rs.getString("Name");
            String locationOfFac = rs.getString("Location");
            String hoursOfFac = rs.getString("OpeningHours");
            String infoOfFac = rs.getString("Information");

            thisJSONRec.put("Name",nameOfFac);
            thisJSONRec.put("Location",locationOfFac );
            thisJSONRec.put("Hours",hoursOfFac );
            thisJSONRec.put("Info",infoOfFac );
        }
        return thisJSONRec.toString();
    }
    public String updateFacility(String nameToUpdate, String location, String hours, String description) throws Exception{

        mySqlINIT();
        Statement statement = con.createStatement ();
        statement.executeUpdate("UPDATE Facilities SET " +
                "OpeningHours = '"+hours+"', " +
                "Location = '"+location+"', " +
                "Information = '"+description+"' WHERE Name = '"+nameToUpdate+"';");
        con.close();
        return "Record added";
    }

    public String addANewShop(String name, String location, String hours, String description){
        mySqlINIT();
        try {
            Statement statement = con.createStatement ();
            statement.executeUpdate("INSERT  INTO shops (Name, Location, OpeningHours, Information) " +
                    "VALUES ('"+name+"', '"+location+"', '"+hours+"', '"+description+"');");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Record added";
    }

    public String returnATableShop(String tableName) throws Exception{
        mySqlINIT();
        JSONArray thisJSONList = new JSONArray();

        String query = "SELECT * FROM "+tableName+";";
        rs = st.executeQuery(query);
        while (rs.next()) {
            String name = rs.getString("name");
            thisJSONList.put(name);
        }
        return thisJSONList.toString();
    }

    public String returnATableWhereShop(String tableName, String column, String search) throws Exception{
        mySqlINIT();
        String query = "SELECT * FROM "+tableName+" WHERE "+column+" = '"+search+"';";
        rs = st.executeQuery(query);
        JSONObject thisJSONRec = new JSONObject();

        if(rs.next()) {
            String nameOfFac = rs.getString("Name");
            String locationOfFac = rs.getString("Location");
            String hoursOfFac = rs.getString("OpeningHours");
            String infoOfFac = rs.getString("Information");

            thisJSONRec.put("Name",nameOfFac);
            thisJSONRec.put("Location",locationOfFac );
            thisJSONRec.put("Hours",hoursOfFac );
            thisJSONRec.put("Info",infoOfFac );
        }
        return thisJSONRec.toString();
    }
    public String updateShop(String nameToUpdate, String location, String hours, String description) throws Exception{

        mySqlINIT();
        Statement statement = con.createStatement ();
        statement.executeUpdate("UPDATE Shops SET " +
                "OpeningHours = '"+hours+"', " +
                "Location = '"+location+"', " +
                "Information = '"+description+"' WHERE Name = '"+nameToUpdate+"';");
        con.close();
        return "Record added";
    }

    public String addANewDepartment(String name, String location, String faculty, String description){
        mySqlINIT();
        try {
            Statement statement = con.createStatement ();
            statement.executeUpdate("INSERT  INTO Departments (Name, Location, Faculty, Information) " +
                    "VALUES ('"+name+"', '"+location+"', '"+faculty+"', '"+description+"');");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Record added";
    }

    public String returnATableDepartment(String tableName) throws Exception{
        mySqlINIT();
        JSONArray thisJSONList = new JSONArray();

        String query = "SELECT * FROM "+tableName+";";
        rs = st.executeQuery(query);
        while (rs.next()) {
            String name = rs.getString("name");
            thisJSONList.put(name);
        }
        return thisJSONList.toString();
    }

    public String returnATableWhereDepartment(String tableName, String column, String search) throws Exception{
        mySqlINIT();
        String query = "SELECT * FROM "+tableName+" WHERE "+column+" = '"+search+"';";
        rs = st.executeQuery(query);
        JSONObject thisJSONRec = new JSONObject();

        if(rs.next()) {
            String nameOfFac = rs.getString("Name");
            String locationOfFac = rs.getString("Location");
            String hoursOfFac = rs.getString("Faculty");
            String infoOfFac = rs.getString("Information");

            thisJSONRec.put("Name",nameOfFac);
            thisJSONRec.put("Location",locationOfFac );
            thisJSONRec.put("Faculty",hoursOfFac );
            thisJSONRec.put("Info",infoOfFac );
        }
        return thisJSONRec.toString();
    }
    public String updateDepartment(String nameToUpdate, String location, String hours, String description) throws Exception{
        mySqlINIT();
        Statement statement = con.createStatement ();
        statement.executeUpdate("UPDATE Departments SET " +
                "Faculty = '"+hours+"', " +
                "Location = '"+location+"', " +
                "Information = '"+description+"' WHERE Name = '"+nameToUpdate+"';");
        con.close();
        return "Record added";
    }

    public boolean logIn(String username, String hashedPass){
        boolean verified = false;
        mySqlINIT();
        try {
            rs = st.executeQuery("SELECT * FROM users;");
            /**
             * Check if Email is correct
             */
            while (rs.next()) {
                String email = rs.getString("Email");
                if (email.equals(username)){
                    /**
                     * Check if password is correct
                     */
                    String pass = rs.getString("Password");
                    if (hashedPass.equals(pass.toString())){
                        verified = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verified;
    }

    public String getAcademics(String userName) {
        JSONObject thisJSONRec = new JSONObject();
        mySqlINIT();
        try {
            ResultSet currentUser;
            Statement statement = con.createStatement();
            currentUser = statement.executeQuery("SELECT * FROM Academics WHERE Email = '" + userName + "';");

            if (currentUser.next()) {
                String nameOfAcademic = currentUser.getString("Name");
                String roleOfAcademic = currentUser.getString("Role");
                String departmentOfAcademic = currentUser.getString("Department");
                String emailOfAcademic = currentUser.getString("Email");
                String officeOfAcademic = currentUser.getString("Office");
                String numberOfAcademic = currentUser.getString("Number");
                String availabilityOfAcademic = currentUser.getString("Availability");

                thisJSONRec.put("Name",nameOfAcademic);
                thisJSONRec.put("Role",roleOfAcademic);
                thisJSONRec.put("Department",departmentOfAcademic);
                thisJSONRec.put("Email",emailOfAcademic);
                thisJSONRec.put("Office",officeOfAcademic);
                thisJSONRec.put("Number",numberOfAcademic);
                thisJSONRec.put("Availability",availabilityOfAcademic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
        return thisJSONRec.toString();
    }

    public boolean addNewUser(String emailEntered){
        boolean success = false;
        mySqlINIT();
        try {
            /**
             * Check if the user is apiCall lecturer by comparing the email entered
             * with the emails on the database
             */
            rs = st.executeQuery("SELECT * FROM Academics");

            while (rs.next()) {
                String name = rs.getString("Email");
                System.out.println(name);
                System.out.println(emailEntered);
                if (name.equals(emailEntered)){
                    System.out.println(emailEntered);
                    System.out.println(name);

                    /**
                     * If the email address is the same, send the lecutrer apiCall six dig. pin through email
                     */
                    ranNum = ThreadLocalRandom.current().nextInt(111111,999999);
                    System.out.println("Pin generated:  "+ranNum);
/**
                    EmailService m = new EmailService(emailEntered,"Thank you for registering with Guide Me.\n" +
                            "Your secret pin is: "+ranNum+".\n" +
                            "Please enter this number in the web form to complete the registration process\n\n" +
                            "Thank you.");
 **/
                    success = true;
                    emailEnteredSetter(emailEntered);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean addNewUserFinal(String pinEntered, String passwordHash){
        mySqlINIT();
        boolean success = false;
        System.out.println("Pin entered:  " + pinEntered);
        String pin = Integer.toString(ranNum);

        if (pin.equals(pinEntered)){
            /**
             * If new...
             */
            try {
                boolean aNewUserIsHere = true;

                ResultSet checkPass;
                checkPass = st.executeQuery("SELECT * FROM users");
                while (checkPass.next()) {
                    String email = checkPass.getString("Email");
                    /**
                     * If an email is already registered, we simply change the password
                     */
                    if (email.equals(emailEntered)){
                        Statement st = con.createStatement();
                        st.executeUpdate("UPDATE users SET Password= '"+passwordHash+"' WHERE Email='"+emailEntered+"';");
                        aNewUserIsHere=false; //No new user is found :(
                        success = true;
                        break;
                    }
                }
                /**
                 * If an email has not been registered before, we add apiCall new entry
                 */
                if (aNewUserIsHere) {
                    Statement st = con.createStatement();
                    st.executeUpdate("INSERT INTO users ( Email, Password) VALUES ( '"+emailEntered+"','"+passwordHash+"');");
                    success = true;
                }
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean updateAcademics(String Email, String nameOfAcademic, String role, String depart,String number, String office, String availablity){
        mySqlINIT();
        boolean success = false;
        try {
            Statement statement = con.createStatement ();
            statement.executeUpdate("UPDATE Academics SET " +
                    "Name = '"+nameOfAcademic+"', " +
                    "Role = '"+role+"', " +
                    "Department = '"+depart+"', " +
                    "Office = '"+office+"', " +
                    "Number = '"+number+"', " +
                    "Availability = '"+availablity+"' " +
                    "WHERE Email = '"+Email+"';");
            con.close();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean addANewAcademics(String Email, String nameOfAcademic, String role, String depart,String number, String office, String availablity){
        mySqlINIT();
        boolean success = false;
        try {
            Statement statement = con.createStatement ();
            statement.executeUpdate("INSERT  INTO Academics (Name, Role, Department, Email, Office, Number, Availability) " +
                    "VALUES ('"+nameOfAcademic+"', '"+role+"', '"+depart+"', '"+Email+"', '"+office+"', '"+number+"', '"+availablity+"');");
            con.close();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public String academicsByDepartment(String department){
        mySqlINIT();
        JSONArray thisJSONList = new JSONArray();
        try {
            String query = "SELECT * FROM Academics WHERE Department = '"+department+"';";
            rs = st.executeQuery(query);
            while (rs.next()){
                String name= rs.getString("Name");
                thisJSONList.put(name);
            }
        }catch(Exception ex) {
            System.out.println("Error while trying to get data from the database");
        }
        return thisJSONList.toString();
    }

    public String locationByTable(String fromTable, String recordName){
        mySqlINIT();
        JSONArray thisJSONList = new JSONArray();
        try {
            String query = "SELECT Location FROM "+fromTable+" WHERE name = '"+recordName+"';";
            rs = st.executeQuery(query);
            while (rs.next()){
                String name= rs.getString("Location");
                thisJSONList.put(name);
            }
        }catch(Exception ex) {
            System.out.println("Error while trying to get data from the database");
        }
        return thisJSONList.toString();
    }


    private void emailEnteredSetter(String email){
        this.emailEntered = email;
    }

    public String getOneAcademicInfo(String name) {
        JSONObject thisJSONRec = new JSONObject();
        mySqlINIT();
        try {
            ResultSet currentUser;
            Statement statement = con.createStatement();
            currentUser = statement.executeQuery("SELECT * FROM Academics WHERE Name = '" + name + "';");

            if (currentUser.next()) {
                String nameOfAcademic = currentUser.getString("Name");
                String roleOfAcademic = currentUser.getString("Role");
                String departmentOfAcademic = currentUser.getString("Department");
                String emailOfAcademic = currentUser.getString("Email");
                String officeOfAcademic = currentUser.getString("Office");
                String numberOfAcademic = currentUser.getString("Number");
                String availabilityOfAcademic = currentUser.getString("Availability");

                thisJSONRec.put("Name",nameOfAcademic);
                thisJSONRec.put("Role",roleOfAcademic);
                thisJSONRec.put("Department",departmentOfAcademic);
                thisJSONRec.put("Email",emailOfAcademic);
                thisJSONRec.put("Office",officeOfAcademic);
                thisJSONRec.put("Number",numberOfAcademic);
                thisJSONRec.put("Availability",availabilityOfAcademic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
        return thisJSONRec.toString();
    }


    /**
     * A function that makes the initial connection to the database
     */
    public void mySqlINIT (){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/guideme", "root", "");
            st = con.createStatement();
        }catch(Exception e) {
            System.out.println("Error while trying to make a connection to the database");
        }
    }
}