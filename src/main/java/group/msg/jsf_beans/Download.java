package group.msg.jsf_beans;


import group.msg.entities.Bug;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Download implements Serializable {

    private Path pdfPath;

    private float leading;

    private PDDocument document;

    private List<String> separateLines(String bug, PDPage page, PDPageContentStream contentStream) throws IOException{
        PDFont pdfFont = PDType1Font.TIMES_ROMAN;
        float fontSize = 12;
        leading = 1.5f * fontSize;

        PDRectangle mediabox = page.getMediaBox();
        float margin = 72;
        float width = mediabox.getWidth() - 2*margin;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;

        String bugContent =bug;
        List<String> lines = new ArrayList<>();

        for (String text : bugContent.split(System.lineSeparator())) {
            int lastSpace = -1;
            while (text.length() > 0) {
                int spaceIndex = text.indexOf(' ', lastSpace + 1);
                if (spaceIndex < 0)
                    spaceIndex = text.length();
                String subString = text.substring(0, spaceIndex);
                float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                if (size > width) {
                    if (lastSpace < 0)
                        lastSpace = spaceIndex;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                } else if (spaceIndex == text.length()) {
                    lines.add(text);
                    text = "";
                } else {
                    lastSpace = spaceIndex;
                }
            }
        }

        contentStream.setFont(pdfFont, fontSize);
        contentStream.newLineAtOffset(startX, startY);

        return lines;
    }

    public void bugPDF(List<Bug> bugs, String filename) throws IOException {

        pdfPath=Paths.get(filename+".pdf");
        document=new PDDocument();

        Iterator it=bugs.iterator();

        while(it.hasNext()) {
            Bug bug = (Bug) it.next();

            PDPage pdPage = new PDPage();
            document.addPage(pdPage);

            int pageIndex = document.getPages().indexOf(pdPage);

            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(pageIndex));

            contentStream.beginText();
            List<String> lines=separateLines(bug.toString(),pdPage,contentStream);

            for (String line: lines)
            {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
            }

            contentStream.endText();
            contentStream.close();
        }

        document.save(pdfPath.toFile().getAbsolutePath());
        document.close();

    }


    public StreamedContent getFile() throws IOException {
        InputStream stream =new FileInputStream(pdfPath.toFile().getAbsolutePath());
        StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "downloaded_bug.pdf");


        return file;
    }



}