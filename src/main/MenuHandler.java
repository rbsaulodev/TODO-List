package main;

import model.enums.Priority;
import model.enums.StatusTask;
import service.CategoryManager;
import service.TodoManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuHandler {
    public static void exibirMenuTarefas(Scanner scanner, TodoManager taskManager, CategoryManager categoryManager) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\n--- GERENCIAR TAREFAS ---");
        System.out.println("1. Criar Nova Tarefa");
        System.out.println("2. Listar Todas");
        System.out.println("3. Atualizar Tarefa (Status/Alarme/Dados)");
        System.out.println("4. Remover Tarefa");
        System.out.println("5. Verificar Alarmes Ativos");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1:
                    System.out.println("\n--- CRIAR NOVA TAREFA ---");
                    System.out.print("Nome: ");
                    String name = scanner.nextLine();

                    System.out.print("Descrição: ");
                    String description = scanner.nextLine();

                    System.out.print("Data e Hora de término (dd/MM/yyyy HH:mm): ");
                    String dataInput = scanner.nextLine();
                    LocalDateTime dateToEnd = LocalDateTime.parse(dataInput, formatter);

                    System.out.print("Prioridade (1 a 5): ");
                    int code = Integer.parseInt(scanner.nextLine());
                    Priority priority = Priority.fromCode(code);

                    categoryManager.listAllCategories();
                    System.out.print("Digite o ID da categoria desejada: ");
                    int catId = Integer.parseInt(scanner.nextLine());
                    var category = categoryManager.findById(catId);

                    if (category == null) {
                        System.out.println("Erro: Categoria não encontrada!");
                        break;
                    }

                    System.out.print("Deseja habilitar alarme para esta tarefa? (s/n): ");
                    boolean alarmEnabled = scanner.nextLine().equalsIgnoreCase("s");
                    int hoursPrior = 0;
                    if (alarmEnabled) {
                        System.out.print("Quantas horas de antecedência para o aviso? ");
                        hoursPrior = Integer.parseInt(scanner.nextLine());
                    }

                    taskManager.createTask(name, description, dateToEnd, priority, category, alarmEnabled, hoursPrior);
                    break;

                case 2:
                    System.out.println("\n--- TODAS AS TAREFAS ---");
                    taskManager.listAllTasks();
                    break;

                case 3:
                    System.out.println("\n--- ATUALIZAR TAREFA ---");
                    taskManager.listAllTasks();
                    System.out.print("Digite o ID da tarefa que deseja alterar: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    var tarefaExistente = taskManager.findById(id);
                    if (tarefaExistente == null) {
                        System.out.println("❌ Erro: Tarefa não encontrada!");
                        break;
                    }

                    System.out.print("Novo Nome (Enter para manter): ");
                    String nameUp = scanner.nextLine();

                    System.out.print("Nova Data/Hora (dd/MM/yyyy HH:mm) (Enter para manter): ");
                    String dataUpRaw = scanner.nextLine();
                    LocalDateTime dateUp = dataUpRaw.isBlank() ? null : LocalDateTime.parse(dataUpRaw, formatter);

                    System.out.print("Novo Status (1-TODO, 2-DOING, 3-DONE) (Enter para manter): ");
                    String statusRaw = scanner.nextLine();
                    StatusTask statusUp = statusRaw.isBlank() ? null : StatusTask.fromCode(Integer.parseInt(statusRaw));

                    System.out.print("Habilitar Alarme? (s/n) (Enter para manter): ");
                    String alarmRaw = scanner.nextLine();
                    Boolean alarmUp = alarmRaw.isBlank() ? null : alarmRaw.equalsIgnoreCase("s");

                    Integer hoursUp = null;
                    if (alarmUp != null && alarmUp) {
                        System.out.print("Nova antecedência em horas: ");
                        hoursUp = Integer.parseInt(scanner.nextLine());
                    }

                    taskManager.updateTask(id, nameUp, null, dateUp, null, statusUp, alarmUp, hoursUp);
                    break;

                case 4:
                    System.out.println("\n--- REMOVER TAREFA ---");
                    taskManager.listAllTasks();
                    System.out.print("Digite o ID para remover: ");
                    int idRem = Integer.parseInt(scanner.nextLine());
                    taskManager.removeTask(idRem);
                    break;

                case 5:
                    taskManager.checkAlarms();
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números para opções, prioridades e IDs.");
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data/hora inválido. Use dd/MM/yyyy HH:mm (Ex: 25/12/2026 15:00)");
        }
    }

    public static void exibirMenuCategorias(Scanner scanner, CategoryManager categoryManager) {
        System.out.println("\n--- MENU DE CATEGORIAS ---");
        System.out.println("1. Criar Categoria");
        System.out.println("2. Listar Todas");
        System.out.println("3. Atualizar Categoria");
        System.out.println("4. Remover Categoria");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1:
                    System.out.println("\n--- 🆕 NOVA CATEGORIA ---");
                    System.out.print("Nome da Categoria: ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição: ");
                    String desc = scanner.nextLine();

                    categoryManager.createCategory(nome, desc);
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE CATEGORIAS ---");
                    categoryManager.listAllCategories();
                    break;

                case 3:
                    System.out.println("\n--- ATUALIZAR CATEGORIA ---");
                    categoryManager.listAllCategories();
                    System.out.print("ID da categoria que deseja editar: ");
                    int idEdit = Integer.parseInt(scanner.nextLine());

                    System.out.print("Novo Nome (Enter para manter): ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Nova Descrição (Enter para manter): ");
                    String novaDesc = scanner.nextLine();

                    categoryManager.updateCategory(idEdit, novoNome, novaDesc);
                    break;

                case 4:
                    System.out.println("\n--- REMOVER CATEGORIA ---");
                    categoryManager.listAllCategories();
                    System.out.print("ID da categoria para remover: ");
                    int idRem = Integer.parseInt(scanner.nextLine());

                    categoryManager.removeCategory(idRem);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números para a opção e ID.");
        }
    }
}
