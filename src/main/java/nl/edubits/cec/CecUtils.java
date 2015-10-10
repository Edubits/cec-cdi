package nl.edubits.cec;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toHexString;
import static nl.edubits.cec.model.Operator.POLLING_MESSAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import nl.edubits.cec.model.Device;
import nl.edubits.cec.model.Message;
import nl.edubits.cec.model.Operator;
import nl.edubits.cec.model.parameter.Parameter;
import nl.edubits.cec.model.parameter.ParameterType;

public class CecUtils {

	private static final Logger logger = Logger.getLogger(CecUtils.class.getName());

	public static final String TRAFFIC = "TRAFFIC";
	public static final Pattern DIRECTION_PATTERN = Pattern.compile(">>|<<");
	public static final Pattern CODE_PATTERN = Pattern.compile("^[a-f0-9]{2}(:[a-f0-9]{2})*$");

	public static Optional<Message> parseMessage(String line) {
		logger.info("Received: "+line);

		String code = extractCode(line).orElse("");
		if (code.length() < 2) {
			return Optional.empty();
		}

		Device source = Device.deviceByInitiatorAddress(parseInt(code.substring(0, 1), 16));
		Device destination = Device.deviceByDestinationAddress(parseInt(code.substring(1, 2), 16));
		Operator operator = Operator.operatorById(code.length() > 3 ? parseInt(code.substring(3, 5), 16) : POLLING_MESSAGE.getId());

		// TODO: Change to Map<CecParameter, String> to support raw values
		List<Parameter> parameters = new ArrayList<>();

		ParameterType parameterType = operator.getParameterType();
		if (parameterType != null && code.length() >= 7) {
			int parameterId = parseInt(code.substring(6, 8), 16);
			parameters.addAll(
					parameterType.getEnumSet().stream()
											  .filter(parameter -> parameter.getId() == parameterId)
											  .collect(Collectors.toList()));
		}

		return Optional.of(new Message(source, destination, operator, parameters, code));
	}

	public static String createRawMessage(Message message) {
		return createRawMessage(message.getSource(), message.getDestination(), message.getOperator(), message.getParameters());
	}

	public static String createRawMessage(Device source, Device destination, Operator operator, List<Parameter> parameters) {
		if (operator == Operator.STANDBY) {
			return "standby "+destination.getDestinationAddress();
		} else if (operator == Operator.IMAGE_VIEW_ON) {
			return "on "+destination.getDestinationAddress();
		}

		StringBuilder sb = new StringBuilder();

		sb.append(toHexString(source.getInitiatorAddress()));
		sb.append(toHexString(destination.getDestinationAddress()));

		sb.append(':');

		sb.append(String.format("%02X", operator.getId()));

		if (!parameters.isEmpty()) {
			sb.append(':');

			// TODO: handle parameters of more than 1 octet correctly
			parameters.forEach(parameter -> sb.append(toHexString(parameter.getId())));
		}

		return "tx "+sb.toString();
	}

	private static Optional<String> extractCode(String line) {
		line = (line == null) ? "" : line.trim();
		if (line == null
				|| !line.startsWith(TRAFFIC)
				|| !DIRECTION_PATTERN.matcher(line).find()) {
			return Optional.empty();
		}

		String[] split = DIRECTION_PATTERN.split(line);
		if (split.length != 2) {
			return Optional.empty();
		}

		String code = split[1].trim();
		if (!CODE_PATTERN.matcher(code).matches()) {
			return Optional.empty();
		}

		return Optional.of(code);
	}

}