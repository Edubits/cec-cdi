# cec-cdi
cec-cdi is a CDI Extension to interface with [libcec](https://github.com/Pulse-Eight/libcec) through cec-client.

# Install
1. Install cec-client in `/usr/local/bin/cec-client` (for Raspbian a [premade deb](https://drgeoffathome.wordpress.com/2015/08/09/a-premade-libcec-deb/) is available)
2. Add cec-cdi to pom.xml:
```xml
<dependency>
	<groupId>nl.edubits.cec</groupId>
	<artifactId>cec-cdi</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

# Usage examples

## Observe CEC messages
```java
@ApplicationScoped
public class CecObserverBean {
	public void tvMessage(@Observes @CecSource(TV) Message message) {
		logger.info("Message received from TV: " + message);
	}
}
```

## Send CEC message
```java
public class SendExample {

	@Inject
	private CecConnection connection;
	
	public void send() {
		// Send message from RECORDER1 (by default the device running this code) to the TV to turn on
		connection.sendMessage(new Message(RECORDER1, TV, IMAGE_VIEW_ON, Collections.emptyList(), ""));
		
		// Send message from RECORDER1 (by default the device running this code) to the TV to turn off
		connection.sendMessage(new Message(RECORDER1, TV, STANDBY, Collections.emptyList(), ""));
	}
}
```

# Resources

- [libcec](https://github.com/Pulse-Eight/libcec)
- [CEC-O-MATIC](http://www.cec-o-matic.com) – Translate CEC messages
