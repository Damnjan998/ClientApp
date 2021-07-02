import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client {

    public Client(String address, int port, String dir) {
        try (Socket socket = new Socket(address, port);
             Stream<Path> filesStream = Files.list(Paths.get(dir));
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            filesStream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toFile)
                    .filter(file -> file.getName().equals("ExternalInput.txt"))
                    .forEach(file -> {
                        try {
                            out.writeUTF(file.getName());
                            byte[] fileBytes = Files.readAllBytes(file.toPath());
                            out.write(fileBytes);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    });

            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String dir = "C:\\Users\\Ogi\\Desktop\\Practical Task\\Input";
        Client client = new Client("127.0.0.1", 8080, dir);
    }
}
