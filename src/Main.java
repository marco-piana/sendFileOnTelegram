import java.io.Console;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class Main {

    private static HttpURLConnection con;

    public final static void clearConsole() {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {


        clearConsole();

        String header = "*****************************************************************************\n" +
        "*       _  _ _ ____ _ _    _  ___  ____ _     ____ _  _ ____ ____ ____      *\n" +
        "*       |  | | | __ | |    |  |  \\ |___ |     |___ |  | |  | |    |  |      *\n" +
        "*        \\/  | |__] | |___ |  |__/ |___ |___  |    |__| |__| |___ |__|      *\n" +
        "* ____ ____ _  _ ____ _  _ ___  ____  ___  _  ____ ____ _  _ ____ _  _ ____ *\n" +
        "* |    |  | |\\/| |__| |\\ | |  \\ |  |  |  \\ |  [__  |__| |  | |  | |\\ | |__| *\n" +
        "* |___ |__| |  | |  | | \\| |__/ |__|  |__/ |  ___] |  |  \\/  |__| | \\| |  | *\n" +
        "*****************************************************************************\n" +
        "* Invio file PDF al canale Telegram VVF-SV Orario Funzionari (Versione 0.1) *\n" +
        "*****************************************************************************\n";
        System.out.println(header);


        // Recupero la username in base al login di sistema
        String username = System.getProperty("user.name");
        String password;
        Console console = System.console();
        if (console == null) {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.print("Inserisci la password dell'utente " + username + "@dipvvf.it di accesso al proxy: ");
            password = myObj.nextLine();
        } else {
            char[] passwordArray = console.readPassword("Inserisci la password dell'utente " + username + "@dipvvf.it di accesso al proxy: ");
            password = String.valueOf(passwordArray);
        }



        if (args != null && args.length == 1) {
            if(password.length() == 0) {
                System.out.println("Il file \"" + args[0] + "\" non è stato inviato.");
                System.exit(0);
            }
//            System.out.println("Il primo parametro deve essere il nome del file PDF da inviare");
            Telegram c;
            c = new Telegram(username, password);
            System.out.println("Invia il file PDF " + args[0]);
            c.sendPDF(args[0]);
//            c.getMe();
        } else {
            System.out.println("Se non invio parametri eseguo una getMe sul bot VVF-SV");
            Telegram c;
            c = new Telegram(username, password);
//            c.sendPDF(args[0]);
            c.getMe();
//            c.getUpdates();
        }
    }
}
