package nl.edubits.cec.model.parameter;

import java.util.EnumSet;

public enum ParameterType {

	POWER_STATUS(EnumSet.allOf(PowerStatus.class)),
	UI_COMMAND(EnumSet.allOf(UICommand.class)),
	MENU_STATE(EnumSet.allOf(MenuState.class)),
	RAW(EnumSet.allOf(RawParameter.class));

	private final EnumSet<? extends Parameter> enumSet;

	ParameterType(EnumSet<? extends Parameter> enumSet) {
		this.enumSet = enumSet;
	}

	public EnumSet<? extends Parameter> getEnumSet() {
		return enumSet;
	}

}
