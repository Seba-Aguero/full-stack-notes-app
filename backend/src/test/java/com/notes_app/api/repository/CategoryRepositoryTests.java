package com.notes_app.api.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.notes_app.api.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
public class CategoryRepositoryTests {

  @Autowired
  private ICategoryRepository categoryRepository;

  @Test
  public void CategoryRepository_Save_ReturnSavedCategory() {

    Category category = Category.builder()
      .name("Test Category")
      .build();

    Category savedCategory = categoryRepository.save(category);

    assertThat(savedCategory).isNotNull();
    assertThat(savedCategory.getId()).isGreaterThan(0);
  }

  @Test
  public void CategoryRepository_FindAll_ReturnAllCategories() {

    Category category1 = Category.builder()
      .name("Test Category 1")
      .build();

    Category category2 = Category.builder()
      .name("Test Category 2")
      .build();

    categoryRepository.save(category1);
    categoryRepository.save(category2);

    List<Category> categories = categoryRepository.findAll();

    assertThat(categories).isNotEmpty();
    assertThat(categories).hasSize(2);
  }

  @Test
  public void CategoryRepository_FindById_ReturnCategory() {

    Category category = Category.builder()
      .name("Test Category")
      .build();

    categoryRepository.save(category);

    Category foundCategory = categoryRepository.findById(category.getId()).get();

    assertThat(foundCategory).isNotNull();
    assertThat(foundCategory.getId()).isEqualTo(category.getId());
  }

  @Test
  public void CategoryRepository_Update_ReturnUpdatedCategory() {

    Category category = Category.builder()
      .name("Test Category")
      .build();

    categoryRepository.save(category);

    Category updatedCategory = Category.builder()
      .id(category.getId())
      .name("Updated Category")
      .build();

    categoryRepository.save(updatedCategory);

    Category foundCategory = categoryRepository.findById(category.getId()).get();

    assertThat(foundCategory).isNotNull();
    assertThat(foundCategory.getId()).isEqualTo(category.getId());
    assertThat(foundCategory.getName()).isEqualTo("Updated Category");
  }

  @Test
  public void CategoryRepository_Delete_ReturnDeletedCategory() {

    Category category = Category.builder()
      .name("Test Category")
      .build();

    categoryRepository.save(category);

    categoryRepository.deleteById(category.getId());

    Category foundCategory = categoryRepository.findById(category.getId()).orElse(null);

    assertThat(foundCategory).isNull();
  }

}
