package com.site.ota.question;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.IOException;

import groovy.util.logging.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.site.ota.DataNotFoundException;
import com.site.ota.answer.Answer;
import com.site.ota.user.SiteUser;

import javax.persistence.criteria.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionService {


    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
            }
        };
    }

    public Page<Question> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
//        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    
    public Question getQuestion(Integer id) {  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Question getVersion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }






    public void create(String subject, String content, SiteUser user, MultipartFile file) throws Exception{

        //  파일 해시 계산
        MerkleTree merkleTree = new MerkleTree();
        byte[] fileBytes = file.getBytes();
        String fileHash = bytesToHex(merkleTree.computeHash(fileBytes));

        //폴더 경로
        String projectPath = System.getProperty("user.dir") + "/files";






        UUID uuid = UUID.randomUUID();

        /*랜덤식별자_원래파일이름 = 저장될 파일이름 지정*/
        String fileName = uuid + "_" + file.getOriginalFilename();


        /*빈 껍데기 생성*/
        /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        // 폴더 내의 .ino 파일들 가져오기
        List<Path> inoFiles = getInoFiles(projectPath);

        // 데이터 배열 구성
        List<byte[]> dataList = new ArrayList<>();
        for (Path inofile : inoFiles) {
            byte[] fileData = readFileData(inofile);
            dataList.add(fileData);
        }

        byte[][] data = dataList.toArray(new byte[0][]);
        // 이진 해시 트리 구축
        byte[] rootHash = merkleTree.buildTree(data);

        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setFilename(fileName);
        q.setFilepath("/files/" + fileName);
        q.setRootHash(bytesToHex(rootHash));
        q.setFileHash(fileHash);
        this.questionRepository.save(q);

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] readFileData(Path filePath) throws IOException {
        File file = filePath.toFile();
        byte[] data = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(data);
        }

        return data;
    }

    private List<Path> getInoFiles(String folderPath) throws IOException {
        List<Path> inoFiles = new ArrayList<>();

        Files.walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".ino"))
                .forEach(inoFiles::add);

        return inoFiles;
    }



    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }


}