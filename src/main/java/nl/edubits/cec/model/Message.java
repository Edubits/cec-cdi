package nl.edubits.cec.model;

import java.util.Collections;
import java.util.List;

import nl.edubits.cec.model.parameter.Parameter;

public class Message {

	private final Device source;
	private final Device destination;
	private final Operator operator;
	private List<Parameter> parameters;

	private String rawMessage;

	public Message(Device source, Device destination, Operator operator, List<Parameter> parameters, String rawMessage) {
		this.source = source;
		this.destination = destination;
		this.operator = operator;
		this.parameters = parameters;
		this.rawMessage = rawMessage;
	}

	public Message(Device source, Device destination, Operator operator, List<Parameter> parameters) {
		this(source, destination, operator, parameters, "");
	}

	public Message(Device source, Device destination, Operator operator) {
		this(source, destination, operator, Collections.emptyList());
	}

	public Device getSource() {
		return source;
	}

	public Device getDestination() {
		return destination;
	}

	public Operator getOperator() {
		return operator;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	@Override
	public String toString() {
		return "Message{" + "source=" + source + ", destination=" + destination + ", operator=" + operator + ", parameters=" + parameters + ", raw=" + rawMessage + "}";
	}
}