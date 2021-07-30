package one.digitalinnovation.beerstock.entity;

import lombok.Data;
import one.digitalinnovation.beerstock.enums.BeerType;

import javax.persistence.*;

@Data
@Entity
public class BeerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeerType type;
}
