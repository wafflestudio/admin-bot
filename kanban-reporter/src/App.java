import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        
        System.out.println("Hello, World!");
    }
}
