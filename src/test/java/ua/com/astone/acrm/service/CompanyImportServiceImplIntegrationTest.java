package ua.com.astone.acrm.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import ua.com.astone.acrm.dto.company.CompanyRequest;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class CompanyImportServiceImplIntegrationTest {

    @Autowired
    private CompanyImportService companyImportService;

    @MockBean
    private CompanyService companyService;

    @Test
    void importCompanies_fromCsv_shouldCreateCompanies() throws Exception {
        String csv = """
                name;industry;address;website;description
                Google;IT;USA;www.google.com;Search engine
                Apple;IT;USA;www.apple.com;Tech giant
                """;
        MockMultipartFile file = new MockMultipartFile(
                "file", "companies.csv", "text/csv", csv.getBytes()
        );

        int count = companyImportService.importCompanies(file);

        assertThat(count).isEqualTo(2);
        ArgumentCaptor<CompanyRequest> captor = ArgumentCaptor.forClass(CompanyRequest.class);
        verify(companyService, times(2)).create(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(CompanyRequest::getName)
                .containsExactly("Google", "Apple");
    }

    @Test
    void importCompanies_fromExcel_shouldCreateCompanies() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet();
        var header = sheet.createRow(0);
        header.createCell(0).setCellValue("name");
        header.createCell(1).setCellValue("industry");
        header.createCell(2).setCellValue("address");
        header.createCell(3).setCellValue("website");
        header.createCell(4).setCellValue("description");

        var row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Amazon");
        row1.createCell(1).setCellValue("E-commerce");
        row1.createCell(2).setCellValue("USA");
        row1.createCell(3).setCellValue("www.amazon.com");
        row1.createCell(4).setCellValue("Marketplace");

        var row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Meta");
        row2.createCell(1).setCellValue("Social");
        row2.createCell(2).setCellValue("USA");
        row2.createCell(3).setCellValue("www.meta.com");
        row2.createCell(4).setCellValue("Social media");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        MockMultipartFile file = new MockMultipartFile(
                "file", "companies.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", out.toByteArray()
        );

        int count = companyImportService.importCompanies(file);

        assertThat(count).isEqualTo(2);
        ArgumentCaptor<CompanyRequest> captor = ArgumentCaptor.forClass(CompanyRequest.class);
        verify(companyService, times(2)).create(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(CompanyRequest::getName)
                .containsExactly("Amazon", "Meta");
    }
}
