public class ArvoreDoisTres {

    class No {
        int[] chaves;
        No[] filhos;
        int quantidade;

        No() {
            this.chaves = new int[3];
            this.filhos = new No[4];
            this.quantidade = 0;
        }
    }

    No raiz = null;

    public boolean buscar(int chave) {
        No atual = raiz;
        while (atual != null) {
            int i = 0;
            while (i < atual.quantidade && chave > atual.chaves[i]) {
                i++;
            }
            if (i < atual.quantidade && atual.chaves[i] == chave) {
                return true;
            }
            atual = atual.filhos[i];
        }
        return false;
    }

    public void inserir(int chave) {
        if (raiz == null) {
            raiz = new No();
        }
        if (buscar(chave)) {
            System.out.println(chave + " já existe");
            return;
        }
        inserirNo(raiz, chave);
        if (raiz.quantidade == 3) {
            No novaRaiz = new No();
            novaRaiz.filhos[0] = raiz;
            dividir(novaRaiz, 0);
            raiz = novaRaiz;
        }
    }

    void inserirNo(No no, int chave) {
        if (no.filhos[0] == null) {
            int i = no.quantidade - 1;
            while (i >= 0 && no.chaves[i] > chave) {
                no.chaves[i + 1] = no.chaves[i];
                i--;
            }
            no.chaves[i + 1] = chave;
            no.quantidade++;
            return;
        }
        int i = 0;
        while (i < no.quantidade && chave > no.chaves[i]) {
            i++;
        }
        inserirNo(no.filhos[i], chave);
        if (no.filhos[i].quantidade == 3) {
            dividir(no, i);
        }
    }

    void dividir(No pai, int i) {
        No cheio = pai.filhos[i];
        No esquerdo = new No();
        No direito = new No();
        int meio = cheio.chaves[1];

        esquerdo.chaves[0] = cheio.chaves[0];
        esquerdo.quantidade = 1;
        esquerdo.filhos[0] = cheio.filhos[0];
        esquerdo.filhos[1] = cheio.filhos[1];

        direito.chaves[0] = cheio.chaves[2];
        direito.quantidade = 1;
        direito.filhos[0] = cheio.filhos[2];
        direito.filhos[1] = cheio.filhos[3];

        int j = pai.quantidade - 1;
        while (j >= i) {
            pai.chaves[j + 1] = pai.chaves[j];
            j--;
        }
        j = pai.quantidade;
        while (j > i) {
            pai.filhos[j + 1] = pai.filhos[j];
            j--;
        }
        pai.chaves[i] = meio;
        pai.filhos[i] = esquerdo;
        pai.filhos[i + 1] = direito;
        pai.quantidade++;
    }

    public void remover(int chave) {
        if (!buscar(chave)) {
            System.out.println(chave + " não existe");
            return;
        }
        removerNo(raiz, chave);
        if (raiz.quantidade == 0) {
            raiz = raiz.filhos[0];
        }
    }

    void removerNo(No no, int chave) {
        int i = 0;
        while (i < no.quantidade && chave > no.chaves[i]) {
            i++;
        }
        if (i < no.quantidade && no.chaves[i] == chave) {
            if (no.filhos[0] == null) {
                for (int j = i; j < no.quantidade - 1; j++) {
                    no.chaves[j] = no.chaves[j + 1];
                }
                no.quantidade--;
                return;
            }
            No aux = no.filhos[i];
            while (aux.filhos[0] != null) {
                aux = aux.filhos[aux.quantidade];
            }
            int predecessor = aux.chaves[aux.quantidade - 1];
            no.chaves[i] = predecessor;
            removerNo(no.filhos[i], predecessor);
        } else {
            removerNo(no.filhos[i], chave);
        }
        if (no.filhos[i].quantidade == 0) {
            corrigir(no, i);
        }
    }

