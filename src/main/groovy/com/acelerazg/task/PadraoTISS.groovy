package com.acelerazg.task

import com.acelerazg.corehttp.Core
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PadraoTISS {

    static void getUltimaVersaoComponentesTISS() {

        Document pagina = Core.getPaginaTISS()
        Element conteudo = pagina.getElementsByClass("internal-link").first()
        String link = conteudo.getElementsByTag("a").attr("href")

        Document pagina2 = Core.getPagina(link)
        conteudo = pagina2.getElementsByTag("tbody").first()
        List<Element> documentos = conteudo.getElementsByTag("tr")

        try{

            documentos.each {tr ->
                List<Element> tdLista = tr.getElementsByTag("td")
                String componentName = tdLista.get(0).text()
                String urlDownload = tr.getElementsByTag("a").attr("href")
                Core.downloadComponents("${urlDownload}", "${componentName}","${componentName}")

            }

        } catch (IOException e){
            e.getMessage()
        }

    }

}