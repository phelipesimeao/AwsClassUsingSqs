package org.aulaawspleno.sqs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EnderecoDto {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("regiao")
    private String regiao;

    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;

    private Boolean erro;
}
