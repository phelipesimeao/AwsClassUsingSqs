package Sns.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Product {
    Long id;
    String produto;
    String valor;
}
