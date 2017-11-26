package guideMe;
/**
 * Created by Faze on 07/12/2016.
 *
 * Returns keys of the server as well as all users
 */
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyChain {

    /**
     * Simply returns the public keys of the registered users and server once specified
     * through a String user's name.
     */
    public PublicKey getPubKey(String name){
        if ("Server".equals(name)){
            return serverPubKey();
        }
        if ("client".equals(name)){
            return clientPubKey();
        }
        else return null;
    }

    /**
     * Simply returns the public keys of the registered users and server once specified
     * through a String user's name.
     */
    public PrivateKey getPriKey(String name){
        if ("Server".equals(name)){
            return serverPriKey();
        }

        return null;
    }


    //Statically loads Server's private key
    private PrivateKey serverPriKey(){
        try {
            byte[] priKeyBytes = Files.readAllBytes(new File("keys/Server/server_private_key").toPath().toAbsolutePath());
            PKCS8EncodedKeySpec serverPrispec = new PKCS8EncodedKeySpec(priKeyBytes);
            KeyFactory priFact = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey serverPrivateKey = priFact.generatePrivate(serverPrispec);
            return serverPrivateKey;
        }
        catch(Exception e){
            System.out.print(e);
        }
        return null;
    }

    //Statically gets Server's public key
    private PublicKey serverPubKey(){
        try {
            byte[] pubKeyBytes = Files.readAllBytes(new File("keys/Server/server_public_key").toPath().toAbsolutePath());
            X509EncodedKeySpec serverPublicspec = new X509EncodedKeySpec(pubKeyBytes);
            KeyFactory pubFact = KeyFactory.getInstance("DSA", "SUN");
            PublicKey serverPublicKey = pubFact.generatePublic(serverPublicspec);
            return serverPublicKey;
        }
        catch(Exception e){
            System.out.print(e);
        }
        return null;
    }

    //Statically gets Jack's public key
    private PublicKey clientPubKey(){
        try {
            byte[] pubKeyBytes = Files.readAllBytes(new File("keys/Client/client_public_key").toPath().toAbsolutePath());
            X509EncodedKeySpec serverPublicspec = new X509EncodedKeySpec(pubKeyBytes);
            KeyFactory pubFact = KeyFactory.getInstance("DSA", "SUN");
            PublicKey serverPublicKey = pubFact.generatePublic(serverPublicspec);
            return serverPublicKey;
        }
        catch(Exception e){
            System.out.print(e);

        }
        return null;
    }



}
