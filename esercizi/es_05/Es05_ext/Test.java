package server;

public class Test {
    public static void main(String[] args){
        EndpointMio endpoint = new EndpointMio("127.0.0.1", 1234);

        endpoint.setFilesFromDirName("panna");

        System.out.println(endpoint);
    }
}
