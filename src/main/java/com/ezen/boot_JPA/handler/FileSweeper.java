package com.ezen.boot_JPA.handler;

import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.repository.FileRepository;
import com.ezen.boot_JPA.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileSweeper {

    private final BoardServiceImpl boardServiceimpl;
    private final String FILE_DIR = "D:\\_myProject\\_java\\_fileUpload\\";

    @Scheduled(cron = "0 45 17 * * *")
    public void fileSweeperPreview() {
        log.info("FileSweeper operation starts. Time: {}", LocalDateTime.now());
        // ! IntelliJ가 이 메서드가 너무 길다고 리팩토링 할 것을 제안했다. 내가 봐도 너무 길기는 하니까 나눠야겠다.
        List<FileDTO> filesFromDb = boardServiceimpl.getAllFile();

        // "폴더에 있는 파일 경로"는 다음과 같다. ? 파일 형태? 이름 구조? 뭐라고 하지?
        // 썸네일이 없는 경우 = D:\_myProject\_java\_fileUpload\2024\11\04\\uuid_파일명
        // 썸네일이 있는 경우 = D:\_myProject\_java\_fileUpload\2024\11\04\\uuid_thumbnail_파일명
        // DB에서 가져온 FileVO 객체들이 이러한 실제 폴더의 경로를 가지고 있지 않다. saveDir, uuid 등을 가지고 있을 뿐이다.
        // 따라서 DB에서 가져온 FileVO들의 정보를 기반으로 "폴더에 있는 파일 경로" 형태를 만든다: dbFilePathToMatch
        // 만들어진 형태를 나중에 "폴더에 있는 실제 파일 경로"와 비교할 것이다.
        List<String> filePathToMatch = new ArrayList<>();
        for(FileDTO fileDTO : filesFromDb) {
            String saveDir = fileDTO.getSaveDir();
            String uuid = fileDTO.getUuid();
            String fileName = fileDTO.getFileName();
            filePathToMatch.add(FILE_DIR + saveDir + uuid + fileName);

            // 썸네일이 있는 경우
            if(fileDTO.getFileType() > 0) {
                filePathToMatch.add(FILE_DIR + saveDir + uuid + "_thumbnail_" + fileName);
            }
        }
        log.info("Final file path to match: {}", filePathToMatch);

        LocalDate now = LocalDate.now();
        String today = now.toString();
        today = today.replace("-", File.separator);

        File fileInFolder = Paths.get(FILE_DIR + today).toFile();
        File[] allFileObj = fileInFolder.listFiles();

        for(File file: allFileObj) {
            String filePathInFolder = file.toPath().toString();
            if(!filePathToMatch.contains(filePathInFolder)) {
                file.delete();
                log.info(" > Delete files : {}", fileInFolder);
            }
        }
        log.info("> FileSweeper operation finished. : {}", LocalDateTime.now());
    }
}
