package p;

/**
 * Created by Nikita on 11.11.2015.
 */
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

@Stateless(name="Executor2")
public class ExecutorBean implements Executor {

    @Asynchronous
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}