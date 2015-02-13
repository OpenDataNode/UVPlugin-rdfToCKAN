package eu.unifiedviews.plugins.loader.rdftockan;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RdfToCkanConfig_V1 {
    private String catalogApiLocation = "http://localhost/internalcatalog/uv";

    private Long pipelineId = 0L;

    private String userId = "mvi";

    private String token = "secret_token";

    public RdfToCkanConfig_V1() {
    }

    public String getCatalogApiLocation() {
        return catalogApiLocation;
    }

    public void setCatalogApiLocation(String catalogApiLocation) {
        this.catalogApiLocation = catalogApiLocation;
    }

    public Long getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Long pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
