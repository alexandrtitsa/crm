package ua.com.astone.acrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.astone.acrm.dto.company.CompanyRequest;
import ua.com.astone.acrm.service.CompanyImportService;
import ua.com.astone.acrm.service.CompanyService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class CompanyImportServiceImpl implements CompanyImportService {

    private final CompanyService companyService;

    @Override
    public int importCompanies(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Файл не має імені!");
        }
        try (InputStream is = file.getInputStream()) {
            if (filename.endsWith(".xlsx")) {
                return importFromExcel(is);
            } else if (filename.endsWith(".csv")) {
                return importFromCsv(is);
            } else {
                throw new IllegalArgumentException("Підтримуються лише .xlsx та .csv");
            }
        }
    }

    private int importFromExcel(InputStream is) throws IOException {
        int count = 0;
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();
                try {
                    String name = getCellValue(row.getCell(0));
                    String industry = getCellValue(row.getCell(1));
                    String address = getCellValue(row.getCell(2));
                    String website = getCellValue(row.getCell(3));
                    String description = getCellValue(row.getCell(4));

                    if (name != null && !name.isBlank()) {
                        companyService.create(
                                CompanyRequest.builder()
                                        .name(name)
                                        .industry(industry)
                                        .address(address)
                                        .website(website)
                                        .description(description)
                                        .build()
                        );
                        count++;
                    }
                } catch (Exception ex) {

                }
            }
        }
        return count;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                double num = cell.getNumericCellValue();
                if (num == (long) num) return String.valueOf((long) num);
                return String.valueOf(num);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return null;
        }
    }

    private int importFromCsv(InputStream is) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            String delimiter = detectDelimiter(header);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(delimiter, -1); // -1 зберігає всі порожні поля
                try {
                    if (parts.length > 0 && !parts[0].isBlank()) {
                        companyService.create(buildCompanyFromParts(parts));
                        count++;
                    }
                } catch (Exception ex) {
                    // Логувати помилку з даним рядком
                }
            }
        }
        return count;
    }

    private String detectDelimiter(String header) {
        if (header == null) return ";";
        if (header.contains(";")) return ";";
        if (header.contains(",")) return ",";
        if (header.contains("\t")) return "\t";
        return ";";
    }

    private CompanyRequest buildCompanyFromParts(String[] parts) {
        return CompanyRequest.builder()
                .name(parts[0])
                .industry(parts.length > 1 ? parts[1] : null)
                .address(parts.length > 2 ? parts[2] : null)
                .website(parts.length > 3 ? parts[3] : null)
                .description(parts.length > 4 ? parts[4] : null)
                .build();
    }
}

