package com.practice.firstspringai.service.impl;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;

import com.practice.firstspringai.service.DataLoader;

import lombok.extern.slf4j.Slf4j;



@Service 
@Slf4j 
public class DataLoaderImpl implements DataLoader {

    @Value ("classpath:sample-data.json")
    private Resource jsonResource;

    @Value("classpath:sample-pdf.pdf")
    private Resource pdfResource;

    @Override
    public List<Document> loadDocumentsFromJson() {
        log.info("Json loading started");
        // var jsonReader = new JsonReader(jsonResource);
        var jsonReader = new JsonReader(jsonResource, "users"); //from sample-data users value is used...
        return jsonReader.read();
    }

    @Override
    public List<Document> loadDocumentsFromPdf() {

		PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource,
				PdfDocumentReaderConfig.builder()
					.withPageTopMargin(0)
					.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
						.withNumberOfTopTextLinesToDelete(0)
						.build())
					.withPagesPerDocument(1)
					.build());
		return pdfReader.read();
    }
    
}
