package br.magno.acervo.aplicacao;

import br.magno.acervo.entidade.Livro;
import br.magno.acervo.repositorio.LivroRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp {
    private final LivroRepository livroRepository;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleApp(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n[Menu]");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Listar Livros");
            System.out.println("3 - Buscar por Autor");
            System.out.println("4 - Buscar por Ano");
            System.out.println("5 - Buscar por Título");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> listarLivros();
                case 3 -> buscarPorAutor();
                case 4 -> buscarPorAno();
                case 5 -> buscarPorTitulo();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarLivro() {
        System.out.println("\n[Cadastro de Livro]");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Autor: ");
        String autor = scanner.nextLine();

        System.out.print("Ano de Publicação: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        System.out.print("Editora: ");
        String editora = scanner.nextLine();

        if (livroRepository.existsByTituloAndAutor(titulo, autor)) {
            System.out.println("Erro: Livro já cadastrado!");
            return;
        }

        Livro livro = new Livro(titulo, autor, ano, editora);
        livroRepository.save(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private void listarLivros() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("\n[Listagem Completa do Acervo]");
        System.out.println("ID | Título | Autor | Ano | Editora");
        System.out.println("----------------------------------------");

        for (Livro livro : livros) {
            System.out.printf("%d | %s | %s | %d | %s%n",
                    livro.getId(), livro.getTitulo(), livro.getAutor(),
                    livro.getAnoPublicacao(), livro.getEditora());
        }
    }

    private void buscarPorAutor() {
        System.out.print("\nDigite o nome do autor: ");
        String autor = scanner.nextLine();

        List<Livro> livros = livroRepository.findByAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("Livros encontrados:");
        livros.forEach(l -> System.out.printf("- %s (%d, %s)%n",
                l.getTitulo(), l.getAnoPublicacao(), l.getEditora()));
    }

    private void buscarPorAno() {
        System.out.print("\nDigite o ano desejado: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        List<Livro> livros = livroRepository.findByAnoPublicacao(ano);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("Livros publicados em " + ano + ":");
        livros.forEach(l -> System.out.printf("- %s, por %s (%s)%n",
                l.getTitulo(), l.getAutor(), l.getEditora()));
    }

    private void buscarPorTitulo() {
        System.out.print("\nDigite um termo no título: ");
        String termo = scanner.nextLine();

        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(termo);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("Livros encontrados:");
        livros.forEach(l -> System.out.printf("- %s, por %s (%d)%n",
                l.getTitulo(), l.getAutor(), l.getAnoPublicacao()));
    }
}
