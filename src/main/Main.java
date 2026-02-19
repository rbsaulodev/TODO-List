package main;

import service.CategoryManager;
import service.TodoManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TodoManager taskManager = new TodoManager();
        CategoryManager categoryManager = new CategoryManager();

        System.out.println("Carregando dados salvos...");
        System.out.println(" ");
        categoryManager.loadCategoriesCSV();
        taskManager.loadTasksCSV(categoryManager);

        int opcao = -1;

        System.out.println("======= ZG-HERO TODO LIST =======");
        System.out.println("Bem-vindo, Brilhante!");

        while (opcao != 0) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gerenciar Tarefas");
            System.out.println("2. Gerenciar Categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        MenuHandler.exibirMenuTarefas(scanner, taskManager, categoryManager);
                        break;
                    case 2:
                        MenuHandler.exibirMenuCategorias(scanner, categoryManager);
                        break;
                    case 0:
                        System.out.println("Saindo... Até logo!");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite apenas números.");
            }
        }
        scanner.close();
    }
}