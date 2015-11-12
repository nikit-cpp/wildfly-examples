package p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.concurrent.Executor;

/**
 * Created by Nikita on 11.11.2015.
 */
@Path("/")
@Stateless
public class Starter {

    @EJB(lookup = "java:jboss/ee/concurrency/executor/nikita_2_executor")
    private Executor syncExecutor;

    @EJB(lookup = "java:module/Executor2")
    private Executor asyncExecutor;

    Logger logger = LoggerFactory.getLogger(Starter.class);

    @Path("async")
    @Produces("text/plain")
    @GET
    public String async() throws NamingException {
        for(int i =0; i<30; ++i) {
            final int j = i;
            asyncExecutor.execute(
                    () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.info("async iteration {}", j);
                        logger.warn("Ololo {}", Thread.currentThread());
                    }
            );
        }

        return "Ok";
    }

    @Path("sync")
    @Produces("text/plain")
    @GET
    public String sync() throws NamingException {
        for(int i =0; i<30; ++i) {
            final int j = i;
            syncExecutor.execute(
                    () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.info("sync iteration {}", j);
                        logger.warn("Ololo {}", Thread.currentThread());
                    }
            );
        }

        return "Ok";
    }

}
