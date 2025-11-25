package com.team18.backend.repository;

import com.team18.backend.UnitTestContainersConfiguration;
import com.team18.backend.model.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(UnitTestContainersConfiguration.class)
class ShopRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;

    @Test
    void saveShop_shouldPersistShopAndGenerateId_whenValidShopProvided() {
        // GIVEN
        Shop shop = new Shop("Test Shop");

        // WHEN
        Shop saved = shopRepository.save(shop);

        // THEN
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Shop");
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getLastModifiedDate()).isNotNull()
                .isEqualTo(saved.getCreatedDate());
    }

    @Test
    void findById_shouldReturnShop_whenShopExists() {
        // GIVEN
        Shop shop = shopRepository.save(new Shop("Find Me"));

        // WHEN
        Optional<Shop> found = shopRepository.findById(shop.getId());

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Find Me");
        assertThat(found.get().getId()).isEqualTo(shop.getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenShopDoesNotExist() {
        // WHEN
        Optional<Shop> found = shopRepository.findById("non-existent-id");

        // THEN
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllShops_whenMultipleShopsExist() {
        // GIVEN
        shopRepository.save(new Shop("Shop 1"));
        shopRepository.save(new Shop("Shop 2"));

        // WHEN
        List<Shop> shops = shopRepository.findAll();

        // THEN
        assertThat(shops).hasSize(2);
        assertThat(shops).extracting("name")
                .containsExactlyInAnyOrder("Shop 1", "Shop 2");
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoShopsExist() {
        // WHEN
        List<Shop> shops = shopRepository.findAll();

        // THEN
        assertThat(shops).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveShop_whenShopExists() {
        // GIVEN
        Shop shop = shopRepository.save(new Shop("To Delete"));
        String shopId = shop.getId();

        // WHEN
        shopRepository.deleteById(shopId);

        // THEN
        Optional<Shop> found = shopRepository.findById(shopId);
        assertThat(found).isEmpty();
    }
}

