package eu.unifiedviews.plugins.loader.rdftockan;

import com.vaadin.ui.FormLayout;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.config.BaseConfigDialog;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends BaseConfigDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = -5668436075836909428L;

    public RdfToCkanVaadinDialog() {
        super(RdfToCkanConfig_V1.class);
        initialize();
    }

    private void initialize() {
        FormLayout mainLayout = new FormLayout();

        // top-level component properties
        setWidth("100%");
        setHeight("100%");

        setCompositionRoot(mainLayout);
    }

    @Override
    public void setConfiguration(RdfToCkanConfig_V1 conf)
            throws DPUConfigException {
    }

    @Override
    public RdfToCkanConfig_V1 getConfiguration()
            throws DPUConfigException {
        RdfToCkanConfig_V1 conf = new RdfToCkanConfig_V1();
        return conf;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
