package nl.edubits.cec.model;

import static nl.edubits.cec.model.parameter.ParameterType.POWER_STATUS;
import static nl.edubits.cec.model.parameter.ParameterType.RAW;
import static nl.edubits.cec.model.parameter.ParameterType.UI_COMMAND;

import java.util.Arrays;

import nl.edubits.cec.model.parameter.ParameterType;

public enum Operator {
	ACTIVE_SOURCE(0x82, RAW),
	INACTIVE_SOURCE(0x9D, RAW),
	REQUEST_ACTIVE_SOURCE(0x85),

	ROUTING_CHANGE(0x80, RAW),
	ROUTING_INFORMATION(0x81, RAW),
	SET_STREAM_PATH(0x86, RAW),

	CEC_VERSION(0x9E, RAW),
	GET_CEC_VERSION(0x9F),
	GIVE_PHYSICAL_ADDRESS(0x83),
	REPORT_PHYSICAL_ADDRESS(0x84, RAW),
	GET_MENU_LANGUAGE(0x91),
	SET_MENU_LANGUAGE(0x32, RAW),

	SET_OSD_STRING(0x64, RAW),
	GIVE_OSD_NAME(0x46),
	SET_OSD_NAME(0x47, RAW),

	DEVICE_VENDOR_ID(0x87, RAW),
	GIVE_DEVICE_VENDOR_ID(0x8c),
	VENDOR_COMMAND(0x89, RAW),
	VENDOR_COMMAND_WITH_ID(0xA0, RAW),

	IMAGE_VIEW_ON(0x04),
	TEXT_VIEW_ON(0x0D),

	STANDBY(0x36),
	GIVE_DEVICE_POWER_STATUS(0x8F),
	REPORT_POWER_STATUS(0x90, POWER_STATUS),

	GIVE_AUDIO_STATUS(0x71),
	SET_SYSTEM_AUDIO_MODE(0x72, POWER_STATUS),
	GIVE_SYSTEM_AUDIO_MODE_STATUS(0x7D),
	REPORT_AUDIO_STATUS(0x7A, POWER_STATUS),
	SYSTEM_AUDIO_MODE_REQUEST(0x70, RAW),
	SYSTEM_AUDIO_MODE_STATUS(0x7E, RAW),

	USER_CONTROL_PRESSED(0x44, UI_COMMAND),
	USER_CONTROL_RELEASED(0x45),
	VENDOR_REMOTE_BUTTON_DOWN(0x8A, RAW),
	VENDOR_REMOTE_BUTTON_UP(0x8B),
	MENU_REQUEST(0x8D, RAW),
	MENU_STATUS(0x8E, RAW),

	SET_AUDIO_RATE(0x9A, RAW),

	FEATURE_ABORT(0x00, RAW),
	ABORT(0xFF),
	POLLING_MESSAGE(-1),
	UNKNOWN(-2);

	private final int id;
	private final ParameterType parameterType;

	Operator(int id) {
		this(id, null);
	}

	Operator(int id, ParameterType parameterType) {
		this.id = id;
		this.parameterType = parameterType;
	}

	public int getId() {
		return id;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	public static Operator operatorById(int id) {
		return Arrays.stream(Operator.values())
				 	 .filter(operator -> operator.id == id)
				 	 .findFirst()
				 	 .orElse(UNKNOWN);
	}
}
