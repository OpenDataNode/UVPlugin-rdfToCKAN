package eu.unifiedviews.plugins.loader.rdftockan;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RdfToCkanConfig_V1 {
    private String catalogApiLocation;

    private String secretToken;

    public String getCatalogApiLocation() {
        return catalogApiLocation;
    }

    public void setCatalogApiLocation(String catalogApiLocation) {
        this.catalogApiLocation = catalogApiLocation;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public RdfToCkanConfig_V1() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
