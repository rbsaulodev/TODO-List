package main;

import main.controller.CategoryController;
import main.controller.TaskController;
import main.observer.AlarmObserver;
import main.repository.CategoryRepository;
import main.repository.TaskRepository;
import main.service.CategoryService;
import main.service.TodoService;
import main.view.MenuHandler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CategoryRepository catRepo = new CategoryRepository();
        TaskRepository taskRepo = new TaskRepository(catRepo);

        CategoryService catService  = new CategoryService(catRepo);
        TodoService todoService = new TodoService(taskRepo);

        todoService.addObserver(new AlarmObserver());

        CategoryController catCtrl = new CategoryController(catService);
        TaskController taskCtrl = new TaskController(todoService, catService);

        catService.load();
        todoService.load(catService);

        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("""
                \n╔══════════════════════════════╗
                ║      TODO LIST — ACZG        ║
                ╠══════════════════════════════╣
                ║ 1. Gerenciar Tarefas         ║
                ║ 2. Gerenciar Categorias      ║
                ║ 0. Sair                      ║
                ╚══════════════════════════════╝
                Opção:\s""");
            opcao = Integer.parseInt(sc.nextLine().trim());
            switch (opcao) {
                case 1 -> MenuHandler.exibirMenuTarefas(sc, taskCtrl, catCtrl);
                case 2 -> MenuHandler.exibirMenuCategorias(sc, catCtrl);
            }
        } while (opcao != 0);

        System.out.println("Até logo! 👋");
        sc.close();
    }
}
