package eu.unifiedviews.plugins.loader.rdftockan;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openrdf.rio.UnsupportedRDFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.rdf.RDFDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUContext;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.rdf.RDFHelper;
import eu.unifiedviews.helpers.dataunit.resource.Resource;
import eu.unifiedviews.helpers.dataunit.resource.ResourceConverter;
import eu.unifiedviews.helpers.dataunit.resource.ResourceHelpers;
import eu.unifiedviews.helpers.dataunit.resource.ResourceMerger;
import eu.unifiedviews.helpers.dataunit.virtualgraph.VirtualGraphHelpers;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;
import eu.unifiedviews.helpers.dpu.exec.UserExecContext;

@DPU.AsLoader
public class RdfToCkan extends AbstractDpu<RdfToCkanConfig_V1> {
    public static final String distributionSymbolicName = "distributionMetadata";

    public static final String PROXY_API_ACTION = "action";

    public static final String PROXY_API_PIPELINE_ID = "pipeline_id";

    public static final String PROXY_API_USER_ID = "user_id";

    public static final String PROXY_API_TOKEN = "token";

    public static final String PROXY_API_TYPE = "type";

    public static final String PROXY_API_TYPE_RDF = "RDF";

    public static final String PROXY_API_STORAGE_ID = "value";

    public static final String PROXY_API_DATA = "data";

    public static final String PROXY_API_ATTACHMENT_NAME = "upload";

    public static final String CKAN_API_PACKAGE_SHOW = "package_show";

    public static final String CKAN_API_RESOURCE_UPDATE = "resource_update";

    public static final String CKAN_API_RESOURCE_CREATE = "resource_create";

    /**
     * @deprecated Global configuration should be used {@link CONFIGURATION_SECRET_TOKEN}
     */
    @Deprecated
    public static final String CONFIGURATION_DPU_SECRET_TOKEN = "dpu.uv-l-rdfToCkan.secret.token";

    /**
     * @deprecated Global configuration should be used {@link CONFIGURATION_CATALOG_API_LOCATION}
     */
    @Deprecated
    public static final String CONFIGURATION_DPU_CATALOG_API_LOCATION = "dpu.uv-l-rdfToCkan.catalog.api.url";

    public static final String CONFIGURATION_SECRET_TOKEN = "org.opendatanode.CKAN.secret.token";

    public static final String CONFIGURATION_CATALOG_API_LOCATION = "org.opendatanode.CKAN.api.url";

    public static final String CONFIGURATION_HTTP_HEADER = "org.opendatanode.CKAN.http.header.";

    private static final Logger LOG = LoggerFactory.getLogger(RdfToCkan.class);

    private static final String CKAN_API_ACTOR_ID = "actor_id";

    private DPUContext dpuContext;

    @DataUnit.AsInput(name = "rdfInput")
    public RDFDataUnit rdfInput;

    @DataUnit.AsInput(name = "distributionInput", optional = true)
    public RDFDataUnit distributionInput;

    public RdfToCkan() {
        super(RdfToCkanVaadinDialog.class, ConfigHistory.noHistory(RdfToCkanConfig_V1.class));
    }

