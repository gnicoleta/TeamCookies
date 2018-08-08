package group.msg.jsf_beans;


import group.msg.entities.Bug;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;


public class Download implements Serializable {

    private Path pdfPath;

    private PDDocument document;

    public void bugPDF(List<Bug> bugs, String filename) throws IOException {

        pdfPath=Paths.get(filename);
        document=new PDDocument();

        Iterator it=bugs.iterator();

        while(it.hasNext()) {
            Bug bug = (Bug) it.next();

            PDPage pdPage = new PDPage();
            document.addPage(pdPage);

            int pageIndex = document.getPages().indexOf(pdPage);

            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(pageIndex));
            contentStream.beginText();
            contentStream.showText(bug.toString());
            contentStream.endText();
            contentStream.close();
        }

        document.save(filename);

    }


    public StreamedContent getFile() throws IOException {
        InputStream stream =new FileInputStream(pdfPath.toFile().getAbsolutePath());
        StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "downloaded_bug.pdf");

        document.close();

        return file;
    }

}

