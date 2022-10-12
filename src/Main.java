import java.io.Console;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class Main {

    private static final String header = "*****************************************************************************\n" +
            "*       _  _ _ ____ _ _    _  ___  ____ _     ____ _  _ ____ ____ ____      *\n" +
            "*       |  | | | __ | |    |  |  \\ |___ |     |___ |  | |  | |    |  |      *\n" +
            "*        \\/  | |__] | |___ |  |__/ |___ |___  |    |__| |__| |___ |__|      *\n" +
            "* ____ ____ _  _ ____ _  _ ___  ____  ___  _  ____ ____ _  _ ____ _  _ ____ *\n" +
            "* |    |  | |\\/| |__| |\\ | |  \\ |  |  |  \\ |  [__  |__| |  | |  | |\\ | |__| *\n" +
            "* |___ |__| |  | |  | | \\| |__/ |__|  |__/ |  ___] |  |  \\/  |__| | \\| |  | *\n" +
            "*****************************************************************************\n" +
            "* Pubblicazione Orari                                   (Ver. 1.1 20221012) *\n" +
            "*****************************************************************************\n";

    public static final String msgClose = "Use: java -jar sendFileOnTelegram.jar <comando> <parametro1 parametro2...>";

    private static HttpURLConnection con;

    public static final void clearConsole() {
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
        System.out.println(Main.header);


        // Se viene eseguito senza parametri interrompo l'esecuzione
        if (args == null || args.length != 2) {
            // Fine
            System.out.println(Main.msgClose);
            System.exit(0);
        }

        //***** Recupero username e password per l'accesso al proxy
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

        // Verifica della password inserita
        if(password.length() == 0) {
            System.out.println("Il file \"" + args[0] + "\" non è stato inviato.");
            System.exit(0);
        }

        //**** Tentativo di esecuzione del comando di args
        try {
            Commands c = new Commands(username, password, args);
        }
        catch (ExceptionCommands e) {
            System.out.println(String.format("%s \n %s", e.getMessage(), Main.msgClose));
            System.exit(0);
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

        // Verifica dei parametri da prompt
        if (args == null || args.length == 0) {
            // Fine
            System.out.println("Use sendFileOnTelegram <comando> <parametro1 parametro2...>");
            System.exit(0);
        }
        else {
            // In base al numero dei parametri passati in args
            switch (args.length) {
                case 0:
                    System.out.println("Utente autenticato al proxy: invio parametri eseguo una getMe sul bot VVF-SV");
                    Telegram c;
                    c = new Telegram(username, password);
                    c.getMe();
                    break;
                case 1:

                    break;
                case 2:
                    break;
                default:
                    System.out.println("Use sendFileOnTelegram <comando> <parametro1 parametro2...>");
                    System.exit(0);
            }
        }


    }
}