    public JsonObject packageShow(UserExecContext ctx, String catalogApiLocation, String pipelineId, String userId, String secretToken, Map<String, String> additionalHttpHeaders) throws DPUException {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder uriBuilder;
            uriBuilder = new URIBuilder(catalogApiLocation);

            uriBuilder.setPath(uriBuilder.getPath());
            HttpPost httpPost = new HttpPost(uriBuilder.build().normalize());
            for (Map.Entry<String, String> additionalHeader : additionalHttpHeaders.entrySet()) {
                httpPost.addHeader(additionalHeader.getKey(), additionalHeader.getValue());
            }

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .addTextBody(PROXY_API_DATA, "{}", ContentType.APPLICATION_JSON.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_PIPELINE_ID, pipelineId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_USER_ID, userId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_TOKEN, secretToken, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                    .addTextBody(PROXY_API_ACTION, CKAN_API_PACKAGE_SHOW, ContentType.TEXT_PLAIN.withCharset("UTF-8"));

            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            JsonReaderFactory readerFactory = Json.createReaderFactory(Collections.<String, Object> emptyMap());
            JsonReader reader = readerFactory.createReader(response.getEntity().getContent());
            JsonObject resourceResponse = reader.readObject();
            if (response.getStatusLine().getStatusCode() == 200) {
                if (resourceResponse.getBoolean("success")) {
                    LOG.info("Response:" + EntityUtils.toString(response.getEntity()));
                } else {
                    LOG.warn("Response:" + EntityUtils.toString(response.getEntity()));
                    throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.noDataset");
                }
            } else {
                throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.noDataset");
            }
            return resourceResponse;
        } catch (URISyntaxException | IllegalStateException | IOException ex) {
            throw ContextUtils.dpuException(ctx, ex, "RdfToCkan.execute.exception.noDataset");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    LOG.warn("Error in close", ex);
                }
            }
        }
    }

    public void outerExecute(UserExecContext ctx, RdfToCkanConfig_V1 config) throws DPUException {
        this.dpuContext = ctx.getExecMasterContext().getDpuContext();
        ContextUtils.sendShortInfo(ctx, "RdfToCkan.execute.start", this.getClass().getSimpleName());
        Map<String, String> environment = dpuContext.getEnvironment();

        String secretToken = environment.get(CONFIGURATION_SECRET_TOKEN);
        if (isEmpty(secretToken)) {
            secretToken = environment.get(CONFIGURATION_DPU_SECRET_TOKEN);
            if (isEmpty(secretToken)) {
                throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.missingSecretToken");
            }
        }
        String userId = (this.dpuContext.getPipelineExecutionOwnerExternalId() != null) ? this.dpuContext.getPipelineExecutionOwnerExternalId()
                : this.dpuContext.getPipelineExecutionOwner();
        String pipelineId = String.valueOf(dpuContext.getPipelineId());

        String catalogApiLocation = environment.get(CONFIGURATION_CATALOG_API_LOCATION);
        if (isEmpty(catalogApiLocation)) {
            catalogApiLocation = environment.get(CONFIGURATION_DPU_CATALOG_API_LOCATION);
            if (isEmpty(catalogApiLocation)) {
                throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.missingCatalogApiLocation");
            }
        }

        Map<String, String> additionalHttpHeaders = new HashMap<>();
        for (Map.Entry<String, String> configEntry : environment.entrySet()) {
            if (configEntry.getKey().startsWith(CONFIGURATION_HTTP_HEADER)) {
                String headerName = configEntry.getKey().replace(CONFIGURATION_HTTP_HEADER, "");
                String headerValue = configEntry.getValue();
                additionalHttpHeaders.put(headerName, headerValue);
            }
        }

        if (rdfInput == null) {
            throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.missingInput");
        }
        Set<RDFDataUnit.Entry> graphs;
        try {
            graphs = RDFHelper.getGraphs(rdfInput);
        } catch (DataUnitException ex1) {
            throw ContextUtils.dpuException(ctx, ex1, "RdfToCkan.execute.exception.dataunit");
        }

        Resource distributionFromRdfInput = null;
        if (distributionInput != null) {
            if (graphs.size() != 1) {
                throw ContextUtils.dpuException(this.ctx, "RdfToCkan.execute.exception.tooManyFilesForOneDistribution");
            }
            try {
                distributionFromRdfInput = ResourceHelpers.getResource(distributionInput, distributionSymbolicName);
            } catch (DataUnitException ex) {
                throw ContextUtils.dpuException(this.ctx, "FilesToCkan.execute.exception.dataunit");
            }
        }

        Map<String, String> existingResources = new HashMap<>();
        JsonObject resourceResponsePackageShow = this.packageShow(ctx, catalogApiLocation, pipelineId, userId, secretToken, additionalHttpHeaders);
        JsonArray resources = resourceResponsePackageShow.getJsonObject("result").getJsonArray("resources");
        if (resources != null) {
            for (JsonObject resource : resources.getValuesAs(JsonObject.class)) {
                existingResources.put(resource.getString("name"), resource.getString("id"));
            }
        }

        JsonBuilderFactory factory = Json.createBuilderFactory(Collections.<String, Object> emptyMap());
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            for (RDFDataUnit.Entry graph : graphs) {
                CloseableHttpResponse responseUpdate = null;
                boolean bResourceExists = false;
                try {
                    String resourceName = null;
                    String storageId = VirtualGraphHelpers.getVirtualGraph(rdfInput, graph.getSymbolicName());
                    if (config.getResourceName() != null) {
                        resourceName = config.getResourceName();
                    }
                    Resource resource = ResourceHelpers.getResource(rdfInput, graph.getSymbolicName());
                    if (distributionFromRdfInput != null) {
                        Resource mergedDistribution = ResourceMerger.merge(distributionFromRdfInput, resource);
                        resource = mergedDistribution;
                        if (StringUtils.isEmpty(resourceName)) {
                            resourceName = resource.getName();
                        }
                    }
                    if (StringUtils.isEmpty(resourceName)) {
                        resourceName = VirtualGraphHelpers.getVirtualGraph(rdfInput, graph.getSymbolicName());
                    }
                    if (StringUtils.isEmpty(resourceName)) {
                        resourceName = graph.getSymbolicName();
                    }
                    if (existingResources.containsKey(resourceName)) {
                        bResourceExists = true;
                        // If resource already exists, created time should not be sent so original created time is preserved
                        resource.setCreated(null);
                    }
                    resource.setName(resourceName);
                    JsonObjectBuilder resourceBuilder = buildResource(factory, resource);
                    if (bResourceExists) {
                        resourceBuilder.add("id", existingResources.get(resourceName));
                    }

                    URIBuilder uriBuilder = new URIBuilder(catalogApiLocation);
                    uriBuilder.setPath(uriBuilder.getPath());
                    HttpPost httpPost = new HttpPost(uriBuilder.build().normalize());
                    for (Map.Entry<String, String> additionalHeader : additionalHttpHeaders.entrySet()) {
                        httpPost.addHeader(additionalHeader.getKey(), additionalHeader.getValue());
                    }

                    MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                            .addTextBody(PROXY_API_TYPE, PROXY_API_TYPE_RDF, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_STORAGE_ID, storageId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_PIPELINE_ID, pipelineId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_USER_ID, userId, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_TOKEN, secretToken, ContentType.TEXT_PLAIN.withCharset("UTF-8"))
                            .addTextBody(PROXY_API_DATA, resourceBuilder.build().toString(), ContentType.APPLICATION_JSON.withCharset("UTF-8"));

                    if (bResourceExists) {
                        builder.addTextBody(PROXY_API_ACTION, CKAN_API_RESOURCE_UPDATE, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                    } else {
                        builder.addTextBody(PROXY_API_ACTION, CKAN_API_RESOURCE_CREATE, ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                    }
                    HttpEntity entity = builder.build();
                    httpPost.setEntity(entity);

                    responseUpdate = client.execute(httpPost);
                    if (responseUpdate.getStatusLine().getStatusCode() == 200) {
                        JsonReaderFactory readerFactory = Json.createReaderFactory(Collections.<String, Object> emptyMap());
                        JsonReader reader = readerFactory.createReader(responseUpdate.getEntity().getContent());
                        JsonObject resourceResponse = reader.readObject();
                        if (resourceResponse.getBoolean("success")) {
                            LOG.info("Response:" + EntityUtils.toString(responseUpdate.getEntity()));
                        } else {
                            LOG.warn("Response:" + EntityUtils.toString(responseUpdate.getEntity()));
                            throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.fail");
                        }
                    } else {
                        LOG.warn("Response:" + EntityUtils.toString(responseUpdate.getEntity()));
                        throw ContextUtils.dpuException(ctx, "RdfToCkan.execute.exception.fail");
                    }
                } catch (UnsupportedRDFormatException | DataUnitException | IOException | URISyntaxException ex) {
                    throw ContextUtils.dpuException(ctx, ex, "RdfToCkan.execute.exception.fail");
                } finally {
                    if (responseUpdate != null) {
                        try {
                            responseUpdate.close();
                        } catch (IOException ex) {
                            LOG.warn("Error in close", ex);
                        }
                    }
                }
            }
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                LOG.warn("Error in close", ex);
            }
        }
    }

    private static boolean isEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    protected void innerExecute() throws DPUException {
        outerExecute(ctx, config);
    }

    private JsonObjectBuilder buildResource(JsonBuilderFactory factory, Resource resource) {
        JsonObjectBuilder resourceBuilder = factory.createObjectBuilder();
        for (Map.Entry<String, String> mapEntry : ResourceConverter.resourceToMap(resource).entrySet()) {
            resourceBuilder.add(mapEntry.getKey(), mapEntry.getValue());
        }
        for (Map.Entry<String, String> mapEntry : ResourceConverter.extrasToMap(resource.getExtras()).entrySet()) {
            resourceBuilder.add(mapEntry.getKey(), mapEntry.getValue());
        }
        if (this.dpuContext.getPipelineExecutionActorExternalId() != null) {
            resourceBuilder.add(CKAN_API_ACTOR_ID, this.dpuContext.getPipelineExecutionActorExternalId());
        }

        return resourceBuilder;
    }
}
