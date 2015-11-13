package p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
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

    @EJB(lookup = "java:jboss/ee/concurrency/executor/nikita_2_executor")
    private ManagedExecutorService customExecutor;

    Logger logger = LoggerFactory.getLogger(Starter.class);

    @Path("async/{num}")
    @Produces("text/plain")
    @GET
    public String async(@PathParam("num") int num) {

        for(int i =0; i<num; ++i) {
            final int j = i;
            customExecutor.execute(
                    () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            logger.warn(e.getMessage());
                        }
                        logger.info("async iteration {}", j);
                        logger.info("async {}", Thread.currentThread());
                    }
            );
        }

        return "Ok";
    }

    @Path("sync/{num}")
    @Produces("text/plain")
    @GET
    public String sync(@PathParam("num") int num) {
        List<Callable<Void>> callables = new ArrayList<>();

        for(int i =0; i<num; ++i) {
            final int j = i;
            callables.add(() -> {
                Thread.sleep(1000);
                logger.info("sync iteration {}", j);
                logger.info("sync {}", Thread.currentThread());
                return null;
            });
        }

        List<Future<Void>> taskResults;

        try {
            taskResults = customExecutor.invokeAll(callables);
            for (int i =0; i<taskResults.size(); ++i) {
                Future<Void> future = taskResults.get(i);
                future.get();
            }

        } catch (InterruptedException | ExecutionException e) {
            logger.warn(e.getMessage());
        }

        return "Ok";
    }

    @PreDestroy
    public void destroy(){
    }

}
