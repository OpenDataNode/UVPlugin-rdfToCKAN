package org.opendatanode.plugins.loader.rdftockan;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.UserDialogContext;
import eu.unifiedviews.plugins.loader.rdftockan.RdfToCkanConfig_V1;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class RdfToCkanVaadinDialog extends AbstractDialog<RdfToCkanConfig_V1> {

    private static final long serialVersionUID = 3526833561546876289L;
    private TextField txtResourceName;

    private VerticalLayout mainLayout;
    public RdfToCkanVaadinDialog() {
        super(RdfToCkan.class);
    }

    public void outerBuildDialogLayout(UserDialogContext ctx) {
        this.ctx = ctx;
        buildDialogLayout();
    }
    
    @Override
    protected void buildDialogLayout() {
        setWidth("100%");
        setHeight("100%");

        this.mainLayout = new VerticalLayout();
        this.mainLayout.setWidth("100%");
        this.mainLayout.setHeight("-1px");
        this.mainLayout.setSpacing(true);
        this.mainLayout.setMargin(true);

        this.txtResourceName = new TextField();
        this.txtResourceName.setNullRepresentation("");
        this.txtResourceName.setRequired(false);
        this.txtResourceName.setCaption(this.ctx.tr("RdfToCkanVaadinDialog.ckan.resource.name"));
        this.txtResourceName.setWidth("100%");
        this.txtResourceName.setDescription(this.ctx.tr("RdfToCkanVaadinDialog.resource.name.help"));
        this.mainLayout.addComponent(this.txtResourceName);
        setCompositionRoot(this.mainLayout);
    }

    @Override
    public void setConfiguration(RdfToCkanConfig_V1 conf) throws DPUConfigException {
        this.txtResourceName.setValue(conf.getResourceName());
    }

    @Override
    public RdfToCkanConfig_V1 getConfiguration() throws DPUConfigException {
        RdfToCkanConfig_V1 conf = new RdfToCkanConfig_V1();
        conf.setResourceName(this.txtResourceName.getValue());
        return conf;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