    void corrigir(No pai, int i) {
        No vazio = pai.filhos[i];
        if (i > 0 && pai.filhos[i - 1].quantidade == 2) {
            No irmao = pai.filhos[i - 1];
            vazio.chaves[0] = pai.chaves[i - 1];
            vazio.filhos[1] = vazio.filhos[0];
            vazio.filhos[0] = irmao.filhos[2];
            vazio.quantidade = 1;
            pai.chaves[i - 1] = irmao.chaves[1];
            irmao.filhos[2] = null;
            irmao.quantidade = 1;
            return;
        }
        if (i < pai.quantidade && pai.filhos[i + 1].quantidade == 2) {
            No irmao = pai.filhos[i + 1];
            vazio.chaves[0] = pai.chaves[i];
            vazio.filhos[1] = irmao.filhos[0];
            vazio.quantidade = 1;
            pai.chaves[i] = irmao.chaves[0];
            irmao.chaves[0] = irmao.chaves[1];
            irmao.filhos[0] = irmao.filhos[1];
            irmao.filhos[1] = irmao.filhos[2];
            irmao.filhos[2] = null;
            irmao.quantidade = 1;
            return;
        }
        int k = i;
        if (i > 0) {
            k = i - 1;
        }
        No esq = pai.filhos[k];
        No dir = pai.filhos[k + 1];
        int q = esq.quantidade;
        esq.chaves[q] = pai.chaves[k];
        for (int j = 0; j < dir.quantidade; j++) {
            esq.chaves[q + 1 + j] = dir.chaves[j];
        }
        for (int j = 0; j <= dir.quantidade; j++) {
            esq.filhos[q + 1 + j] = dir.filhos[j];
        }
        esq.quantidade = q + 1 + dir.quantidade;
        for (int j = k; j < pai.quantidade - 1; j++) {
            pai.chaves[j] = pai.chaves[j + 1];
        }
        for (int j = k + 1; j < pai.quantidade; j++) {
            pai.filhos[j] = pai.filhos[j + 1];
        }
        pai.filhos[pai.quantidade] = null;
        pai.quantidade--;
    }

    void saida(No no, String linha, String prefixo) {
        System.out.print(linha + "[");
        for (int i = 0; i < no.quantidade; i++) {
            if (i > 0) {
                System.out.print(",");
            }
            System.out.print(no.chaves[i]);
        }
        System.out.println("]");
        if (no.filhos[0] == null) {
            return;
        }
        for (int i = 0; i <= no.quantidade; i++) {
            System.out.println(prefixo + "|");
            if (i < no.quantidade) {
                saida(no.filhos[i], prefixo + "|----> ", prefixo + "|      ");
            } else {
                saida(no.filhos[i], prefixo + "|----> ", prefixo + "       ");
            }
        }
    }

    public void exibir() {
        if (raiz == null) {
            System.out.println("Árvore vazia");
        } else {
            saida(raiz, "", "");
        }
        System.out.println();
    }

    void intervalo(No no, int min, int max) {
        if (no == null) {
            return;
        }
        for (int i = 0; i < no.quantidade; i++) {
            if (min < no.chaves[i] && (i == 0 || max > no.chaves[i - 1])) {
                intervalo(no.filhos[i], min, max);
            }
            if (no.chaves[i] >= min && no.chaves[i] <= max) {
                System.out.print(no.chaves[i] + " ");
            }
        }
        if (max > no.chaves[no.quantidade - 1]) {
            intervalo(no.filhos[no.quantidade], min, max);
        }
    }

    public static void main(String[] args) {
        ArvoreDoisTres arvore = new ArvoreDoisTres();

        System.out.println("Árvore 2-3\nBaseada em Goodrich, Tamassia e Goldwasser, Data Structures and Algorithms in Java, 6a ed., seção 11.5 (árvore (2,4) adaptada para (2,3))\n");

        int valores[] = {10, 20, 30, 40, 50, 60, 70, 15, 35, 65, 80, 90, 30};
        for (int i = 0; i < valores.length; i++) {
            System.out.println("Inserindo " + valores[i]);
            arvore.inserir(valores[i]);
            arvore.exibir();
        }

        System.out.println("Busca 35: " + arvore.buscar(35));
        System.out.println("Busca 55: " + arvore.buscar(55));
        System.out.println();

        int removidos[] = {15, 40, 65, 70, 10, 99, 90};
        for (int i = 0; i < removidos.length; i++) {
            System.out.println("Removendo " + removidos[i]);
            arvore.remover(removidos[i]);
            arvore.exibir();
        }

        System.out.println("Aplicação: índice de códigos de produtos de um estoque\n");
        ArvoreDoisTres estoque = new ArvoreDoisTres();
        int codigos[] = {1050, 2030, 1010, 3070, 2500, 1500, 4100, 3300, 1200, 2800};
        for (int i = 0; i < codigos.length; i++) {
            System.out.println("Inserindo " + codigos[i]);
            estoque.inserir(codigos[i]);
            estoque.exibir();
        }

        System.out.println("Busca 2800: " + estoque.buscar(2800));
        System.out.println("Busca 2900: " + estoque.buscar(2900));
        System.out.println();

        System.out.println("Removendo 1050");
        estoque.remover(1050);
        estoque.exibir();

        System.out.print("Códigos entre 1200 e 3070: ");
        estoque.intervalo(estoque.raiz, 1200, 3070);
        System.out.println();
    }
}
