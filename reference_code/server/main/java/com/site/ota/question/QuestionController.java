package com.site.ota.question;

import java.security.Principal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import com.site.ota.answer.AnswerForm;
import com.site.ota.user.SiteUser;
import com.site.ota.user.UserService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

   
	private final QuestionService questionService;
	private final UserService userService;

	@GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
    		@RequestParam(value = "kw", defaultValue = "") String kw) {
		log.info("page:{}, kw:{}", page, kw);
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping(value = "/firmware/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
    	Question question = this.questionService.getQuestion(id);
        model.addAttribute("filepath", question.getFilepath());
        log.info(question.getFilepath());
        log.info("FileHash");
        log.info(question.getFileHash());
        log.info("RootHash");
        log.info(question.getRootHash());
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping(value = "/firmware/{id}/JsonUpdate")
    @ResponseBody
    public Map<String, String> updateDetail(@PathVariable("id") Integer id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Question question = this.questionService.getQuestion(id);

        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("Name", question.getSubject());
        keyValueMap.put("Version", question.getContent());
        keyValueMap.put("RootHash", question.getRootHash());
        keyValueMap.put("FileHash", question.getFileHash());
        keyValueMap.put("URL", question.getFilepath());

        return keyValueMap;
    }


    @GetMapping(value = "/firmware/{id}/update")
    public String updateDetail(Model model, @PathVariable("id") Integer id) throws MqttException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("들어옴");
        Question question = this.questionService.getQuestion(id);
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("Name", question.getSubject());
        keyValueMap.put("Version", question.getContent());
        keyValueMap.put("RootHash", question.getRootHash());
        keyValueMap.put("FileHash", question.getFileHash());
        keyValueMap.put("URL", question.getFilepath());

        String jsonPayload = objectMapper.writeValueAsString(keyValueMap);

        // MQTT 메시지 발행
        String broker = "tcp://localhost:3000";
        String clientId = MqttClient.generateClientId();

        IMqttClient mqttClient = new MqttClient(broker, clientId);

        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName("kusecar");
        options.setPassword("kusetest".toCharArray());




        for(int i = 0; i < 1; i++) {  // times 만큼 메시지 발행
            mqttClient.connect(options);
            MqttMessage message = new MqttMessage();
            message.setPayload(jsonPayload.getBytes());
            mqttClient.publish("/MoSE/mqtt", message);
            mqttClient.disconnect();
        }



        return String.format("redirect:/question/firmware/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal, MultipartFile file) throws Exception{
        log.info(principal.getName());
        if(principal.getName().equals("admin") ){
            if(file.isEmpty()){
                log.info("비어있음");
            }
            log.info("안 비어어있음");
            log.info(System.getProperty("user.dir"));



            if (bindingResult.hasErrors()) {
                return "question_form";
            }
            SiteUser siteUser = this.userService.getUser(principal.getName());
            this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, file);
            return "redirect:/question/list";
        }
        else
            log.info("different");
            return "redirect:/";


    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/firmware/%s", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/firmware/%s", id);
    }
}