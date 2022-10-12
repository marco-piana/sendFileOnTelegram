import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Commands {

    private final String[] args;
    private final String username;
    private final String password;
    private final String chatIDOrarioFunzionari = "-1001784403260";
    private final String chatIDTurnarioSO115 = "-1001830670031";
    private File fileToSend;

    public Commands(String username, String password, String[] args) throws ExceptionCommands {
        this.username = username;
        this.password = password;
        this.args = args;
        this.verifyParams();
        this.executeCommand();
    }

    /**
     * Verifica che i parametri provenienti dal prompt siano validi
     * @throws ExceptionCommands Eccezione per i comandi e parametri non validi
     */
    private void verifyParams() throws ExceptionCommands {
        // Il parametro deve essere il percorso assoluto di un file PDF esistente
        String param = this.args[1];

        //**** Il file deve esistere ed essere di tipo PDF
        Path path = new File(param).toPath();
        try {
            // Verifico che l'esistenza del file
            File filePDF = new File(param);
            if (!filePDF.isFile()) {
                String errorMsg = String.format("Errore: il file %s che si sta cercando di inviare non esiste", param);
                throw new ExceptionCommands(errorMsg);
            }

            this.fileToSend = filePDF;

            // Verifico il MimeType del file
            String mimeType = Files.probeContentType(path);
            System.out.println(mimeType);
            if(!mimeType.equals("application/pdf")) {
                String errorMsg = String.format("Errore: il file %s che si sta cercando di inviare non viene riconosciuto come PDF", param);
                throw new ExceptionCommands(errorMsg);
            }
        } catch (IOException e) {
            String errorMsg = String.format("Errore: nella ricerca del MimeType del file %s", param);
            throw new ExceptionCommands(errorMsg);
        }
    }

    /**
     * Prima di eseguire il comando verifica se sia uno dei comandi validi
     * @throws ExceptionCommands Eccezione per i comandi e parametri non validi
     */
    private void executeCommand() throws ExceptionCommands {
        // Comandi validi
        if(args[0].equals("funzionari")) {
            // Generazione e invio nel canale funzionari degli orari
//            System.out.println("Generazione e invio nel canale funzionari degli orari");
            this.sendFunzionari();
        }
        else if (args[0].equals("turniso")) {
            // Generazione e invio nel canale TurniSO115 dei turni
            System.out.println("Generazione e invio nel canale TurniSO115 dei turni");
            this.sendTurniSO();
        }
        else {
            String msg = String.format("Il comando \"%s\" non è valido.", args[0]);
            throw new ExceptionCommands(msg);
        }
    }

    private void sendFileOnTelegram(String chatID) throws ExceptionCommands {
        try {
            Telegram t;
            t = new Telegram(username, password);
            System.out.println("Invia il file PDF " + args[1]);
            t.sendPDF(chatID, this.fileToSend);
        } catch (IOException e) {
            throw new ExceptionCommands(e.getMessage());
        }
    }

    private void sendTurniSO() throws ExceptionCommands {
        this.sendFileOnTelegram(this.chatIDTurnarioSO115);
    }

    private void sendFunzionari() throws ExceptionCommands {
        this.sendFileOnTelegram(this.chatIDTurnarioSO115);
    }
}
