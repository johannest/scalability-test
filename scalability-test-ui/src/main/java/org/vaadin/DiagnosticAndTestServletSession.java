package org.vaadin;

import com.vaadin.data.RpcDataProviderExtension;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

import java.util.logging.Logger;

public class DiagnosticAndTestServletSession extends VaadinSession {
	private static final long serialVersionUID = 4596901275146146127L;

	public DiagnosticAndTestServletSession(VaadinService service) {
		super(service);
	}

	@Override
	public String createConnectorId(ClientConnector connector) {
		if (connector instanceof Component) {
			Component component = (Component) connector;
			return component.getId() == null ? super
					.createConnectorId(connector) : component.getId();
		}
		if (connector.getClass().equals(RpcDataProviderExtension.class)) {
			return "rpcdataprovider";
		}
		
		String createConnectorId = super.createConnectorId(connector);
		Logger.getLogger(DiagnosticAndTestServletSession.class.getName()).info("Created connector id "+createConnectorId+" for "+connector.getClass());
		return createConnectorId;
	}
}