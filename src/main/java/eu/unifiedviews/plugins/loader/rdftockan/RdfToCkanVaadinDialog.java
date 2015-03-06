package eu.unifiedviews.plugins.loader.rdftockan;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.config.BaseConfigDialog;
import eu.unifiedviews.helpers.dpu.localization.Messages;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends BaseConfigDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = -5668436075836909428L;

    private ObjectProperty<String> catalogApiLocation = new ObjectProperty<String>("");

    private Messages messages;

    public RdfToCkanVaadinDialog() {
        super(RdfToCkanConfig_V1.class);
        initialize();
    }

    private void initialize() {
        messages = new Messages(getContext().getLocale(), this.getClass().getClassLoader());
        FormLayout mainLayout = new FormLayout();

        // top-level component properties
        setWidth("100%");
        setHeight("100%");
        TextField txtCatalogApiLocation = new TextField(messages.getString("RdfToCkanVaadinDialog.catalogApiLocation"), catalogApiLocation);
        txtCatalogApiLocation.setWidth("100%");
        setCompositionRoot(mainLayout);
    }

    @Override
    public void setConfiguration(RdfToCkanConfig_V1 conf)
            throws DPUConfigException {
        catalogApiLocation.setValue(conf.getCatalogApiLocation());
    }

    @Override
    public RdfToCkanConfig_V1 getConfiguration()
            throws DPUConfigException {
        RdfToCkanConfig_V1 conf = new RdfToCkanConfig_V1();
        conf.setCatalogApiLocation(catalogApiLocation.getValue());
        return conf;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
