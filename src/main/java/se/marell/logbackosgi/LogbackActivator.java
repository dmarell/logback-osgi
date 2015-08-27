/*
 * Created by Daniel Marell 12-06-20 9:57 PM
 */
package se.marell.logbackosgi;

import org.osgi.framework.*;
import org.osgi.service.log.LogReaderService;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The LogBackendActivator registers a LogbackAdaptor object as a LogListener, and
 * listens to service event, in case some LogReaderService are started or stopped.
 * <p/>
 * It registers the BacklogAdaptor to all the LogReaderService available on the
 * OSGi server.
 *
 * @author Rodrigo Reyes
 */
public class LogbackActivator implements BundleActivator, ServiceListener {
    private LogbackAdaptor backendLogger = new LogbackAdaptor();
    private LinkedList<LogReaderService> readers = new LinkedList<LogReaderService>();
    private BundleContext context;

    public void start(BundleContext context) throws Exception {
        this.context = context;

        //
        // Register this class as a listener to updates of the service list
        //
        String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
        try {
            context.addServiceListener(this, filter);
        } catch (InvalidSyntaxException e) {
            assert false : "addServiceListener failed: " + e.getMessage();
        }

        // Register the LogbackAdaptor to all the LogReaderService objects available
        // on the server. That's right, ALL of them.
        ServiceReference[] refs = context.getServiceReferences(LogReaderService.class.getName(), null);
        if (refs != null) {
            for (ServiceReference ref : refs) {
                LogReaderService service = (LogReaderService) context.getService(ref);
                if (service != null) {
                    readers.add(service);
                    service.addLogListener(backendLogger);
                }
            }
        }
    }

    public void stop(BundleContext context) throws Exception {
        for (Iterator<LogReaderService> i = readers.iterator(); i.hasNext(); ) {
            LogReaderService lrs = i.next();
            lrs.removeLogListener(backendLogger);
            i.remove();
        }
    }

    //  We use a ServiceListener to dynamically keep track of all the LogReaderService service being
    //  registered or unregistered
    public void serviceChanged(ServiceEvent event) {
        LogReaderService lrs = (LogReaderService) context.getService(event.getServiceReference());
        if (lrs != null) {
            if (event.getType() == ServiceEvent.REGISTERED) {
                readers.add(lrs);
                lrs.addLogListener(backendLogger);
            } else if (event.getType() == ServiceEvent.UNREGISTERING) {
                lrs.removeLogListener(backendLogger);
                readers.remove(lrs);
            }
        }
    }

}
