package p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.Executor;

/**
 * Created by Nikita on 11.11.2015.
 */
@Path("/")
@Stateless
public class Starter {

    @EJB
    private Executor executor;

    Logger logger = LoggerFactory.getLogger(Starter.class);

    @GET
    public String init() throws NamingException {
        InitialContext initialContext = new InitialContext();
        Executor executor = (Executor) initialContext.lookup("java:module/Executor2");

        for(int i =0; i<1000; ++i) {
            executor.execute(() -> logger.warn("Ololo {}", Thread.currentThread()));
        }

        return "Ok";
    }
}
