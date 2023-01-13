/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package innui.modelos_emails;


import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import innui.modelos.errores.oks;
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
        oks ok = new oks();
        modelos_gmails instance = new modelos_gmails();
        instance.iniciar(ok);
        assertTrue(ok.es);
        Draft expResult = null;
        Message result = instance.enviar_email(remitente, destinatario, asunto, cuerpo, ok);
        assertNotNull(result);
        assertTrue(ok.es);
    }
    
}
