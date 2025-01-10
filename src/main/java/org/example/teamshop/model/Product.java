package org.example.teamshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String name;
   private BigDecimal price;

   @ManyToOne
   @JoinColumn(name = "category_id")
   private Category category;

   @OneToMany(mappedBy = "product",
           orphanRemoval = true,
           cascade = CascadeType.ALL)
   @JsonBackReference
   private Set<Image> images;
}
