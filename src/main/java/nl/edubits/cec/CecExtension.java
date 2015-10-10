package nl.edubits.cec;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CecExtension implements Extension {

	private CecConnection connection;

	void afterDeploymentValidation(@Observes AfterDeploymentValidation afterDeploymentValidation, BeanManager beanManager) {
		// Injected connection is not available yet
		CecConnection connection = CDI.current().select(CecConnection.class).get();
		newThread(connection).start();
	}

	void shutdown(@Observes BeforeShutdown beforeShutdown) {
		connection.close();
	}

	private Thread newThread(Runnable runnable) {
		ThreadFactory threadFactory;
		try {
			threadFactory = InitialContext.doLookup("java:comp/DefaultManagedThreadFactory");
		} catch (NamingException e) {
			threadFactory = Executors.defaultThreadFactory();
		}
		return threadFactory.newThread(runnable);
	}

}
