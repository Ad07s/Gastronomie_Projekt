package com.acme.gastronomie.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.time.Duration;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.ApiVersionInserter;
import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
final class TestConstants {
    static final String SCHEMA = "https";
    static final String HOST = "localhost";
    static final String VERSION_1 = "1.0.0";
    static final ApiVersionInserter API_VERSION_INSERTER = ApiVersionInserter.useHeader("X-Version");

    static final ClientHttpRequestFactory REQUEST_FACTORY;

    private static final int READ_TIMEOUT_IN_MILLIS = 2_000;
    private static final long TIMEOUT_IN_SECONDS = 10;

    static {
        // Pfad zum Zertifikat - muss exakt mit deinem Verzeichnis übereinstimmen
        final var path = Path.of("src", "main", "resources", "certificate.crt");

        final SSLContext sslContext;
        try (var certificateStream = Files.lines(path)) {
            final var certificateBytes = certificateStream.collect(Collectors.joining("\n")).getBytes(UTF_8);
            final var certificateFactory = CertificateFactory.getInstance("X.509");
            final var certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));

            final var truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(null, null);
            truststore.setCertificateEntry("microservice", certificate);

            final var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);

            sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            throw new IllegalStateException(e);
        }
        final var httpClient = HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .sslContext(sslContext)
                .connectTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
                .build();

        final var jdkRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkRequestFactory.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
        REQUEST_FACTORY = jdkRequestFactory;
    }

    private TestConstants() {
        // leerer Rumpf
    }
}