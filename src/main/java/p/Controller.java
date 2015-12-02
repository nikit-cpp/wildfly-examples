package p;

import multipart.Example;
import multipart.MultipartRequestMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Nikita on 11.11.2015.
 */
@Path("/")
@Stateless
public class Controller {

    @EJB(lookup = "java:jboss/ee/concurrency/executor/nikita_2_executor")
    private ManagedExecutorService customExecutor;

    Logger logger = LoggerFactory.getLogger(Controller.class);

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




    // http://blogs.steeplesoft.com/posts/2014/file-uploads-with-jax-rs-2.html
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response formPost(@Context HttpServletRequest request) {
        try {
            MultipartRequestMap map = new MultipartRequestMap(request);
            Example example = new Example();
            example.setName(map.getStringParameter("name"));
            example.setAttachment(readFile(map.getFileParameter("attachment")));

            return Response.ok(buildMessage(example.getName(), example.getAttachment().length)).build();
        } catch (Exception ex) {
            logger.error("exception: ", ex);
        }
        return Response.serverError().build();
    }

    private Object buildMessage(String name, int length) {
        MultipartFormResponse mr = new MultipartFormResponse();
        mr.length = length;
        mr.name = name;
        return mr;
    }

    private byte[] readFile(File attachment) throws IOException {
        if(null == attachment) {
            return new byte[0];
        }
        return FileUtils.readFileToByteArray(attachment);
    }

    public static class MultipartFormResponse {
        public String name;
        public int length;
    }
}
