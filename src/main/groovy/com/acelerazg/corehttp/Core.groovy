package com.acelerazg.corehttp

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Core {

    static Document getPaginaTISS() {
        Document paginaInicialANS = HttpBuilder.configure { request.uri = 'https://www.gov.br/ans/pt-br' }.get()
        Element content = paginaInicialANS.getElementById("ce89116a-3e62-47ac-9325-ebec8ea95473")
        String link = content.getElementsByTag("a").attr("href")
        Document pagina2 = getPagina(link)
        content = pagina2.getElementsByClass("govbr-card-content").first()
        link = content.getElementsByTag("a").attr("href")
        return (Document) getPagina(link)
    }

    static Document getPagina(String url) {
        return (Document) HttpBuilder.configure { request.uri = url }.get()
    }

    static downloadComponents(String url, String componentName, String fileName){

        String pastaDownloads = "../downloads/${componentName}"

        Path caminho = Paths.get(pastaDownloads)

        if (!Files.exists(caminho)) {
            Files.createDirectories(caminho)
        }

        File arquivo = new File("../downloads/${componentName}/${fileName}")

        HttpBuilder.configure {
            request.uri = url
        }.get { Download.toFile(delegate, arquivo) }

    }

    static void salvarHistorico(String competencia, String publicacao, String inicioVigencia) {

        String pastaDownloads = "../downloads"
        String arquivo = "../downloads/Histórico versões Componentes padrão TISS.csv"

        Path caminho = Paths.get(pastaDownloads)

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo, true))){

            if (!Files.exists(caminho)) {
                Files.createDirectories(caminho)
            }

            String linhaCSV = String.format("%s,%s,%s", competencia, publicacao, inicioVigencia)
            escritor.write(linhaCSV)
            escritor.newLine()

        } catch (IOException e) {
            e.getMessage()
        }
    }


}