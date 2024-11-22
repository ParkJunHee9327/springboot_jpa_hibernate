package com.ezen.boot_JPA.handler;


import com.ezen.boot_JPA.dto.FileDTO;
import groovy.util.logging.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Component
public class FileHandler {

    private final String UPLOAD_DIR = "D:\\_myProject\\_java\\_fileUpload\\";

    public List<FileDTO> uploadFiles (MultipartFile[] files) {
        List<FileDTO> fileDTOList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        // LocalDate인 date를 문자열로 바꾸고 date의 -를 파일 구분자(2024\\11\\15. 파일 경로가 됨)로 만들어주겠다.
        String today = date.toString().replace("-", File.separator);

        // 파일 객체를 만든다. 파일 객체: 파일 경로 + 날짜 + 확장자.
        // 파일 경로에 날짜를 붙여서 폴더를 만든다: D:\_myProject\_java\_fileUpload\2024\11\15
        File folders = new File(UPLOAD_DIR, today);

        // 해당 폴더가 없으면 만든다. .mkdir(): 하나의 폴더 만들기. .mkdirs(): 여러 개 만들기.
        if(!folders.exists()) {
            folders.mkdirs(); // ? 왜 여러 개 만든다고?
        }

        // 한 파일 당 하나의 FileVO를 만들어야 한다.
        for(MultipartFile file : files) {
            FileDTO fileDTO = new FileDTO();
            // application.properties에 파일 경로를 명시해놨다. 우리는 뒷부분(날짜)만 신경쓰면 된다.
            fileDTO.setSaveDir(today);
            // 파일이 원래 이름과 크기는 가지고 있다. 넣어주면 된다.
            fileDTO.setFileSize(file.getSize());

            // 보통 파일 이름에 파일 경로가 포함된 경우가 많다. 예시: /test/test.txt
            // 나는 test.txt만 필요하다.
            String originalFileName = file.getOriginalFilename();
            String onlyFileName = originalFileName.substring(
                    originalFileName.lastIndexOf(File.separator)+1);
            fileDTO.setFileName(onlyFileName);

            UUID uuid = UUID.randomUUID(); // ? UUID는 뭐하는 클래스인지 알아봐야겠음.
            // 자주 쓸 거라 uuid를 UUID 객체가 아니라 문자열로 바꿨다.
            String uuidStr = uuid.toString();
            fileDTO.setUuid(uuidStr);

            /* ---- fileVO 설정 완료. ---- */
            // D드라이브 파일(디스크)에 저장함.
            String fileName = uuidStr + "_" + onlyFileName;
            // 저장할 용도의 새로운 File 객체를 만들었다.
            File storeFile = new File(folders, fileName);

            // 파일을 저장한다.
            try {
                file.transferTo(storeFile);
                // 이미지 파일인지 아닌지 파일 타입을 확인하려 isImageFile 메서드를 사용한다.
                if(isImageFile(storeFile)) {
                    fileDTO.setFileType(1);
                    // 그림 파일만 썸네일을 생성할 수 있다.
                    File thumbnail = new File(folders, uuidStr + "_thumbnail_" + onlyFileName);
                    Thumbnails.of(storeFile).size(100, 100).toFile(thumbnail); // toFile: thumbnail이라는 File 객체로 생성.
                }
            } catch (Exception e) {
                log.info("An error occurred while storing file.", e);
            }

            // 파일 리스트에 최종 FileVO를 삽입한다.
            fileDTOList.add(fileDTO);
        }

        return fileDTOList;
    }

    // 파일을 받아 이미지 파일인지 아닌지 확인하는 메서드. Tika 라이브러리를 사용했다.
    private boolean isImageFile(File file) throws IOException {
        String fileType = new Tika().detect(file);
        return fileType.startsWith("image"); // 맞으면 true 아니면 false를 반환함.
    }
}
