package kz.ozon.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.ozon.enums.ClothingSize;
import kz.ozon.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clothes")
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Clothes extends Product {
    private String color;
    private ClothingSize size;
    private String material;
    private Gender gender;
}