package org.aulaawspleno.sqs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {
    @Id
    @GeneratedValue
    private Long id;

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

    @OneToOne(mappedBy = "endereco")
    private User user;
}
