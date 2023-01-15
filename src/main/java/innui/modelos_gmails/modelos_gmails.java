/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package innui.modelos_gmails;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Drafts;
import com.google.api.services.gmail.Gmail.Users.Drafts.Create;
import com.google.api.services.gmail.Gmail.Users.Drafts.Send;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.errores.oks;
import innui.modelos.internacionalizacion.tr;
import innui.modelos.modelos;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author emilio
 */
public class modelos_gmails extends modelos {
    public static String k_in_ruta_modelos_gmails = "in/innui/modelos_emails/in";
    public static String k_ruta_credenciales_json = "/re/google_credentials.json";
    public static String k_directorio_tokens = "/home/nombre_usuario/carpeta_donde_se_crean_google_StoredCredential"; // Si no existen, google las creará;
    public static int k_LocalServerReceiver_port = 8888;
    public static NetHttpTransport k_http_transport = null;
    public Credential _credential = null;
    
    @Override
    public boolean iniciar(oks ok, Object... extra_array) throws Exception {
        super.iniciar(ok, extra_array);
        if (ok.es) {
            String texto;
            texto = properties.getProperty("gmail.api.ruta_credenciales_json");
            if (texto != null) {
                k_ruta_credenciales_json = texto;
            }
            texto = properties.getProperty("gmail.api.directorio_tokens");
            if (texto != null) {
                k_directorio_tokens = texto;
            }
        }
        return ok.es;
    }    
    /**
     * Obtiene la autorización de las credenciales de un archivo json definido en k_ruta_credenciales_json
     * @param netHttpTransport The network HTTP Transport.
     * @param ok Comunicar resultados
     * @param extras_array Opción de añadir parámetros en el futuro.
     * @return Un objeto authorized Credential object.
     * @throws Exception si no encuentra el archivo de credenciales k_ruta_credenciales_json.
     */
    private static Credential obtener_credenciales_autorizadas(final NetHttpTransport netHttpTransport
    , oks ok, Object ... extras_array)  throws Exception {
        ResourceBundle in = null;
        try {
            List<String> gmaiScopes_list = new ArrayList<>();
            in = ResourceBundles.getBundle(k_in_ruta_modelos_gmails);
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//            gmaiScopes_list.add(GmailScopes.GMAIL_COMPOSE);  // No se necesita
//            gmaiScopes_list.add(GmailScopes.GMAIL_INSERT);  // No se necesita
//            gmaiScopes_list.add(GmailScopes.GMAIL_LABELS);  // No se necesita
//            gmaiScopes_list.add(GmailScopes.GMAIL_MODIFY);  // No se necesita
//            gmaiScopes_list.add(GmailScopes.GMAIL_SEND);  // No se necesita
            gmaiScopes_list.add(GmailScopes.MAIL_GOOGLE_COM);
            // Load client secrets.
            InputStream inputStream = modelos_gmails.class.getResourceAsStream(k_ruta_credenciales_json);
            if (inputStream == null) {
                ok.setTxt(tr.in(in, "Archivo de credenciales no encontrado en la ruta: ") + k_ruta_credenciales_json);
                return null;
            }
            GoogleClientSecrets googleClientSecrets
                    = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(inputStream));
            URL directorio_tokens_url = modelos_gmails.class.getResource(k_directorio_tokens);            
            if (directorio_tokens_url == null) {
                ok.setTxt(tr.in(in, "Directorio para los tokens de identificación no encontrado: ") + k_ruta_credenciales_json);
                return null;
            }
            File directorio_tokens = new File(directorio_tokens_url.toURI());// new File(k_directorio_tokens);
            FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(directorio_tokens);
            // Si no encuentra el fileDataStoreFactory en la ruta, creará uno en esa ruta.
            // - Para ello abrirá un navegador web pro defecto y se conectará a Google.
            // - Cuando lo haya creado, lo utilizará sin necesidad de abrir el navegador por defecto.
            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    netHttpTransport, jsonFactory, googleClientSecrets, gmaiScopes_list)
                    .setDataStoreFactory(fileDataStoreFactory /* Files.createTempDirectory(k_directorio_tokens).toFile())*/)
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(k_LocalServerReceiver_port).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            credential.setExpiresInSeconds(null);
            return credential;
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Renueva y obtiene la autorización de las credenciales de un archivo json definido en k_ruta_credenciales_json
     * @param credential Credenciales que renovar.
     * @param netHttpTransport The network HTTP Transport.
     * @param ok Comunicar resultados
     * @param extras_array Opción de añadir parámetros en el futuro.
     * @return Un objeto authorized Credential object.
     * @throws Exception si no encuentra el archivo de credenciales k_ruta_credenciales_json.
     */
    private static Credential renovar_credenciales_autorizadas(Credential credential
    , final NetHttpTransport netHttpTransport
    , oks ok, Object ... extras_array)  throws Exception {
        try {
            ok.es = credential.refreshToken();
            if (ok.es == false) {
                ok.iniciar();
                return modelos_gmails.obtener_credenciales_autorizadas(netHttpTransport, ok);
            } else {
                credential.setExpiresInSeconds(null);
                return credential;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Inicio de la aplicación
     * @param args 
     */
    public static void main(String[] args) {
        oks ok = new oks();
        try {
            modelos_gmails modelo_email = null;
            try {
                modelo_email = new modelos_gmails();
                modelo_email.run(ok);
            } catch (Exception e) {
                ok.setTxt(e);
            }
        } catch (Exception e) {
            ok.setTxt(e);
        }
        if (ok.es == false) {
            System.err.println(ok.txt);
            exit(1);
        } else {
            exit(0);
        }
    }    
    /**
     * Envia un email sin autenticacion
     * @param remitente email de quien envía
     * @param destinatario email del destinatario
     * @param asunto Asunto del email
     * @param cuerpo Cuerpo del email
     * @param files_adjuntos_array Array con los archivos que adjuntar; null si no hay adjuntos.
     * @param ok Comunicar resultados
     * @param extras_array Opción de añadir parámetros en el futuro.
     * @return El mensaje, o null.
     * @throws Exception Opción de notificar errores de excepción
     */
    public Message enviar_email(String remitente
    , String destinatario
    , String asunto
    , String cuerpo
    , File [] files_adjuntos_array
    , oks ok, Object ... extras_array)  throws Exception {
        try {
            if (ok.es == false) { return null; }
            // Obtener un transporte de confianza
            if (k_http_transport == null) {
                k_http_transport  = GoogleNetHttpTransport.newTrustedTransport();
            }
            // Gestionar el permiso de gmail (credenciales autorizadas)
            if (_credential == null) {
                _credential = modelos_gmails.obtener_credenciales_autorizadas(k_http_transport, ok);
                if (ok.es == false) { return null; }
            } else {
                _credential = modelos_gmails.renovar_credenciales_autorizadas(_credential, k_http_transport, ok);
            }
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            Gmail gmail = new Gmail.Builder(k_http_transport, jsonFactory, _credential)
                .setApplicationName(this.getClass().getSimpleName())
                .build();
            // Configurar el envio
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);            
            String smtp_username = properties.getProperty("mail.smtp.username");
            // Crear los datos MIME
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(remitente));
            mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(destinatario));
            mimeMessage.setSubject(asunto);
            if (files_adjuntos_array != null) {
                Multipart multipart = new MimeMultipart();
                MimeBodyPart mimeBodyPart;
                // Primera parte
                mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(cuerpo);
                multipart.addBodyPart(mimeBodyPart);
                for (File file: files_adjuntos_array) {
                    // Siguientes partes
                    mimeBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    mimeBodyPart.setDataHandler(new DataHandler(source));
                    mimeBodyPart.setFileName(file.getName());
                    multipart.addBodyPart(mimeBodyPart);
                }
                mimeMessage.setContent(multipart);
            } else {
                mimeMessage.setText(cuerpo);
            }
            // Codificar el email
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mimeMessage.writeTo(byteArrayOutputStream);
            byte[] bytes_array = byteArrayOutputStream.toByteArray();
            String encoded_email = Base64.encodeBase64URLSafeString(bytes_array);
            // Crear el mensaje
            Message message = new Message();
            message.setRaw(encoded_email);
            // Crear un borrador listo para su envío
            Draft draft = new Draft();
            draft.setMessage(message);
            Drafts drafts = gmail.users().drafts();
            Create create = drafts.create(smtp_username, draft);
            draft = create.execute();
            Send send = drafts.send(smtp_username, draft);
            message = send.execute();
//            Messages messages = gmail.users().messages();
//            Send send = messages.send(smtp_username, message);
//            send.execute();
            return message;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
