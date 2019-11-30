package com.grafo.gerenciador;

import com.grafo.carregarDados.LerEntradas;
import com.grafo.entregas.calculoProfundidade.Entregas;
import com.grafo.entregas.calculoProfundidade.Rota;
import com.grafo.models.Entradas;
import com.grafo.models.RotasEntrega;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GerenciadorSimples {

    private static Entradas entradas = new Entradas();
    private static List<RotasEntrega> rotas = new ArrayList<>();
    private static int cores = Runtime.getRuntime().availableProcessors();
    private static Date start;
    private static Date finish;

    private static String diretorio = null;

    /**
     * Construtor da classe que executa o menu
     */

    public GerenciadorSimples() throws Exception {

        Scanner ler = new Scanner(System.in);
        int opcaoMenu;
        while (true) {

            try {
                System.out.println("\n=================== =================== LEILÃO DE ENTREGAS =================== ===================");
                System.out.println("1 - Carregar Entradas ");
                System.out.println("2 - Calcular Entregas Busca Profunda");
                System.out.println("6 - Mostrar Rotas ");
                System.out.println("7 - Limpar tela ");
                System.out.println("0 - Sair ");
                opcaoMenu = ler.nextInt();

                switch (opcaoMenu) {
                    case 1:
                        menuArquivos();
                        break;
                    case 2:
                        calcularRota();
                        break;
                    case 3:
                        mostrarRota();
                        break;
                    case 4:
                        limparTela();
                        break;
                    case 0:
                        System.out.println("Saindo ...");
                        System.exit(0);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ops! Ocorreu algo errado, tente novamente.");
            }
        }
    }

    private void menuArquivos() {
        Scanner ler = new Scanner(System.in);
        int opcaoMenuArquivos;
        System.out.println("\n=================== =================== ESCOLHA UMA OPÇÃO =================== ===================");
        System.out.println("1 - Carregar Entradas Enunciado ");
        System.out.println("2 - Carregar Entradas 2");
        System.out.println("3 - Carregar Bug Parametro ");
        System.out.println("4 - Carregar Bug Aleatorio ");
        System.out.println("5 - Carregar Bug Complexa ");
        System.out.println("0 - Voltar ");
        opcaoMenuArquivos = ler.nextInt();

        switch (opcaoMenuArquivos) {
            case 1:
                diretorio = "src\\files\\entradas.txt";
                ReadFile();
                break;

            case 2:
                diretorio = "src\\files\\entradas2.txt";
                ReadFile();
                break;

            case 3:
                diretorio = "src\\files\\bug_parametro.txt";
                ReadFile();
                break;

            case 4:
                diretorio = "src\\files\\bug_aleatorio.txt";
                ReadFile();
                break;

            case 5:
                diretorio = "src\\files\\bug_complexa.txt";
                ReadFile();
                break;

            case 0:
                return;
        }
    }

    /**
     * Mostra as rotas alternativas e a rota principal
     */

    private void mostrarRota() {
        int cont = 1;
        int contMelhorRota = 1;
        int recompensa = 0;
        int distanciaTotal = 0;
        List<Rota> calculaMR;

        System.out.println("\n=================== =================== ========== #Entregas do dia# ========== =================== ===================");
        for (RotasEntrega re : rotas) {
            Rota r = re.getRotaMenor();

            System.out
                    .print("\n\n=================== =================== A " + cont + "º possível rota a ser realizada é de 'A' até '" + r.getDestino() + "' =================== ===================");


            boolean isTrue = false;
            if (r.getRecompensa() == 1) {
                isTrue = true;
            }

            System.out.println("\n\nA possivel rota é: " + printRoute(r));
            System.out.println(
                    "Com a chegada estimada de " + r.getDistancia() + " unidades de tempo no destino " + "'"
                            + r.getDestino() + "'" + " e o valor para esta entrega será de " + (isTrue ?
                            r.getRecompensa() + " real" : r.getRecompensa() + " reais") + ".");


            distanciaTotal += r.getDistancia();
            cont++;
        }

        calculaMR = calculaMelhorEntraga(distanciaTotal);
        System.out.println("\n#############################################################################################################################");

        for(Rota reS : calculaMR)
        {
            System.out
                    .print("\n\n=================== =================== A " + contMelhorRota + "º rota a ser realizada é de 'A' até '" + reS.getDestino() + "' =================== ===================");


            boolean isTrue = false;
            if (reS.getRecompensa() == 1) {
                isTrue = true;
            }

            System.out.println("\n\nA melhor rota é: " + printRoute(reS));
            System.out.println(
                    "Com a chegada estimada de " + reS.getDistancia() + " unidades de tempo no destino " + "'"
                            + reS.getDestino() + "'" + " e o valor para esta entrega será de " + (isTrue ?
                            reS.getRecompensa() + " real" : reS.getRecompensa() + " reais") + ".");

            recompensa += reS.getRecompensa();
            contMelhorRota ++;
        }

        System.out.println("\n\nO lucro total do dia: " + recompensa + ".");
    }


    private List<Rota> calculaMelhorEntraga(int distanciaTotal)
    {
        int idaEvolta = 0;
        int somaIdaEVolta = 0;

        List<Rota> calculaMelhorEntrega = new ArrayList<>();
        List<Rota> listaMelhorEntrega = new ArrayList<>();

        for(RotasEntrega re : rotas)
        {
            Rota r = re.getRotaMenor();

            if(r != null)
            {
                idaEvolta = r.getDistancia() * 2;

                Rota rotaIdaEVolta = new Rota();
                rotaIdaEVolta.setPontos(r.getPontos());
                rotaIdaEVolta.setDestino(r.getDestino());
                rotaIdaEVolta.setDistancia(idaEvolta);
                rotaIdaEVolta.setRecompensa(r.getRecompensa());
                listaMelhorEntrega.add(rotaIdaEVolta);
            }
        }

        for(Rota r : listaMelhorEntrega)
        {
            somaIdaEVolta += r.getDistancia();

            if(somaIdaEVolta <= distanciaTotal)
            {
                Rota re = new Rota();
                re.setPontos(r.getPontos());
                re.setRecompensa(r.getRecompensa());
                re.setDistancia(r.getDistancia()/2);
                re.setDestino(r.getDestino());
                calculaMelhorEntrega.add(re);
            }
            somaIdaEVolta -= somaIdaEVolta;
        }

        return calculaMelhorEntrega;
    }

    /**
     * Monta a tela da principal rota
     */

    private String printRoute(Rota r) {
        StringBuilder s = new StringBuilder();

        for (String d : r.getPontos()) {
            s.append(d + "->");
        }
        s = s.replace(s.length() - 2, s.length(), ".");

        return s.toString();
    }

    private static void limparTela() throws IOException {
        for (int i = 0; i < 100; ++i)
            System.out.println();
    }

    /**
     * Faz a leitura do arquivo, caso não seja possivel ler lança exception ao user
     */

    private static void ReadFile() {
        try {
            LerEntradas read = new LerEntradas();
            entradas = read.lerArquivoTxt(diretorio);
        } catch (Exception e) {
            System.out.println("Formato de arquivo inválido!");
        }
    }

    /**
     * Faz o calculo das entregas retornando as rotas
     */
    private static void calcularRota() throws CloneNotSupportedException {
        Entregas matriz = new Entregas(entradas);
        rotas = matriz.processarEntregas();
    }
}


