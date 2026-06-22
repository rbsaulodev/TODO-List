package test;


import main.model.CategoryTask;
import main.repository.CategoryRepository;
import main.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CategoryService — Testes unitários")
class CategoryServiceTest {

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(new CategoryRepository());
    }

    @Test
    @DisplayName("Criar categoria adiciona à lista")
    void deveCriarCategoria() {
        categoryService.createCategory("Backend", "APIs e serviços");
        List<CategoryTask> list = categoryService.listAll();
        assertEquals(1, list.size());
        assertEquals("Backend", list.get(0).getName());
    }

    @Test
    @DisplayName("Atualizar categoria modifica nome e descrição")
    void deveAtualizarCategoria() {
        categoryService.createCategory("Antigo", "desc");
        Integer id = categoryService.listAll().get(0).getId();

        categoryService.updateCategory(id, "Novo", "nova desc");

        CategoryTask cat = categoryService.findById(id);
        assertEquals("Novo",     cat.getName());
        assertEquals("nova desc", cat.getDescription());
    }

    @Test
    @DisplayName("Remover categoria existente funciona")
    void deveRemoverCategoria() {
        categoryService.createCategory("Temp", "desc");
        Integer id = categoryService.listAll().get(0).getId();

        categoryService.removeCategory(id);

        assertTrue(categoryService.listAll().isEmpty());
    }

    @Test
    @DisplayName("Buscar ID inexistente lança exceção")
    void deveLancarExcecaoIdInexistente() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findById(999));
    }

    @Test
    @DisplayName("Buscar por nome retorna categoria correta")
    void deveBuscarPorNome() {
        categoryService.createCategory("Frontend", "Angular");
        assertTrue(categoryService.findByName("Frontend").isPresent());
        assertTrue(categoryService.findByName("Inexistente").isEmpty());
    }
}
