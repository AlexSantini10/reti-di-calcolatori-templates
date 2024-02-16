package test;

import java.rmi.Naming;

public class HelloClient {
    public static void main(String[] args) {
        try {
            Hello obj = (Hello) Naming.lookup("//localhost/HelloServer");
            String message = obj.sayHello();
            System.out.println("Message from server: " + message);
        } catch (Exception e) {
            System.err.println("HelloClient exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

