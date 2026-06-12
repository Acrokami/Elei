package com.acrobtw.elei.domain.economy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "shop_items")
@NoArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "stock", nullable = false)
    private Integer stock;
}
