package com.acelerazg.task

import com.acelerazg.corehttp.Core
import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PadraoTISS {

    static void getUltimaVersaoComponentesTISS() {

        Document pagina = Core.getPaginaTISS()
        Element conteudo = pagina.getElementsByClass("internal-link").first()
        String link = conteudo.getElementsByTag("a").attr("href")

        pagina = Core.getPagina(link)
        conteudo = pagina.getElementsByTag("tbody").first()
        List<Element> documentos = conteudo.getElementsByTag("tr")

        try{

            documentos.each {tr ->
                List<Element> tdLista = tr.getElementsByTag("td")
                String componentName = tdLista.get(0).text()
                String urlDownload = tr.getElementsByTag("a").attr("href")
                Core.downloadArquivo("${urlDownload}", "${componentName}","${componentName}")

            }

        } catch (IOException e){
            e.getMessage()
        }

    }

    static void getHistoricoVersaoComponentesTISS() {

        Document pagina = Core.getPaginaTISS()
        Element conteudo = pagina.getElementsByClass("internal-link").get(1)
        String link = conteudo.getElementsByTag("a").attr("href")

        pagina = Core.getPagina(link)
        conteudo = pagina.getElementsByTag("tbody").first()
        List<Element> historico = conteudo.getElementsByTag("tr")

        historico.forEach { tr ->
            List<Element> tdLista = tr.getElementsByTag("td")
            String competencia = tdLista.get(0).text()

            List<String> anoCompetencia = competencia.tokenize("/")
            Integer ano = Integer.parseInt(anoCompetencia[1])

            if (ano >= 2016) {
                String publicacao = tdLista.get(1).text()
                String inicioVigencia = tdLista.get(2).text()
                Core.salvarHistorico(competencia, publicacao, inicioVigencia)
            }
        }
    }

    static void getTabelaErrosEnvioANS(){

        Document pagina = Core.getPaginaTISS()
        Element conteudo = pagina.getElementsByClass("internal-link").get(2)
        String link = conteudo.getElementsByTag("a").attr("href")

        pagina = Core.getPagina(link)
        conteudo = pagina.getElementById("parent-fieldname-text")
        link = conteudo.getElementsByTag("a").first().attr("href")
        String nomeArquivo = conteudo.getElementsByTag("h2").first().text()

        Core.downloadArquivo("${link}","${nomeArquivo}","${nomeArquivo}.xlsx")

    }

}