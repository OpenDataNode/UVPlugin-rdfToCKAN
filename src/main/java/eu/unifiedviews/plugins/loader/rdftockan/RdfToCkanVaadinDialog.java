package eu.unifiedviews.plugins.loader.rdftockan;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends AbstractDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = -4773062982259181847L;

    private ObjectProperty<String> catalogApiLocation = new ObjectProperty<String>("");

    public RdfToCkanVaadinDialog() {
        super(RdfToCkan.class);
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

    @Override
    protected void buildDialogLayout() {
        FormLayout mainLayout = new FormLayout();

        // top-level component properties
        setWidth("100%");
        setHeight("100%");
        TextField txtCatalogApiLocation = new TextField(this.ctx.tr("RdfToCkanVaadinDialog.catalogApiLocation"), catalogApiLocation);
        txtCatalogApiLocation.setWidth("100%");
        setCompositionRoot(mainLayout);
    }
}
