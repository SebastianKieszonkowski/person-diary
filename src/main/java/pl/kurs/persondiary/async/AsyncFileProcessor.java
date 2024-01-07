package pl.kurs.persondiary.async;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.persondiary.exeptions.ImportLineProcessException;
import pl.kurs.persondiary.services.ImportProgressService;
import pl.kurs.persondiary.services.importcsv.ImportFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AsyncFileProcessor {
    private final ImportFactory importFactory;
    private final ImportProgressService importProgressService;
    private final TransactionTemplate transactionTemplate;

    @Async
    public void processFileAsync(MultipartFile file, String task) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    AtomicLong counter = new AtomicLong(0);
                    try (Stream<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()) {
                        lines.forEach(line -> importPerson(line, counter, task));
                        importProgressService.completeImport(task);
                    } catch (IOException | DuplicateKeyException e) {
                        throw new ImportLineProcessException("Problem witch imported line: " + counter + "!!!\n" + e.getMessage());
                    }
                } catch (Exception e) {
                    importProgressService.logException(task, e);
                    importProgressService.abortedImport(task);
                    status.setRollbackOnly();
                }
            }
        });
        importProgressService.pushImportStatusToDb(task);
    }

    private void importPerson(String line, AtomicLong counter, String task) {
        String[] args = line.split(",");
        importFactory.importPerson(args);
        Long processedLines = counter.incrementAndGet();
        importProgressService.updateProgress(processedLines, task);
    }
}

