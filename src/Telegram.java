import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URISyntaxException;

public class Telegram {

    private Authenticator authenticator = null;
    private Proxy proxy = null;
    private HttpURLConnection con = null;
    private String boundary = "---------- TelegramJava";
    private String crlf = "\r\n";
    private String twoHyphens = "--";

    private String authUser = "";
    private String authPassword = "";
    private String proxyHost = "192.168.2.14";
    private int proxyPort = 3128;

    public String urlAPI = "api.telegram.org";

    private String chatID = "-1001784403260";

    public Telegram (String username, String password) {
        this.authUser = username + "@dipvvf.it";
        this.authPassword = password;
    }

    public void getMe() throws IOException {
        this.get("https", 443, urlAPI, "/bot5114900339:AAEiEpTI3c2Nt52eaf35FOS-7EXtxwMriGY/getMe");
    }

    public void getUpdates() throws IOException {
        this.get("https", 443, urlAPI, "/bot5114900339:AAEiEpTI3c2Nt52eaf35FOS-7EXtxwMriGY/getUpdates");
    }

    public void sendPDF(String strFileAbsPath) throws IOException {

        // Autenticazione del Client sul Proxy
        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(authUser, authPassword.toCharArray()));

        // Creazione e configurazione del proxy per la request
        final HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        final RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        String uri = "https://" + urlAPI + "/bot5114900339:AAEiEpTI3c2Nt52eaf35FOS-7EXtxwMriGY/sendDocument?chat_id=" + chatID;

        try (final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {

            final HttpPost httppost = new HttpPost(uri);
            httppost.setConfig(config);
            httppost.setHeader("User-Agent", "Comando VVF Savona");
            //httppost.setHeader("Content-Type", "multipart/form-data;boundary=" + this.boundary);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody(
                    "document", new File(strFileAbsPath), ContentType.APPLICATION_PDF, "orario_funzionari.pdf");

            HttpEntity multipart = builder.build();
            httppost.setEntity(multipart);

            try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Terminato invio dell'orario.");
    }

    public void get(String scheme, int port, String domain, String uri) throws IOException {

        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(authUser, authPassword.toCharArray()));

        try (final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
            final HttpHost target = new HttpHost(scheme, domain, port);
            final HttpHost proxy = new HttpHost(proxyHost, proxyPort);

            final RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();

            final HttpGet httpget = new HttpGet(uri);
            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri() +
                    " via " + proxy);

            try (final CloseableHttpResponse response = httpclient.execute(target, httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                System.out.println(e.getMessage());
                System.out.println("Chiudere la console ed effettuare nuovamente l'operazione di invio.");
            } catch (HttpHostConnectException e) {
                System.out.println(e.getMessage());
                System.out.println("Non è possibile raggiungere il server, verificare la connessione ed effettuare nuovamente l'operazione di invio.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
