package nl.edubits.cec;

import javax.enterprise.util.AnnotationLiteral;

import nl.edubits.cec.model.Device;

public class CecSourceAnnotationLiteral extends AnnotationLiteral<CecSource> implements CecSource {

	private static final long serialVersionUID = 1138839897412013649L;

	private Device value;

	public CecSourceAnnotationLiteral(Device value) {
		this.value = value;
	}

	@Override
	public Device value() {
		return value;
	}

}
