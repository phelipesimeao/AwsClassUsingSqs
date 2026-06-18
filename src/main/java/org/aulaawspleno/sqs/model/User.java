package org.aulaawspleno.sqs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String nome;

    private String cep;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", unique = true) // Chave estrangeira
    private Endereco endereco;

    @Override
    public String toString(){
        return "id: "+ id + ", nome: " + nome + ", cep: " + cep;
    }
}
