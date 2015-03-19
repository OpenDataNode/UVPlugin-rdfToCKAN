package eu.unifiedviews.plugins.loader.rdftockan;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends AbstractDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = 3526833561546876289L;

    public RdfToCkanVaadinDialog() {
        super(RdfToCkan.class);
    }

    @Override
    protected void buildDialogLayout() {
        // No dialog for this DPU
    }

    @Override
    public void setConfiguration(RdfToCkanConfig_V1 conf) throws DPUConfigException {
        // No configuration for this DPU
    }

    @Override
    public RdfToCkanConfig_V1 getConfiguration() throws DPUConfigException {
        RdfToCkanConfig_V1 conf = new RdfToCkanConfig_V1();
        return conf;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
