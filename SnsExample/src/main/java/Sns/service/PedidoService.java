package Sns.service;

import Sns.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class PedidoService {
    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final SnsClient snsClient;

    private final ObjectMapper objectMapper;

    private static final String TOPIC_ARN =
            "arn:aws:sns:us-east-1:105927215094:academia-aws-aula-8";

    public PedidoService(SnsClient snsClient, ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
    }

    public String publish(Product product) {

        PublishRequest request = PublishRequest.builder()
                .topicArn(TOPIC_ARN)
                .subject("Pedido Enviado!")
                .message(objectMapper.writeValueAsString(product))
                .build();

        PublishResponse response = snsClient.publish(request);

        log.info("Mensagem publicada no topic. MessageId: {}", response.messageId());

        return "Pedido Enviado. ID: " + response.messageId();
    }

    public String subscribeEmail(String email) {

        SubscribeRequest request = SubscribeRequest.builder()
                .topicArn(TOPIC_ARN)
                .protocol("email")
                .endpoint(email)
                .build();

        SubscribeResponse response = snsClient.subscribe(request);

        log.info("Inscrição de email criada: {}", email);

        return "Inscrição criada. ARN: " + response.subscriptionArn() +
                " (verifique seu email e clique em 'Confirm subscription')";
    }

    public String subscribeHttp(String endpointUrl) {

        String protocol = endpointUrl.startsWith("https") ? "https" : "http";

        SubscribeRequest request = SubscribeRequest.builder()
                .topicArn(TOPIC_ARN)
                .protocol(protocol)
                .endpoint(endpointUrl)
                .build();

        SubscribeResponse response = snsClient.subscribe(request);

        log.info("Inscrição HTTP criada para endpoint: {}", endpointUrl);

        return "Inscrição criada. ARN: " + response.subscriptionArn();
    }
}
