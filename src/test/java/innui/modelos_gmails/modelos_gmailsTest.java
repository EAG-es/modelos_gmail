/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package innui.modelos_gmails;


import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.google.common.io.Resources;
import innui.modelos.errores.oks;
import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author emilio
 */
public class modelos_gmailsTest {
    
    public modelos_gmailsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of main method, of class modelos_gmails.
     */
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        modelos_gmails.main(args);
    }

    /**
     * Test of enviar_email method, of class modelos_gmails.
     */
    @Test
    public void testEnviar_email() throws Exception {
        System.out.println("enviar_email");
        String remitente = "no.responder@gmail.com";
        String destinatario = "eag2001@gmail.com";
        String asunto = "Probar email";
        String cuerpo = "Cuerpo de probar email";
        File [] files_adjuntos_array = null;
        oks ok = new oks();
        modelos_gmails instance = new modelos_gmails();
        instance.iniciar(ok);
        assertTrue(ok.es);
        Draft expResult = null;
        Message result = null;
        URL url = null;
//        result = instance.enviar_email(remitente, destinatario, asunto, cuerpo, null, ok);
//        assertNotNull(result);
//        assertTrue(ok.es);
//        result = instance.enviar_email(remitente, destinatario, asunto, cuerpo, null, ok);
//        assertNotNull(result);
//        assertTrue(ok.es);
//        files_adjuntos_array = new File[1];
//        url = this.getClass().getResource("/re/configuraciones.properties");
//        files_adjuntos_array[0] = new File(url.toURI());
//        result = instance.enviar_email(remitente, destinatario, asunto, cuerpo, files_adjuntos_array, ok);
//        assertNotNull(result);
//        assertTrue(ok.es);
        files_adjuntos_array = new File[2];
        url = Resources.getResource(this.getClass(), "/re/innui.modelos_gmails.properties");
        files_adjuntos_array[0] = new File(url.toURI());
        url = Resources.getResource(this.getClass(), "/re/google_credentials.json");
        files_adjuntos_array[1] = new File(url.toURI());
        result = instance.enviar_email(remitente, destinatario, asunto, cuerpo, files_adjuntos_array, ok);
        assertNotNull(result);
        assertTrue(ok.es);
    }
    
}
