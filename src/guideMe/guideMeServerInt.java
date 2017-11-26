package guideMe;
import org.json.JSONObject;

import javax.crypto.SealedObject;
import java.security.Signature;
import java.security.SignedObject;
import java.sql.ResultSet;
import java.util.HashMap;

public interface guideMeServerInt extends java.rmi.Remote{

    /**
     *
     * @param chal - Sends the server a numerical challenge to authenticate the server
     * @return returns a signed object which can then be verfied
     * @throws java.rmi.RemoteException
     */
    public SignedObject challengeForServer (int chal, String client) throws java.rmi.RemoteException;

    /**
     *
     * @return Client Gets a numerical challenge from the server so it can authenticate itself
     * @throws java.rmi.RemoteException
     */
    public int receiveChallengeForClient() throws java.rmi.RemoteException;

    /**
     *
     * @return Sends the server the signed challenge so the server can authenticate the client
     * @throws java.rmi.RemoteException
     */
    public void clientChallengeResult(SignedObject sO) throws java.rmi.RemoteException;

    public String addANewFacility(String name, String location, String hours, String description)throws Exception;

    public String updateFacility(String nameToUpdate, String location, String hours, String description) throws Exception;

    public String returnATableFac(String tableName) throws Exception;

    public String returnATableWhereFac(String tableName, String column, String search) throws Exception;

    public String updateShop(String nameToUpdate, String location, String hours, String description) throws Exception;

    public String returnATableWhereShop(String tableName, String column, String search) throws Exception;

    public String returnATableShop(String tableName) throws Exception;

    public String addANewShop(String name, String location, String hours, String description) throws Exception;

    public String returnATableDepartment(String tableName) throws Exception;

    public String returnATableWhereDepartment(String tableName, String column, String search) throws Exception;

    public String updateDepartment(String nameToUpdate, String location, String hours, String description) throws Exception;

    public String addANewDepartment(String name, String location, String faculty, String description) throws Exception;

    public boolean logIn(String username, String hashedPass) throws Exception;

    public String getAcademics(String userName) throws Exception;

    public boolean addNewUser(String emailEntered) throws Exception;

    public boolean addNewUserFinal(String pinEntered, String passwordHash) throws Exception;

    public boolean updateAcademics(String Email, String nameOfAcademic, String role, String depart,String number, String office, String availablity) throws Exception;

    public boolean addANewAcademics(String Email, String nameOfAcademic, String role, String depart,String number, String office, String availablity) throws Exception;

    public String academicsByDepartment(String department) throws Exception;

    public String locationByTable(String fromTable, String recordName) throws Exception;

    public String getOneAcademicInfo(String name) throws Exception;
}
