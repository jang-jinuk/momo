package com.momo.momopjt.global;

//import static org.apache.commons.io.file.PathUtils.*;

import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoRepository;
import com.momo.momopjt.photo.PhotoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Component
@RequiredArgsConstructor
public class FileCheckTask {

  //TODO 배포시 리눅스 경로로 변경
  @Value("${UploadPath}")
  private String UploadPath;

  private final PhotoRepository photoRepository;
  private final PhotoService photoService;

  //TODO 꼭 배포 전에 활성화 시키기

  // 매일 자정에 파일 정리 실행
  // @Scheduled(cron = "0 0 0 * * *")
  // 테스트용 20초마다 정리 실행
   @Scheduled(cron = "0/10 * * * * *")
  // 테스트용 30분마다 정리 실행
  // @Scheduled(cron = "0 30 * * * *")
  @Async
   @Transactional
  public void checkFiles() throws Exception {
     log.info("----------------- [checkFiles start]-----------------");
//    log.info("===========================================");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 EEEE");
//    log.info("File Check Task run.................");
    log.info("오늘은 {} ", LocalDateTime.now().format(formatter) + " 입니다.");

     List<String> photos = photoRepository.findAll().stream().map(Photo::toString).collect(Collectors.toList());
     log.info("----------------- [uuids in DB : {}]-----------------",photos);

     String delUUID ="";

     photoRepository.deleteById(delUUID);

//    log.info("-------------------------------------------");
//    // summernote temp 폴더는 항상 삭제
////    if (Paths.get(UploadPath).toFile().exists()) {
////      deleteDirectory(Paths.get(UploadPath));
////      log.info("SummerNote Temp: 삭제 완료.");
////    } else {
////      log.info("SummerNote Temp: 존재하지 않습니다.");
////    }
//    log.info("-------------------------------------------");
//
// //S3 버킷과 DB의 이미지 테이블과 비교해 S3에 없는 파일 제거
////    List<String> uploadedFiles = fileListInBucket();
////    deleteUploadedFiles(uploadedFiles);
//
//  }
//
//  // ImgBoard 에서 파일 찾기 위한 Uuid 추출
//  private String extractionUuid(String originalFileName) {
//
//    int lastIndex = originalFileName.lastIndexOf(".");
//
//    return lastIndex != -1 ? originalFileName.substring(0, lastIndex) : originalFileName;
//
//  }
//
//  // 버킷에 업로드된 파일들의 키값(파일이름)을 가져옴
//  private List<String> fileListInBucket() {
//
//    List<S3ObjectSummary> uploadedS3Files;
//
//    // 버킷에 업로드된 모든 오브젝트를 리스트로 받아옴
//    uploadedS3Files = s3Client.listObjects(bucketName).getObjectSummaries();
//
//    List<String> uploadedFiles = new ArrayList<>();
//
//    // 썸네일 파일 제외하고 리스트로 넣음
//    for (S3ObjectSummary uploadedS3File : uploadedS3Files) {
//
//      if (!uploadedS3File.getKey().contains("t_")) {
//
//        String fileName = uploadedS3File.getKey();
//
//        uploadedFiles.add(fileName);
//      }
//    }
//    return uploadedFiles;
//  }
//
//  // DB에 저장되어있지 않는 업로드 파일들 제거
//  private void deleteUploadedFiles(List<String> uploadedFiles) {
//
//    if (!uploadedFiles.isEmpty()) {
//      log.info("정리 할 S3 업로드 이미지가 없습니다");
//    }
//
//    for (String uploadedFile : uploadedFiles) {
//      String fileUuid = extractionUuid(uploadedFile);
//
//      Optional<ImgBoard> findImageInDatabase = uploadRepository.findById(fileUuid);
//
//      if (findImageInDatabase.isEmpty()) {
//
//        String thumbUploadedFile = "t_" + uploadedFile;
//
//        s3Client.deleteObject(bucketName, uploadedFile);
//        s3Client.deleteObject(bucketName, thumbUploadedFile);
//      }
//    }

    log.info("----------------- [end checkFiles]-----------------");
  }
}
