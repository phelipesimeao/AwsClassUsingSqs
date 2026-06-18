package org.aulaawspleno.sqs.service;

import org.aulaawspleno.rds.repository.CepRepository;
import org.aulaawspleno.sqs.dto.EnderecoDto;
import org.aulaawspleno.sqs.model.Endereco;
import org.aulaawspleno.sqs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SqsService {

    private final SqsClient sqsClient;

    private static final String QUEUE_URL =
            "https://sqs.us-east-1.amazonaws.com/105927215094/academia-aws-java-07";

    private final CepRepository repository;

    private final ViaCepService viaCepService;

    private ObjectMapper objectMapper;

    public SqsService(SqsClient sqsClient, CepRepository repository, ViaCepService viaCepService, ObjectMapper objectMapper ) {
        this.repository = repository;
        this.sqsClient = sqsClient;
        this.viaCepService = viaCepService;
        this.objectMapper = objectMapper;
    }

    public String sendUser(User user) {

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .messageBody(objectMapper.writeValueAsString(user))
                .build();

        SendMessageResponse response = sqsClient.sendMessage(request);

        return "Mensagem enviada com sucesso. ID: " + response.messageId();
    }

    public List<Map<String, String>> receiveAndProcess() {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(2)
                .visibilityTimeout(30) // 30s para processar antes de a msg reaparecer
                .build();

        List<Message> messages = sqsClient.receiveMessage(request).messages();

        List<Map<String, String>> processed = new ArrayList<>();

        for (Message message : messages) {
            try {

                var user = processMessage(message);

                var endereco = getUserAdress(user.getCep());
                user.setEndereco(endereco);
                var savedUser = saveUser(user);

                deleteMessage(message.receiptHandle());


                processed.add(Map.of(
                        "messageId", message.messageId(),
                        "body", message.body(),
                        "status", "PROCESSADA E REMOVIDA: usuario salvo: "+ savedUser.getId(),
                        "Usuario salvo: ", objectMapper.writeValueAsString(savedUser)
                ));

            } catch (Exception e) {
                processed.add(Map.of(
                        "messageId", message.messageId(),
                        "body", message.body(),
                        "status", "FALHA - SERÁ REPROCESSADA",
                        "erro", e.getMessage()
                ));
            }
        }

        return processed;
    }

    private Endereco getUserAdress(String cep) {
        return objectMapper.convertValue(viaCepService.buscarCep(cep), Endereco.class);
    }

    private User saveUser(User user) {
        return repository.save(user);
    }

    public List<String> peekMessages() {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(2)
                .build();

        return sqsClient.receiveMessage(request).messages()
                .stream()
                .map(Message::body)
                .toList();
    }

    private User processMessage(Message message) {
        System.out.println("Processando mensagem: " + message.body());
        ObjectMapper objectMapper = new ObjectMapper();

        User userRequest = objectMapper.readValue(message.body(), User.class);

        return userRequest;
    }

    private void deleteMessage(String receiptHandle) {
        DeleteMessageRequest request = DeleteMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(request);
    }
}