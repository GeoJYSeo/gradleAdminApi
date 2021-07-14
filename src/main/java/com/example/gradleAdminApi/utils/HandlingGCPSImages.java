package com.example.gradleAdminApi.utils;

import com.example.gradleAdminApi.repository.GoodsImageRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class HandlingGCPSImages {
    @Value("${spring.cloud.gcp.project.id}")
    private String gcp_project_id;

    @Value("${spring.cloud.gcp.credentials.location}")
    private String gcp_credentials;

    @Value("${spring.cloud.gcp.storage.bucket.name}")
    private String gcp_storage_bucket_name;

    @Autowired
    private GoodsImageRepository goodsImageRepository;

    public String executeImageUpload(MultipartFile mpRequest, String uuidFileName) {
        try {
            PostPolicyV4 policyV4 = generateV4GPutObjectSignedUrl(uuidFileName);
            HttpEntity<MultiValueMap<String, Object>> body = makeRequestData(policyV4, mpRequest);

            RestTemplate restTemplate = new RestTemplate();
            String gcpUploadUrl = policyV4.getUrl();
            restTemplate.postForObject(gcpUploadUrl, body, String.class);

            return uuidFileName;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public Boolean executeImageDelete(String imageName) {
        try {
            InputStream keyFile = ResourceUtils.getURL(gcp_credentials).openStream();

            Storage storage = StorageOptions.newBuilder().setProjectId(gcp_project_id).setCredentials(GoogleCredentials.fromStream(keyFile)).build().getService();

            return storage.delete(gcp_storage_bucket_name, imageName);
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public URL generateV4GetObjectSignedUrl(String imgName) throws IOException {
        InputStream keyFile = ResourceUtils.getURL(gcp_credentials).openStream();

        Storage storage = StorageOptions.newBuilder().setProjectId(gcp_project_id).setCredentials(GoogleCredentials.fromStream(keyFile)).build().getService();

        // Define resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(gcp_storage_bucket_name, imgName)).build();

        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
    }

    public String makeFilenameWithUUID(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis() / 1000;
    }

    private PostPolicyV4 generateV4GPutObjectSignedUrl(String uuidFileName) throws StorageException, IOException {
        InputStream keyFile = ResourceUtils.getURL(gcp_credentials).openStream();

        Storage storage = StorageOptions.newBuilder().setProjectId(gcp_project_id).setCredentials(GoogleCredentials.fromStream(keyFile)).build().getService();

        PostPolicyV4.PostFieldsV4 fields =
                PostPolicyV4.PostFieldsV4.newBuilder().setCustomMetadataField("test", "data").build();

        return storage.generateSignedPostPolicyV4(
                BlobInfo.newBuilder(gcp_storage_bucket_name, uuidFileName).build(), 10, TimeUnit.MINUTES, fields);
    }

    private HttpEntity<MultiValueMap<String, Object>> makeRequestData(PostPolicyV4 policyV4, MultipartFile mpRequest) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        policyV4.getFields().forEach(body::add);

        HttpHeaders partHeaders = new HttpHeaders();
        partHeaders.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<ByteArrayResource> bytesPart = new HttpEntity<>(new ByteArrayResource(mpRequest.getBytes()), partHeaders);
        body.add("file", bytesPart);

        return new HttpEntity<>(body, headers);
    }
}
