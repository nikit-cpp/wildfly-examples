package p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * Created by Nikita on 11.11.2015.
 */
@Path("/")
@Stateless
public class Starter {
    @EJB(lookup = "java:module/Executor2")
    private Executor asyncExecutor;

    @EJB(lookup = "java:jboss/ee/concurrency/executor/nikita_2_executor")
    ManagedExecutorService syncExecutor;

    Logger logger = LoggerFactory.getLogger(Starter.class);

    @Path("async/{num}")
    @Produces("text/plain")
    @GET
    public String async(@PathParam("num") int num) {

        for(int i =0; i<num; ++i) {
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

    @Path("sync/{num}")
    @Produces("text/plain")
    @GET
    public String sync(@PathParam("num") int num) throws InterruptedException, ExecutionException {
        List<Callable<Void>> callables = new ArrayList<>();

        for(int i =0; i<num; ++i) {
            final int j = i;
            callables.add(() -> {
                logger.info("sync iteration {}", j);
                logger.warn("Ololo {}", Thread.currentThread());
                return null;
            });
        }

        List<Future<Void>> taskResults = syncExecutor.invokeAll(callables);

        for(int i =0; i<taskResults.size(); ++i) {
            Future<Void> future = taskResults.get(i);
            future.get();
        }

        return "Ok";
    }

}
