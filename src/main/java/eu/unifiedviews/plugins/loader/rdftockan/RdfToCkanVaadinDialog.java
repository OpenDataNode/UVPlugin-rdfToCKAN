package eu.unifiedviews.plugins.loader.rdftockan;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.config.BaseConfigDialog;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends BaseConfigDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = -5668436075836909428L;

    private static final String CATALOG_API_LOCATION_LABEL = "Catalog API location";

    private static final String PIPELINE_ID_LABEL = "Pipeline ID";

    private ObjectProperty<String> catalogApiLocation = new ObjectProperty<String>("");

    private ObjectProperty<Long> pipelineId = new ObjectProperty<Long>(0L);

    public RdfToCkanVaadinDialog() {
        super(RdfToCkanConfig_V1.class);
        initialize();
    }

    private void initialize() {
        FormLayout mainLayout = new FormLayout();

        // top-level component properties
        setWidth("100%");
        setHeight("100%");

        TextField txtApiLocation = new TextField(CATALOG_API_LOCATION_LABEL, catalogApiLocation);
        txtApiLocation.setWidth("100%");
        mainLayout.addComponent(txtApiLocation);

        TextField txtPipelineId = new TextField(PIPELINE_ID_LABEL, pipelineId);
        txtPipelineId.setWidth("100%");
        mainLayout.addComponent(txtPipelineId);
        setCompositionRoot(mainLayout);
    }

    @Override
    public void setConfiguration(RdfToCkanConfig_V1 conf)
            throws DPUConfigException {
        catalogApiLocation.setValue(conf.getCatalogApiLocation());
        pipelineId.setValue(conf.getPipelineId());
    }

    @Override
    public RdfToCkanConfig_V1 getConfiguration()
            throws DPUConfigException {
        RdfToCkanConfig_V1 conf = new RdfToCkanConfig_V1();
        conf.setCatalogApiLocation(catalogApiLocation.getValue());
        conf.setPipelineId(pipelineId.getValue());
        return conf;
    }

    @Override
    public String getDescription() {
        return catalogApiLocation.getValue();
    }
}
