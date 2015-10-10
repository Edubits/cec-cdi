package nl.edubits.cec;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.logging.Logger;

class CecStreamConsumer {

	private static final Logger logger = Logger.getLogger(CecStreamConsumer.class.getName());
	private final String label;
	private final BufferedReader reader;
	private final Consumer<String> consumer;

	CecStreamConsumer(String label, InputStream stream, Consumer<String> consumer) {
		this.label = label;
		reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream)));
		this.consumer = consumer;
	}

	CecStreamConsumer(String label, InputStream stream) {
		this(label, stream, line -> {
			logger.log(INFO, "Error: {0}", line);
		});
	}

	void maybeReadStream() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			logger.log(FINE, "CEC command from ''{0}'': {1}", new Object[]{label, line});
			consumer.accept(line.trim());
		}
	}
}