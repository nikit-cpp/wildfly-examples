package p;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by nik on 22.11.15.
 */
@Path("/mail")
@Stateless
public class EmailWs {

    @Inject
    private Email email;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/send")
    public EmailWsResponse sendMail(EmailWsRequest request) {
        email.setBody(request.body);
        email.setFrom(request.from);
        email.setSubject(request.subject);
        email.setTo(request.to);

        try {
            email.send();
        } catch (Exception e) {
            return new EmailWsResponse(1, e.getLocalizedMessage());
        }
        return new EmailWsResponse(0, "");
    }

    public static class EmailWsRequest {
        public String from;
        public String to;
        public String subject;
        public String body;
    }

    public static class EmailWsResponse {
        public int status;
        public String errorMessage;

        public EmailWsResponse(int i, String localizedMessage) {
            status = i;
            errorMessage = localizedMessage;
        }
    }

    @GET
    @Path("/getmail")
    public String getInstAddress() {
        return email.toString();
    }
}
