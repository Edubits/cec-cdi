package nl.edubits.cec;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.edubits.cec.model.Message;

public class CecListener implements Runnable, AutoCloseable {

	private static final Logger logger = Logger.getLogger(CecListener.class.getName());
	private static final int SLEEP_BETWEEN_RETRIES = 1000;
	private static final int EMPTY_STREAM_DELAY = 500;

	private BeanManager beanManager;
	private ExecutorService executorService;

	private final String command;
	private Process process;
	InputStream inputStream;
	InputStream errorStream;
	OutputStream outputStream;
	int maxRetryCount = 10;
	boolean keepAlive = true;

	public CecListener(String command, BeanManager beanManager) {
		this.command = command;
		this.beanManager = beanManager;
	}

	@Override
	public void run() {
		int retryCount = 1;
		do {
			try {
				try {
					executorService = InitialContext.doLookup("java:comp/DefaultManagedExecutorService");
				} catch (NamingException e) {
					executorService = new ThreadPoolExecutor(16, 16, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
				}

				logger.log(INFO, "Starting cec-listener on command line. Attempt {0}", retryCount);
				openStreams();
				CecStreamConsumer is = new CecStreamConsumer("InputStream", inputStream, line -> fireEvent(line));
				CecStreamConsumer es = new CecStreamConsumer("ErrorStream", errorStream);
				do {
					is.maybeReadStream();
					es.maybeReadStream();
					sleep(EMPTY_STREAM_DELAY); // don't hammer the poor raspi down
				} while (keepAlive);

			} catch (IOException ex) {
				logger.log(WARNING, "Error reading CEC stream from commandline!", ex);
			} catch (InterruptedException ex) {
				logger.log(SEVERE, "Thread interrupted", ex);
			} finally {
				close();
			}

			// set asleep after an error. Maybe the call is simply invalid. In this case I want to avoid going
			// into an infinite loop
			try {
				sleep(SLEEP_BETWEEN_RETRIES);
			} catch (InterruptedException ex) {
				logger.info("Interrupted CECListener shutting down");
				return;
			}
		} while (keepAlive && retryCount++ < maxRetryCount);
	}

	@Override
	public void close() {
		if (process != null) {
			process.destroy();
		}
		closeStream(inputStream);
		closeStream(errorStream);
		closeStream(outputStream);
	}

	private void fireEvent(String line) {
		Optional<Message> optionalMessage = CecUtils.parseMessage(line);
		if (optionalMessage.isPresent()) {
			executorService.execute(new MessageAsyncSender(optionalMessage.get()));
		} else {
			logger.log(INFO, "Could not parse: {0}", line);
		}
	}

	private class MessageAsyncSender implements Runnable {

		private Message message;

		public MessageAsyncSender(Message message) {
			this.message = message;
		}

		@Override
		public void run() {
			logger.log(INFO, "Event fired: {0}", message);
			beanManager.fireEvent(message, new CecSourceAnnotationLiteral(message.getSource()));
		}

	}

	void openStreams() throws IOException {
		logger.log(INFO, "Open streams: {0}", command);

		ProcessBuilder pb = new ProcessBuilder(command.split(" "));
		process = pb.start();

		inputStream = process.getInputStream();
		errorStream = process.getErrorStream();
		outputStream = process.getOutputStream();
	}

	private void closeStream(Closeable is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
	}

}