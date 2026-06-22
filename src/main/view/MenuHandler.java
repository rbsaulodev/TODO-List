package main.view;

import main.controller.CategoryController;
import main.controller.TaskController;
import main.model.Task;
import main.model.enums.StatusTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private MenuHandler() { /* utility class */ }

    // ── Menu de Tarefas ───────────────────────────────────────────────────────

    public static void exibirMenuTarefas(Scanner sc, TaskController taskCtrl,
                                         CategoryController catCtrl) {
        int opcao;
        do {
            System.out.println("""
                \n╔══════════════════════════════╗
                ║       MENU — TAREFAS         ║
                ╠══════════════════════════════╣
                ║ 1. Criar tarefa              ║
                ║ 2. Atualizar tarefa          ║
                ║ 3. Remover tarefa            ║
                ║ 4. Listar todas              ║
                ║ 5. Listar por status         ║
                ║ 6. Verificar alarmes         ║
                ║ 0. Voltar                    ║
                ╚══════════════════════════════╝
                Opção:\s""");
            opcao = Integer.parseInt(sc.nextLine().trim());
            switch (opcao) {
                case 1 -> criarTarefa(sc, taskCtrl);
                case 2 -> atualizarTarefa(sc, taskCtrl);
                case 3 -> { System.out.print("ID da tarefa: "); taskCtrl.remove(Integer.parseInt(sc.nextLine())); }
                case 4 -> listarTarefas(taskCtrl.listAll());
                case 5 -> listarPorStatus(sc, taskCtrl);
                case 6 -> taskCtrl.checkAlarms();
            }
        } while (opcao != 0);
    }

    private static void criarTarefa(Scanner sc, TaskController taskCtrl) {
        System.out.print("Nome: ");          String name        = sc.nextLine();
        System.out.print("Descrição: ");     String desc        = sc.nextLine();
        System.out.print("Prazo (dd/MM/yyyy HH:mm): ");
        LocalDateTime date = LocalDateTime.parse(sc.nextLine().trim(), DTF);
        System.out.print("Prioridade (1-MuitoBaixa ... 5-Urgente): ");
        int priority = Integer.parseInt(sc.nextLine());
        System.out.print("ID da categoria (0 = nenhuma): ");
        int catId = Integer.parseInt(sc.nextLine());
        System.out.print("Alarme? (s/n): ");
        boolean alarm = sc.nextLine().trim().equalsIgnoreCase("s");
        int hours = 0;
        if (alarm) { System.out.print("Horas antes: "); hours = Integer.parseInt(sc.nextLine()); }
        taskCtrl.create(name, desc, date, priority, catId == 0 ? null : catId, alarm, hours);
    }

    private static void atualizarTarefa(Scanner sc, TaskController taskCtrl) {
        System.out.print("ID da tarefa: ");
        Integer id = Integer.parseInt(sc.nextLine());
        System.out.print("Novo nome (Enter = manter): ");     String name = sc.nextLine();
        System.out.print("Nova descrição (Enter = manter): "); String desc = sc.nextLine();
        System.out.print("Novo status (1-Todo 2-Doing 3-Done, 0=manter): ");
        String statusInput = sc.nextLine().trim();
        Integer statusCode = statusInput.equals("0") || statusInput.isBlank() ? null : Integer.parseInt(statusInput);
        taskCtrl.update(id,
            name.isBlank() ? null : name,
            desc.isBlank() ? null : desc,
            null, null, statusCode, null, null);
    }

    private static void listarTarefas(List<Task> tasks) {
        if (tasks.isEmpty()) { System.out.println("Nenhuma tarefa encontrada."); return; }
        tasks.forEach(System.out::println);
    }

    private static void listarPorStatus(Scanner sc, TaskController taskCtrl) {
        System.out.print("Status (1-Todo 2-Doing 3-Done): ");
        StatusTask status = StatusTask.fromCode(Integer.parseInt(sc.nextLine()));
        listarTarefas(taskCtrl.listByStatus(status));
    }

    // ── Menu de Categorias ────────────────────────────────────────────────────

    public static void exibirMenuCategorias(Scanner sc, CategoryController catCtrl) {
        int opcao;
        do {
            System.out.println("""
                \n╔══════════════════════════════╗
                ║      MENU — CATEGORIAS       ║
                ╠══════════════════════════════╣
                ║ 1. Criar categoria           ║
                ║ 2. Atualizar categoria       ║
                ║ 3. Remover categoria         ║
                ║ 4. Listar categorias         ║
                ║ 0. Voltar                    ║
                ╚══════════════════════════════╝
                Opção:\s""");
            opcao = Integer.parseInt(sc.nextLine().trim());
            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome: ");       String name = sc.nextLine();
                    System.out.print("Descrição: ");  String desc = sc.nextLine();
                    catCtrl.create(name, desc);
                }
                case 2 -> {
                    System.out.print("ID: ");        Integer id   = Integer.parseInt(sc.nextLine());
                    System.out.print("Novo nome: "); String name  = sc.nextLine();
                    System.out.print("Nova desc: "); String desc  = sc.nextLine();
                    catCtrl.update(id, name, desc);
                }
                case 3 -> { System.out.print("ID: "); catCtrl.remove(Integer.parseInt(sc.nextLine())); }
                case 4 -> catCtrl.listAll().forEach(System.out::println);
            }
        } while (opcao != 0);
    }
}
