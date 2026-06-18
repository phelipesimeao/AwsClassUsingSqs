package org.aulaawspleno.sqs.service;

import org.aulaawspleno.sqs.dto.EnderecoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ViaCepService {
    private final RestClient restClient;

    public ViaCepService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }

    public EnderecoDto buscarCep(String cep) {
        EnderecoDto endereco = restClient
                .get()
                .uri("/{cep}/json", cep)
                .retrieve()
                .body(EnderecoDto.class);

        if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
            throw new RuntimeException("CEP não encontrado");
        }

        return endereco;
    }
}
