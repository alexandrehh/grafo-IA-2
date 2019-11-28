package com.grafo.leiaoEntregas.entregas.calculoIA;

import com.grafo.leiaoEntregas.entregas.CalculaEntregasUtils;
import com.grafo.leiaoEntregas.entregas.calculoProfundidade.Rota;
import com.grafo.leiaoEntregas.models.Aresta;
import com.grafo.leiaoEntregas.models.Entradas;
import com.grafo.leiaoEntregas.models.Vertice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalculaRotaEntregaIA {
    private List<RotaIA> rotas = new ArrayList<>();
    private Set<Vertice> verticesVisitados = new HashSet<>();
    CalculaEntregasUtils calcUtils;

    public CalculaRotaEntregaIA(Entradas entradas) {
        calcUtils = new CalculaEntregasUtils(entradas);
    }

    public RotaIA calculaRota(RotaIA rota) {

        rotas.add(rota);
        RotaIA menorRota = calculaVertices();

        return menorRota;
    }

    private RotaIA calculaVertices() {

        RotaIA rota = getMenorRotaAtual();
        if (rota == null)
            return null;
        RotaIA rotaCalculada = calculaArestas(rota);

        if (rotaCalculada == null)
            return calculaVertices();

        else
            return rotaCalculada;
    }

    private RotaIA calculaArestas(RotaIA rota) {
        if (interromperRota(rota)) {
            rotas.remove(rota);
            return null;
        }
        Vertice verticeAtual = rota.getVerticeAtual();
        verticesVisitados.add(verticeAtual);

        List<Aresta> arestasVertice = verticeAtual.getArestas();
        for (Aresta a : arestasVertice) {
            if (a.getComprimento() == 0)
                continue;
            if (rota.getDestino().equals(a.getVerticeDestino()))
                return calcUtils.criaNovaRota(rota, a);
            rotas.add(calcUtils.criaNovaRota(rota, a));
        }
        rotas.remove(rota);
        return null;
    }

    private boolean interromperRota(RotaIA rota) {
        try {
            if (rota == null)
                return true;
            return verticesVisitados.stream().anyMatch(x -> x.getNome().equals(rota.getVerticeAtual().getNome()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private RotaIA getMenorRotaAtual() {
        rotas.sort((x, y) -> Integer.compare(x.getDistancia(), y.getDistancia()));
        if (rotas.isEmpty())
            return null;
        return rotas.get(0);
    }
}